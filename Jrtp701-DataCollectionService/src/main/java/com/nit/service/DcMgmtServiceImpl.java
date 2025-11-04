package com.nit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.binding.ChildrenInputs;
import com.nit.binding.CitizenAppRegistrationInputs;
import com.nit.binding.DcSummaryReport;
import com.nit.binding.EducationInputs;
import com.nit.binding.IncomeInputs;
import com.nit.binding.PlanSelectionInputs;
import com.nit.entity.CitizenAppRegistrationEntity;
import com.nit.entity.DcCaseEntity;
import com.nit.entity.DcChildrenEntity;
import com.nit.entity.DcEducationEntity;
import com.nit.entity.DcIncomeEntity;
import com.nit.entity.PlanEntity;
import com.nit.repository.ICitizenRegistrationRepository;
import com.nit.repository.IDcCaseRepository;
import com.nit.repository.IDcChildrenRepository;
import com.nit.repository.IDcEducationRepository;
import com.nit.repository.IDcIncomeRepository;
import com.nit.repository.IPlanRepository;

@Service
public class DcMgmtServiceImpl implements IDcMgmtService {
    @Autowired
	private  IDcCaseRepository  caseRepo;
    @Autowired
    private  ICitizenRegistrationRepository  citizenRepo;
    @Autowired
    private  IPlanRepository   planRepo;
    @Autowired
    private  IDcIncomeRepository  incomeRepo;
    @Autowired
    private IDcEducationRepository  educationRepo;
    @Autowired
    private IDcChildrenRepository  childRepo;
    
    @Override
	public Integer generateCaseNo(Integer appId) {
		// load citizen data
		Optional<CitizenAppRegistrationEntity>  appCitizen  = citizenRepo.findById(appId);
		if(appCitizen.isPresent()) {
			DcCaseEntity  caseEntity  = new  DcCaseEntity();
			caseEntity.setAppId(appId);
			return caseRepo.save(caseEntity).getCaseNo();//save obj operation 
		}
		return 0;
	}

    @Override
	public List<String> showAllPlanNames() {
		List<PlanEntity>  plansList = planRepo.findAll();
		//get only plan name
	   List<String>  planNamesList = plansList.stream().map(plan->plan.getPlanName()).toList();
		return planNamesList;
	}

    @Override
	public Integer savePlanSelection(PlanSelectionInputs plan) {
		// load DcCaseEntity obj
		Optional<DcCaseEntity>  opt = caseRepo.findById(plan.getCaseNo());
		if(opt.isPresent()) {
			DcCaseEntity  caseEntity = opt.get();
			caseEntity.setPlanId(plan.getPlanId());
			//update the DcCaseEntity obj with plan id
			caseRepo.save(caseEntity);// update obj operation
			return  caseEntity.getCaseNo();
		}
		return 0;
	}

    @Override
	public Integer saveIncomeDetails(IncomeInputs income) {
		// convert the binding obj data to entity class obj data
		DcIncomeEntity  incomeEntity = new DcIncomeEntity();
		BeanUtils.copyProperties(income, incomeEntity);
		//save income details
		incomeRepo.save(incomeEntity);
		//return caseNo
		return income.getCaseNo();
	}

    @Override
	public Integer saveEducationDetails(EducationInputs education) {
		// convert the binding obj data to entity obj data
		DcEducationEntity  educationEntity  = new DcEducationEntity();
		BeanUtils.copyProperties(education, educationEntity);
		//save the edu obj
		educationRepo.save(educationEntity);
		//return caseNo
		return education.getCaseNo();
	}

    @Override
	public Integer saveChildrenDetails(List<ChildrenInputs> children) {
		// convert the each binding obj to each entity class obj
		children.forEach(child->{
		   DcChildrenEntity childEntity = new DcChildrenEntity();
		   //save each child obj
		   childRepo.save(childEntity);
		});
		return children.get(0).getCaseNo();
	}

    @Override
	public DcSummaryReport showDcSummary(Integer caseNo) {
		// get multiple entity obj based on caseno
		DcIncomeEntity  incomeEntity = incomeRepo.findByCaseNo(caseNo);
		DcEducationEntity  educationEntity = educationRepo.findByCaseNo(caseNo);
	    List<DcChildrenEntity>  childsEntityList = childRepo.findByCaseNo(caseNo);
	    Optional<DcCaseEntity>  optCaseEntity = caseRepo.findById(caseNo);
	    //get planName
	    String planName = null;
	    Integer appId= null;
	    if(optCaseEntity.isPresent()) {
	    	DcCaseEntity  caseEntity = optCaseEntity.get();
	    	Integer  planId = caseEntity.getPlanId();
	    	appId = caseEntity.getAppId();
	    	Optional<PlanEntity> optPlanEntity = planRepo.findById(planId);
	    	if(optPlanEntity.isPresent()) {
	    		planName = optPlanEntity.get().getPlanName();
	    	}
	    }
	    Optional<CitizenAppRegistrationEntity> optCitizenEntity =citizenRepo.findById(appId);
	    CitizenAppRegistrationEntity citizenEntity = null;
	    if(optCitizenEntity.isPresent()) {
	    	citizenEntity = optCitizenEntity.get();
	    }	
	    	
	    	//convert entity obj to binding obj
	    	IncomeInputs income = new IncomeInputs();
	    	BeanUtils.copyProperties(incomeEntity, income);
	    	
	    	EducationInputs education = new EducationInputs();
	    	BeanUtils.copyProperties(educationEntity, education);
	    	
	    	List<ChildrenInputs> listChilds = new ArrayList();
	    	childsEntityList.forEach(childEntity->{
	    		ChildrenInputs child = new ChildrenInputs();
	    		BeanUtils.copyProperties(childEntity, child);
	    		listChilds.add(child);
	    	});
	    	CitizenAppRegistrationInputs  citizen = new CitizenAppRegistrationInputs();
	    	BeanUtils.copyProperties(citizenEntity, citizen);
	    	
	    	
			//prepare DcSummary report
			DcSummaryReport report = new DcSummaryReport();
			report.setPlanName(planName);
			report.setCitizenDetails(citizenEntity);
			report.setIncomeDetails(income);
			report.setEducationDetails(education);
			report.setChildrenDetails(listChilds);
			return report;
	  
	}

}
