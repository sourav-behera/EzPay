package com.nwg.ezpay.service;

import java.util.List;
import java.util.Date;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.model.Transaction;

public interface ITransactionService {

	Transaction getTransactionByIdService(String transactionId) throws InvalidTransactionIDException;

	List<Transaction> getTransactionByTypeService(String type) throws InvalidTransactionTypeException;

	List<Transaction> getTransactionByStatusService(String status) throws InvalidTransactionStatusException;

	List<Transaction> getTransactionByDateService(Date date) throws InvalidDateFormatException;

	List<Transaction> getTransactionByDateRangeService(Date startDate, Date endDate) throws InvalidDateFormatException, InvalidRangeException;

	List<Transaction> getTransactionByAmountRangeService(Double startAmount, Double endAmount) throws InvalidRangeException;

	Transaction createTransactionService(Transaction transaction) throws InvalidTransactionObjectException;

	boolean deleteTransactionService(String transactionId) throws InvalidTransactionIDException;

	Transaction updateTransactionService(Transaction transaction) throws InvalidTransactionObjectException;
}
