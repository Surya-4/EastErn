package com.ecommerce.products.exceptions;

@SuppressWarnings("serial")
public class ProductAlreadyExistsException extends RuntimeException{
	public ProductAlreadyExistsException(String message) {
		super(message);
	}
}
