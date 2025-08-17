package com.nwg.ezpay.service;

import java.util.Date;
import java.util.List;

/**
 * This class contains the implementation for the Transaction Status Service {@link ITransactionService} interface.
 * 
 * @author Sourav Behera
 * @author Shiksha Nayan
 * @version 0.0.1
 * @since 2025-07-28
 * @revised 2025-08-01
 */

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.repository.ITransactionDAO;
import com.nwg.ezpay.repository.TransactionDAOImpl;
import com.nwg.ezpay.entity.Transaction;

public class TransactionServiceImpl implements ITransactionService {

	ITransactionDAO iTransactionDAO;

	/**
	 * Constructor for the Transaction Service. It initializes a new
	 * {@link TransactionDAOImpl} to establish a connection to the data layer.
	 */
	public TransactionServiceImpl() {
		iTransactionDAO = new TransactionDAOImpl();
	}

	
	
	/**
	 * Returns a {@code Transaction} if a transaction with the supplied ID is found.
	 * This method validates the provided transaction ID before querying the DAO.
	 *
	 * @param transactionID : UID of the transaction
	 * @return {@code Transaction} : The transaction object if found, otherwise {@code null}.
	 * @throws InvalidTransactionIDException if the provided transaction ID is {@code null}.
	 */
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

	
	
	/**
	 * Returns a {@code List<Transaction>} containing all transactions that match the
	 * provided type. This method validates that the transaction type is one of the
	 * allowed types ("upi" or "bank").
	 *
	 * @param type : Type of transaction (e.g., "upi" or "bank")
	 * @return {@code List<Transaction>} : A list of transactions belonging to the specified type.
	 * @throws InvalidTransactionTypeException if the provided type is {@code null} or not a valid type.
	 */
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
	
	
	/**
	 * Returns a {@code List<Transaction>} containing all transactions that have the
	 * provided status. This method validates that the transaction status is one of the
	 * allowed statuses.
	 *
	 * @param status : Status of the transaction (e.g., "initiated", "pending", "completed", "failed")
	 * @return {@code List<Transaction>} : A list of transactions with the specified status.
	 * @throws InvalidTransactionStatusException if the provided status is {@code null} or not a valid status.
	 */
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
	
	
	
	/**
	 * Returns a {@code List<Transaction>} containing all transactions that occurred on
	 * the specified date. This method validates the provided date object.
	 *
	 * @param date : Date of the transaction
	 * @return {@code List<Transaction>} : A list of transactions performed on the specified date.
	 * @throws InvalidDateFormatException if the provided date object is {@code null}.
	 */
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

	
	/**
	 * Returns a {@code List<Transaction>} containing all transactions that occurred within the
	 * specified date range. This method validates both the date objects and the range.
	 *
	 * @param startDate : The start date for the transaction range.
	 * @param endDate   : The end date for the transaction range.
	 * @return {@code List<Transaction>} : A list of transactions within the specified date range.
	 * @throws InvalidDateFormatException if either the start or end date is {@code null}.
	 * @throws InvalidRangeException if the start date is after the end date.
	 */
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

	
	
	/**
	 * Returns a {@code List<Transaction>} containing all transactions whose amount is
	 * within the specified range. This method validates the amount range.
	 *
	 * @param startAmount : The starting amount for the transaction range.
	 * @param endAmount   : The ending amount for the transaction range.
	 * @return {@code List<Transaction>} : A list of transactions within the specified amount range.
	 * @throws InvalidRangeException if the start amount is greater than the end amount.
	 */
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

	
	
	/**
	 * Creates a new transaction and returns the created object. This method validates
	 * that the provided transaction object is not {@code null}.
	 *
	 * @param transaction : The {@code Transaction} object to be created.
	 * @return {@code Transaction} : The newly created transaction object.
	 * @throws InvalidTransactionObjectException if the provided transaction object is {@code null}.
	 */
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
	
	

	/**
	 * Deletes an existing transaction from the system. This method validates the
	 * transaction ID.
	 *
	 * @param transactionID : The UID of the transaction to delete.
	 * @return {@code boolean} : {@code true} if the transaction was successfully deleted, otherwise {@code false}.
	 * @throws InvalidTransactionIDException if the provided transaction ID is {@code null}.
	 */
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

	
	
	/**
	 * Updates an existing transaction with the data from the provided object. This method
	 * validates the transaction object and ensures the transaction exists.
	 *
	 * @param transaction : The {@code Transaction} object containing the updated data.
	 * @return {@code Transaction} : The updated transaction object.
	 * @throws InvalidTransactionObjectException if the transaction object is {@code null} or if a transaction with the given ID cannot be found.
	 */
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