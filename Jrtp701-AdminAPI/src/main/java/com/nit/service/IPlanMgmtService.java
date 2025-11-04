package com.nit.service;

import java.util.List;
import java.util.Map;

import com.nit.bindings.PlanData;


public interface IPlanMgmtService {
     public String registerPlan(PlanData  plan); //save operation
     public Map<Integer, String> getPlanCategories(); //for select operation
     public  List<PlanData>  showAllPlans();  //select for operation
     public PlanData  showPlanById(Integer planId);  //for edit operation lunch(To show the existing record for editing)
     public String updatePlan(PlanData plan); //for edit operation from submission
     public String deletePlan(Integer planId);  //for deletion operation
     public String changePlanStatus(Integer planId, String status); //for soft deletion activity
     
}
