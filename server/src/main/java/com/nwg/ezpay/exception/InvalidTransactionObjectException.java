package com.nwg.ezpay.exception;

public class InvalidTransactionObjectException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionObjectException(String message) {
		super(message);
	}
}
