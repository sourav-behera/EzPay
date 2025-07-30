package com.nwg.ezpay.exception;

public class InvalidTransactionStatusException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionStatusException(String message) {
		super(message);
	}
}
