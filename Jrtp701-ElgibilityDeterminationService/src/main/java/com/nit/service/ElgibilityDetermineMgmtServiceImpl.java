package com.nit.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.binding.ElgibilityDetailsOutput;
import com.nit.entity.CitizenAppRegistrationEntity;
import com.nit.entity.CoTriggersEntity;
import com.nit.entity.DcCaseEntity;
import com.nit.entity.DcChildrenEntity;
import com.nit.entity.DcEducationEntity;
import com.nit.entity.DcIncomeEntity;
import com.nit.entity.ElgibilityDetailsEntity;
import com.nit.entity.PlanEntity;
import com.nit.repository.ICitizenRegistrationRepository;
import com.nit.repository.ICoTriggersRepository;
import com.nit.repository.IDcCaseRepository;
import com.nit.repository.IDcChildrenRepository;
import com.nit.repository.IDcEducationRepository;
import com.nit.repository.IDcIncomeRepository;
import com.nit.repository.IElgibilityDetermineRepository;
import com.nit.repository.IPlanRepository;

@Service
public class ElgibilityDetermineMgmtServiceImpl implements IElgibilityDeterminationMgmtService {
    
	@Autowired
	private  IDcCaseRepository  caseRepo;
	@Autowired
	private IPlanRepository  planRepo;
	@Autowired
	private IDcIncomeRepository  incomeRepo;
	@Autowired
	private  IDcChildrenRepository  childRepo;
	@Autowired
	private  ICitizenRegistrationRepository  citizenRepo;
	@Autowired
	private  IDcEducationRepository  educationRepo;
	@Autowired
	private  ICoTriggersRepository  triggerRepo;
	@Autowired
	private  IElgibilityDetermineRepository  elgibilityRepo;
	@Override
	public ElgibilityDetailsOutput determineElgibility(int caseNo) {
		Integer  appId = null;
		Integer  planId = null;
		//get appId and planId based on caseNo
		Optional<DcCaseEntity>  optCaseEntity = caseRepo.findById(caseNo);
		System.out.println("Display plan name :: " );
		if(optCaseEntity.isPresent()) {
			DcCaseEntity  caseEntity  = optCaseEntity.get();
			System.out.println("Display plan id :: " + caseEntity.getAppId());
			appId=caseEntity.getAppId();
			planId=caseEntity.getPlanId();
			System.out.println("Display plan id :: " + planId);
			
		}
			//get planName
			String planName=null;
			System.out.println("Display plan Id :: " + planId);
			
			Optional<PlanEntity>  optPlanEntity = planRepo.findById(planId);
			System.out.println("Display plan name :: " );
			if(optPlanEntity.isPresent()) {
				PlanEntity  planEntity = optPlanEntity.get();
				System.out.println("Display plan name :: " +planEntity.getPlanName());
				planName=planEntity.getPlanName();
			}
			//calculate citizen age by getting citizen DOB through  appId
			Optional<CitizenAppRegistrationEntity>  optCitizenEntity  = citizenRepo.findById(planId);
			
			int citizenAge=0;
			String citizenName=null;
			if(optCitizenEntity.isPresent()) {
				CitizenAppRegistrationEntity  citizenEntity = optCitizenEntity.get();
				LocalDate citizenDOB = citizenEntity.getDob();
				citizenName=citizenEntity.getFullName();
				LocalDate  sysDate=LocalDate.now();
				citizenAge=Period.between(citizenDOB, sysDate).getYears();
			}
			//call helper method to plan condition
			ElgibilityDetailsOutput elgOutput = applyPlanConditions(caseNo, planName, citizenAge);
		      
			//save Elgibility entity obj
			ElgibilityDetailsEntity  elgiEntity = new ElgibilityDetailsEntity();
			BeanUtils.copyProperties(elgOutput, elgiEntity);
			elgibilityRepo.save(elgiEntity);
			
			//save co triggers obj
			CoTriggersEntity   triggerEntity = new CoTriggersEntity();
			triggerEntity.setCaseNo(caseNo);
			triggerEntity.setTriggerStatus("pending");
			triggerRepo.save(triggerEntity);
			return elgOutput;
		
	}
	//helper method
	private ElgibilityDetailsOutput   applyPlanConditions(Integer caseNo, String planName, int citizenAge) {
		ElgibilityDetailsOutput  elgOutput = new ElgibilityDetailsOutput();
		System.out.println("display plan status :"+elgOutput.getPlanStatus());
		elgOutput.setPlanName(planName);
		
		//get income details of the citizen
		DcIncomeEntity  incomeEntity = incomeRepo.findByCaseNo(caseNo);
		double empIncome = incomeEntity.getEmpIncome();
		double propertyIncome = incomeEntity.getPropertyIncome();
		
		//for SNAP
		if(planName.equalsIgnoreCase("SNAP")) {
			if(empIncome<=300) {
				elgOutput.setPlanStatus("Approved");
				elgOutput.setBenefitAmt(200.0);
			}
			else {
				elgOutput.setPlanStatus("Denied");
				elgOutput.setDenialReason("High income");
			}
		}
		else if(planName.equalsIgnoreCase("CCAP")) {
			         boolean  kidsCountCondition=false;
			         boolean  kidAgeCondition=true;
			          
			         List<DcChildrenEntity>  listChilds = childRepo.findByCaseNo(caseNo);
			         if(!listChilds.isEmpty()) {
			        	         kidsCountCondition=true;
			        	         
			        	         for(DcChildrenEntity  child:listChilds) {
			        	        	        int kidAge = Period.between(child.getChildDOB(), LocalDate.now()).getYears();
			        	        	        if(kidAge>16) {
			        	        	        	         kidAgeCondition=false;
			        	        	        	         break;
			        	        	        }//if
			        	         }//for
			         }//if
			         if(empIncome<=300 && kidsCountCondition && kidAgeCondition) {
			        	    elgOutput.setPlanStatus("Approved");
			        	    elgOutput.setBenefitAmt(300.0);
			         }
			         else {
			        	    elgOutput.setPlanStatus("Denied");
			        	    elgOutput.setDenialReason("CCAP roles are not satisfied");
			         }
		}
		else if(planName.equalsIgnoreCase("MEDAID")) {
			      if(empIncome<=300 && propertyIncome==0) {
			    	  System.out.println("display plan status :"+elgOutput.getPlanStatus());
			    	  elgOutput.setPlanStatus("Approved");
			          elgOutput.setBenefitAmt(200.0);
			      }
			      else {
			        	 elgOutput.setPlanStatus("Denied");
			        	 elgOutput.setDenialReason("MADAID roles are not satisfied");
			         }
		}
		else if(planName.equalsIgnoreCase("MEDCARE")) {
	         if(citizenAge>=65) {
	        	 elgOutput.setPlanStatus("Approved");
	        	 elgOutput.setBenefitAmt(350.0);
	        }
	         else {
	        	 elgOutput.setPlanStatus("Denied");
	        	 elgOutput.setDenialReason("MADCARE roles are not satisfied");
	         }
        }
		else if(planName.equalsIgnoreCase("CAJW")) {
			       DcEducationEntity  educationEntity = educationRepo.findByCaseNo(caseNo);
			       int passOutYear =educationEntity.getPassOutYear();
			       if(empIncome==0 && passOutYear<LocalDate.now().getYear()) {
			    	   elgOutput.setPlanStatus("Approved");
				       elgOutput.setBenefitAmt(300.0);
			       }
			       else {
			        	 elgOutput.setPlanStatus("Denied");
			        	 elgOutput.setDenialReason("CAJW roles are not satisfied");
			         }
		}
		else if(planName.equalsIgnoreCase("QHP")) {
		      if(citizenAge>=1) {
		    	  elgOutput.setPlanStatus("Approved");
		          
		      }
		      else {
		        	 elgOutput.setPlanStatus("Denied");
		        	 elgOutput.setDenialReason("QHP roles are not satisfied");
		         }
	}
		//set the common properties for elgOutput obj only if the plan is approved
		if(elgOutput.getPlanStatus().equalsIgnoreCase("Approved")) {
			      elgOutput.setPlanStartDate(LocalDate.now());
			      elgOutput.setPlanEndDate(LocalDate.now().plusYears(2));
		}
		return elgOutput;
	}

}
