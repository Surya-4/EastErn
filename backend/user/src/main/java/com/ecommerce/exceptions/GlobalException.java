package com.ecommerce.exceptions;

@SuppressWarnings("serial")
public class GlobalException extends Exception{
	public GlobalException() {
		super("Unknown Error Occured, Please try again");
	}
}
