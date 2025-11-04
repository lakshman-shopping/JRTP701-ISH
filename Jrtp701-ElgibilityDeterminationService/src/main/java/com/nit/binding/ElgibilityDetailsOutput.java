package com.nit.binding;

import java.time.LocalDate;

import lombok.Data;


@Data
public class ElgibilityDetailsOutput {
	private String  holderName;
    private String  planName;
    private String  planStatus="Approved";
    private LocalDate  planStartDate;
    private LocalDate  planEndDate;
    private  Double  benefitAmt;
    private  String denialReason;
}
