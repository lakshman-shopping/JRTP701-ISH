package com.nit.binding;

import java.util.List;

import com.nit.entity.CitizenAppRegistrationEntity;

import lombok.Data;

@Data
public class DcSummaryReport {
     private EducationInputs  educationDetails;
     private List<ChildrenInputs>  childrenDetails;
     private IncomeInputs  incomeDetails;
     private CitizenAppRegistrationEntity  citizenDetails;
     private String  planName;
}
