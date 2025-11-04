package com.nit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlanControllerAdvice {
	@ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String>  handleIAE(IllegalArgumentException iae){
    	return  new ResponseEntity(iae.getMessage(), HttpStatus.OK);
    }
	
	@ExceptionHandler(Exception.class)
    public  ResponseEntity<String>  handleAllException(Exception e){
    	return  new ResponseEntity(e.getMessage(), HttpStatus.OK);
    }
}
