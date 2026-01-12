package com.ecommerce.cart.exceptions;

@SuppressWarnings("serial")
public class CartNotExistsException extends RuntimeException{
	public CartNotExistsException(String message) {
		super(message);
	}
}
