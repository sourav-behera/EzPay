package com.nwg.EzPay.exception;

public class InvalidTransactionTypeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidTransactionTypeException(String message) {
		super(message);
	}

}
