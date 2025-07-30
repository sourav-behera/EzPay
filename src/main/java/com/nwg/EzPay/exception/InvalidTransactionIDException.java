package com.nwg.ezpay.exception;

public class InvalidTransactionIDException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionIDException(String message) {
		super(message);
	}
}
