package com.nwg.EzPay.exception;

public class InvalidTransactionStatusObjectException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTransactionStatusObjectException(String message) {
        super(message);
    }
}
