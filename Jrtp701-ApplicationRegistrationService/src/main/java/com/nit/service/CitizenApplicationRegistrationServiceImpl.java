package com.nit.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.nit.bindings.CitizenAppRegistrationInputs;
import com.nit.entity.CitizenAppRegistrationEntity;
import com.nit.exceptions.InvalidSSNException;
import com.nit.repository.ICitizenRegistrationRepository;

import reactor.core.publisher.Mono;


@Service
public class CitizenApplicationRegistrationServiceImpl implements ICitizenApplicationRegistrationService {
    @Autowired 
	private ICitizenRegistrationRepository citizenRepo;
	/*
	  @Autowired  
	  private RestTemplate template;
	 */
    @Autowired
    private WebClient client;
    
    @Value("${ar.ssa-web.url}")
    private String endpointUrl;
    @Value("${ar.state}")
    private String targetState;
	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException {
		//External rest api url
		//String SSAWebUrl="http://localhost:9090/ssa-web-api/find/{ssn}";
		//perform restfull webservice call to check wheather SSN is valid or not and to get the state name
		//ResponseEntity<String> response = template.exchange(endpointUrl,HttpMethod.GET,null,String.class,inputs.getSsn());
		//get state name
		Mono<String> response=client.get().uri(endpointUrl,inputs.getSsn()).retrieve()
				                                   .onStatus(HttpStatus.BAD_REQUEST::equals,res->res.bodyToMono(String.class).map(ex->new InvalidSSNException("invalid SSN"))).bodyToMono(String.class);
		String stateName = response.block();
		//register citizen if he belongs to california state(CA)
		if(stateName.equalsIgnoreCase(targetState)) {
			//prepare the entity object
			CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			//save the object
			int appId = citizenRepo.save(entity).getAppId();
			return appId;
		}
		throw new InvalidSSNException("inavlid SSN");
	}

}
