package com.nwg.EzPay.exception;

public class InvalidTransactionStatusIdException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTransactionStatusIdException(String message) {
        super(message);
    }
}
