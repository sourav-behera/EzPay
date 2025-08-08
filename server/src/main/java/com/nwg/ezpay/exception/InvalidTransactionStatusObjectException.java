package com.nwg.ezpay.exception;

public class InvalidTransactionStatusObjectException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTransactionStatusObjectException(String message) {
        super(message);
    }
}
