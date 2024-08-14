package com.nit.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.bindings.CitizenAppRegistrationInputs;
import com.nit.service.ICitizenApplicationRegistrationService;

@RestController
@RequestMapping("/CitizenAR-api")
public class CitizenApplicationRegistrationOperationsController {
	@Autowired
    private ICitizenApplicationRegistrationService registerService;
	
	@PostMapping("/save")
	public ResponseEntity<String>	  saveCitizenApplication(@RequestBody CitizenAppRegistrationInputs inputs){
		
		try {
			//use service
			int appId  = registerService.registerCitizenApplication(inputs);
			if(appId>0)
				return new ResponseEntity<String>("Citizen Application is registered with the Id::"+appId,HttpStatus.CREATED);
			else
				return new ResponseEntity<String>("Invalid SSN OR Citizen must belongs to california state::"+appId,HttpStatus.OK);
		}//try
		catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}//method
	   
}//class
