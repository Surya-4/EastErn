package com.ecommerce.products.exceptions;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException{
	public  ProductNotFoundException(String message) {
		super(message);
	}
}
