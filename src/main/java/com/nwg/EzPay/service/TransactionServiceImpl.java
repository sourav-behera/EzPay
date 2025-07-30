package com.nwg.ezpay.service;

import java.util.Date;
import java.util.List;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.model.Transaction;
import com.nwg.ezpay.dao.ITransactionDAO;
import com.nwg.ezpay.dao.TransactionDAOImpl;

public class TransactionServiceImpl implements ITransactionService {

	ITransactionDAO iTransactionDAO;

	public TransactionServiceImpl() {
		iTransactionDAO = new TransactionDAOImpl();
	}

	
	@Override
	public Transaction getTransactionByIdService(String transactionID) throws InvalidTransactionIDException {
		Transaction transaction = null;
		// TODO : Helper function: Add better transactionID check
		if (transactionID != null) {
			transaction = iTransactionDAO.getTransactionById(transactionID);
		} else {
			throw new InvalidTransactionIDException("ID doesn't match transaction ID semantics.");
		}
		return transaction;
	}

	@Override
	public List<Transaction> getTransactionByTypeService(String type) throws InvalidTransactionTypeException {
		List<Transaction> transactionsByType = null;
		if (type != null && (type.equals("upi") || type.equals("bank"))) {
			transactionsByType = iTransactionDAO.getTransactionByType(type);
		} else {
			throw new InvalidTransactionTypeException("Transaction type can be \"upi\" or \"bank\" only.");
		}
		return transactionsByType;
	}

	@Override
	public List<Transaction> getTransactionByStatusService(String status) throws InvalidTransactionStatusException {
		List<Transaction> transactionsByStatus = null;
		if (status != null && (status.equals("initiated") || status.equals("pending") || status.equals("completed")
				|| status.equals("failed"))) {
			transactionsByStatus = iTransactionDAO.getTransactionByStatus(status);
		} else {
			throw new InvalidTransactionStatusException(
					"Invalid transaction status. Status can be either \"initiated\", \"pending\", \"completed\" or \"pending\"");
		}
		return transactionsByStatus;
	}

	@Override
	public List<Transaction> getTransactionByDateService(Date date) throws InvalidDateFormatException {
		List<Transaction> transactionsByDate = null;
		// TODO: Helper Function : Write helper method for date format check.
		if (date != null) {
			transactionsByDate = iTransactionDAO.getTransactionByDate(date);
		} else {
			throw new InvalidDateFormatException("Invalid date format");
		}
		return transactionsByDate;
	}

	@Override
	public List<Transaction> getTransactionByDateRangeService(Date startDate, Date endDate)
			throws InvalidDateFormatException, InvalidRangeException {
		// TODO: Helper Function : Write helper method for date format check.
		List<Transaction> transactionsByDateRange = null;
		if (startDate.compareTo(endDate) > 0) {
			throw new InvalidRangeException("Start should be smaller or equal to end.");
		} else {
			transactionsByDateRange = iTransactionDAO.getTransactionByDateRange(startDate, endDate);
		}
		return transactionsByDateRange;
	}

	@Override
	public List<Transaction> getTransactionByAmountRangeService(Double startAmount, Double endAmount)
			throws InvalidRangeException {
		List<Transaction> transactionsByAmountRange = null;
		if (startAmount <= endAmount) {
			transactionsByAmountRange = iTransactionDAO.getTransactionByAmountRange(startAmount, endAmount);
		} else {
			throw new InvalidRangeException("Start should be smaller or equal to end.");
		}
		return transactionsByAmountRange;
	}

	@Override
	public Transaction createTransactionService(Transaction transaction) throws InvalidTransactionObjectException {
		Transaction newTransaction = null;
		if (transaction != null) {
			newTransaction = iTransactionDAO.createTransaction(transaction);
		} else {
			throw new InvalidTransactionObjectException("Invalid Transaction object. Ensure fields are correct");
		}
		return newTransaction;
	}

	@Override
	public boolean deleteTransactionService(String transactionID) throws InvalidTransactionIDException {
		boolean status = false;
		if (transactionID != null) {
			status = iTransactionDAO.deleteTransaction(transactionID);
		} else {
			throw new InvalidTransactionIDException("ID doesn't match transaction ID sematics");
		}
		return status;
	}

	@Override
	public Transaction updateTransactionService(Transaction transaction) throws InvalidTransactionObjectException {

		if (transaction == null) {
			throw new InvalidTransactionObjectException(
					"Invalid Transaction object. Cannot update a null transaction.");
		}

		Transaction updatedTransaction = iTransactionDAO.updateTransaction(transaction);

		if (updatedTransaction == null) {
			throw new InvalidTransactionObjectException("Transaction with ID '" + transaction.getTransactionId()
					+ "' not found for update, or update failed.");
		}

		return updatedTransaction;
	}

}
