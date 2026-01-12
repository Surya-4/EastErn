package com.ecommerce.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.cart.exceptions.CartNotExistsException;

@ControllerAdvice
public class CartControllerAdvice {
	@ExceptionHandler(CartNotExistsException.class)
	public ResponseEntity<String> handleCartNotExistsException(CartNotExistsException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown Error, please try again");
	}
}
