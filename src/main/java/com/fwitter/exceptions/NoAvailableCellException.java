package com.fwitter.exceptions;


public class NoAvailableCellException extends RuntimeException {
    public NoAvailableCellException(String message) {
        super(message);
    }
}