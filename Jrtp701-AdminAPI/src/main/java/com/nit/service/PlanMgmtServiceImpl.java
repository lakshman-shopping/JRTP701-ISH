package com.nit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.bindings.PlanData;
import com.nit.entity.PlanCategory;
import com.nit.entity.PlanEntity;
import com.nit.repository.IPlanCategoryRepository;
import com.nit.repository.IPlanRepository;

@Service
public class PlanMgmtServiceImpl implements IPlanMgmtService {
	@Autowired
     private IPlanRepository PlanRepo;
	@Autowired
     private IPlanCategoryRepository planCategoryRepo;
	
	@Override
	public String registerPlan(PlanData plan) {
		//convert plan data binding obj to planEntity
		PlanEntity  entity = new PlanEntity();
		BeanUtils.copyProperties(plan, entity);
		
		//save the object
		PlanEntity savedEntity = PlanRepo.save(entity);
//		if(saved.getPlanId()!=null)
//			return "Travel Plan is saved with id value::"+saved.getPlanId();
//		else
//			return " Problem is saving the Travel Plan";
		return savedEntity.getPlanId()!=null?"Travel Plan is saved with id value::"+savedEntity.getPlanId():" Problem is saving the Travel Plan";
		
	}

	@Override
	public Map<Integer, String> getPlanCategories() {
		//get all travelplanCategory
		List<PlanCategory> list = planCategoryRepo.findAll();
		@SuppressWarnings("unused")
		Map<Integer, String> categoriesMap = new HashMap<Integer,String>();
		list.forEach(category->{
			categoriesMap.put(category.getCategoryId(), category.getCategoryName());
		});
		return categoriesMap;
	}

	@Override
	public List<PlanData> showAllPlans() {
	      List<PlanEntity>  listEntities = PlanRepo.findAll();
	      List<PlanData>  listPlanData = new ArrayList();
	      listEntities.forEach(entity ->{
	    	  PlanData  data = new PlanData();
	    	  BeanUtils.copyProperties(entity, data);
	    	  listPlanData.add(data);
	      });
	      return  listPlanData;
	  }

	@Override
	public PlanData showPlanById(Integer planId) {
	PlanEntity entity =  PlanRepo.findById(planId).orElseThrow(()->new IllegalArgumentException("Travel Plan is not found"));
	   //convert plan data binding to plan entity
	    PlanData  data = new PlanData();
	    BeanUtils.copyProperties(entity, data);
	    return data;
	/*
	 * Optional<TravelPlan> opt = travelPlanRepo.findById(planId);
	 * if(opt.isPresent()) { return opt.get(); } else { throw new
	 * IllegalArgumentException("Travel Plan id is not found"); }
	 */
	}

	@Override
	public String updatePlan(PlanData plan) {
		Optional<PlanEntity> optEntity = PlanRepo.findById(plan.getPlanId());
		if(optEntity.isPresent()) {
		//update the object
			System.out.println("plan id:"+plan.getPlanId());
			PlanEntity entity = new PlanEntity();
			BeanUtils.copyProperties(entity, plan);
		PlanRepo.save(entity);
		return plan.getPlanId()+ "Travel Plan is updated";
		}
		else {
			return plan.getPlanId()+ " travel Plan is not found";
					
		}
	}

	@Override
	public String deletePlan(Integer planId) {
		Optional<PlanEntity> opt = PlanRepo.findById(planId);
		if(opt.isPresent()) {
			//update the object
			PlanRepo.deleteById(planId);
		    return planId+" Travel Plan is deleted";
		}
       else {
			return planId+ "Travel Plan is not found";
       }
	}

	@Override
	public String changePlanStatus(Integer planId, String status) {
		Optional<PlanEntity> opt = PlanRepo.findById(planId);
		if(opt.isPresent()) {
			PlanEntity entity = opt.get();
			entity.setActiveSw(status);
			PlanRepo.save(entity);
			return planId+ "Travel Plan status is changed";
		}
		else {
		return planId+ "Travel Plan is not found";
		}
	}

}
