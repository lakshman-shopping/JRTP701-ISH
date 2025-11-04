package com.nit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nit.exceptions.InvalidSSNException;

@RestControllerAdvice
public class CitizenRegisterationControllerAdvice {
	
    @ExceptionHandler(InvalidSSNException.class)
	public ResponseEntity<String> handleInvalidSSN(InvalidSSNException exception){
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception exception){
    	return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
