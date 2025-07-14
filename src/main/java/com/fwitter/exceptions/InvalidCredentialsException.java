package com.fwitter.exceptions;

public class InvalidCredentialsException extends Exception{
	public InvalidCredentialsException() {
		super("Username or password does not exist");
	}
}
