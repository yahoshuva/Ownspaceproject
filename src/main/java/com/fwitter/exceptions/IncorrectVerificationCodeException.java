package com.fwitter.exceptions;

public class IncorrectVerificationCodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectVerificationCodeException() {
		super("The code passed did not match the users verification code");
	}

}
