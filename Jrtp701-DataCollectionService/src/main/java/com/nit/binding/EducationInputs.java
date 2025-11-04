package com.nit.binding;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class EducationInputs {
	private Integer  educationId;
    private Integer  caseNo;
    private String  highestQlfy;
    private Integer  passOutYear;
}
