package com.nit.service;

import java.util.List;

import com.nit.binding.ChildrenInputs;
import com.nit.binding.DcSummaryReport;
import com.nit.binding.EducationInputs;
import com.nit.binding.IncomeInputs;
import com.nit.binding.PlanSelectionInputs;

public interface IDcMgmtService {
     public  Integer  generateCaseNo(Integer appId);
     public  List<String>  showAllPlanNames();
     public  Integer savePlanSelection(PlanSelectionInputs plan);
     public Integer  saveIncomeDetails(IncomeInputs income);
     public Integer  saveEducationDetails(EducationInputs education);
     public Integer  saveChildrenDetails(List<ChildrenInputs> children);
     public DcSummaryReport showDcSummary(Integer caseNo);
}
