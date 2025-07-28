package com.nwg.EzPay.service;

import java.util.List;
import java.util.Date;

import com.nwg.EzPay.exception.InvalidDateFormatException;
import com.nwg.EzPay.exception.InvalidRangeException;
import com.nwg.EzPay.exception.InvalidTransactionIDException;
import com.nwg.EzPay.exception.InvalidTransactionObjectException;
import com.nwg.EzPay.exception.InvalidTransactionStatusException;
import com.nwg.EzPay.exception.InvalidTransactionTypeException;
import com.nwg.EzPay.model.Transaction;

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
