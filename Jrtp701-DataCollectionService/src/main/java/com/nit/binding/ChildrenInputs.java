package com.nit.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChildrenInputs {
	private  Integer  childId;
    private Integer  caseNo;
    private LocalDate  childDOB;
    private Integer  childssn;
}
