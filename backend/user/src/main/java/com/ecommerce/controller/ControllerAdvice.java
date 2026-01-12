package com.ecommerce.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.exceptions.UserAlreadyExistsException;
import com.ecommerce.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserExistsException(UserAlreadyExistsException e){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e ) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e ) {
		logger.error("exception caught",e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong, please try again");
	}
}
