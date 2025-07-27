package com.nwg.EzPay.service;

import java.util.List;
import java.util.Date;

import com.nwg.EzPay.model.Transaction;

public interface ITransactionService {

	Transaction getTransactionByIdService(String transactionId);

	List<Transaction> getTransactionByTypeService(String type);

	List<Transaction> getTransactionByStatusService(String status);

	List<Transaction> getTranstaionByDateService(Date date);

	List<Transaction> getTransactionByDateRangeService(Date startDate, Date endDate);

	List<Transaction> getTransactionByAmountRangeService(Double startAmount, Double endAmount);

	boolean createTransactionService(Transaction transaction);

	boolean deleteTransactionService(Transaction transaction);

	boolean readTransactionService(Transaction transaction);

	boolean updateTransactionService(Transaction transaction);
}
