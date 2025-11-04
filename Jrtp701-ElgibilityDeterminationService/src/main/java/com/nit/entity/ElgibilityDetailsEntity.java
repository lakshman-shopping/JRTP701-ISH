package com.nit.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;



@Entity
@Table(name="JR701_ELGIBILITY_DETERMINATION")
@Data
@NoArgsConstructor
public class ElgibilityDetailsEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer  edTraceId;
    private Integer  caseNo;
    @Column(length=50)
    private String  holderName;
    private Long  holderSSN;
    @Column(length=30)
    private String  planName;
    @Column(length=30)
    @NonNull
    private String  planStatus;
    private LocalDate  planStartDate;
    private LocalDate  planEndDate;
    private  Double  benefitAmt;
    @Column(length=60)
    private  String denialReason;
}
