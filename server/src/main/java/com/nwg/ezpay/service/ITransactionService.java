package com.nwg.ezpay.service;

import java.util.List;
import java.util.Date;

import com.nwg.ezpay.entity.Transaction;

/**
 * This interface contains method declaration for the Transaction Service
 * 
 * @author Sourav Behera
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

public interface ITransactionService {

	/**
	 * Returns {@code Transaction} if a transaction with the supplied ID is found.
	 * This method performs validation on the provided ID.
	 *
	 * @param transactionId : UID of the transaction
	 * @return {@code Transaction} : The transaction object if found, otherwise {@code null}.
	 * @throws InvalidTransactionIDException if the provided transaction ID is null or invalid.
	 */
	Transaction getTransactionByIdService(String transactionId) throws InvalidTransactionIDException;

	/**
	 * Returns a {@code List<Transaction>} containing all transactions that match the
	 * provided type. This method validates the transaction type.
	 *
	 * @param type : Type of transaction (e.g., "upi" or "bank")
	 * @return {@code List<Transaction>} : A list of transactions belonging to the specified type.
	 * @throws InvalidTransactionTypeException if the provided type is null or not a valid type.
	 */
	List<Transaction> getTransactionByTypeService(String type) throws InvalidTransactionTypeException;

	/**
	 * Returns a {@code List<Transaction>} containing all transactions that have the
	 * provided status. This method validates the transaction status.
	 *
	 * @param status : Status of the transaction (e.g., "initiated", "pending", "completed", "failed")
	 * @return {@code List<Transaction>} : A list of transactions with the specified status.
	 * @throws InvalidTransactionStatusException if the provided status is null or not a valid status.
	 */
	List<Transaction> getTransactionByStatusService(String status) throws InvalidTransactionStatusException;

	/**
	 * Returns a {@code List<Transaction>} containing all transactions that occurred on
	 * the specified date. This method validates the date object.
	 *
	 * @param date : Date of the transaction
	 * @return {@code List<Transaction>} : A list of transactions performed on the specified date.
	 * @throws InvalidDateFormatException if the provided date object is null.
	 */
	List<Transaction> getTransactionByDateService(Date date) throws InvalidDateFormatException;

	/**
	 * Returns a {@code List<Transaction>} containing all transactions that occurred within the
	 * specified date range. This method validates both the date objects and the range.
	 *
	 * @param startDate : The start date for the transaction range.
	 * @param endDate   : The end date for the transaction range.
	 * @return {@code List<Transaction>} : A list of transactions within the specified date range.
	 * @throws InvalidDateFormatException if either the start or end date is null.
	 * @throws InvalidRangeException if the start date is after the end date.
	 */
	List<Transaction> getTransactionByDateRangeService(Date startDate, Date endDate) throws InvalidDateFormatException, InvalidRangeException;

	/**
	 * Returns a {@code List<Transaction>} containing all transactions whose amount is
	 * within the specified range. This method validates the range.
	 *
	 * @param startAmount : The starting amount for the transaction range.
	 * @param endAmount   : The ending amount for the transaction range.
	 * @return {@code List<Transaction>} : A list of transactions within the specified amount range.
	 * @throws InvalidRangeException if either the start or end amount is null, or if the start amount is greater than the end amount.
	 */
	List<Transaction> getTransactionByAmountRangeService(Double startAmount, Double endAmount) throws InvalidRangeException;

	/**
	 * Creates a new transaction and returns the created object. This method validates
	 * the provided transaction object before attempting to create it.
	 *
	 * @param transaction : The {@code Transaction} object to be created.
	 * @return {@code Transaction} : The newly created transaction object.
	 * @throws InvalidTransactionObjectException if the provided transaction object is null.
	 */
	Transaction createTransactionService(Transaction transaction) throws InvalidTransactionObjectException;

	/**
	 * Deletes an existing transaction from the system. This method validates the
	 * transaction ID.
	 *
	 * @param transactionId : The UID of the transaction to delete.
	 * @return {@code boolean} : {@code true} if the transaction was successfully deleted, otherwise {@code false}.
	 * @throws InvalidTransactionIDException if the provided transaction ID is null or invalid.
	 */
	boolean deleteTransactionService(String transactionId) throws InvalidTransactionIDException;

	/**
	 * Updates an existing transaction with the data from the provided object. This method
	 * validates the transaction object and ensures the transaction exists.
	 *
	 * @param transaction : The {@code Transaction} object containing the updated data.
	 * @return {@code Transaction} : The updated transaction object.
	 * @throws InvalidTransactionObjectException if the transaction object is null, or if a transaction with the given ID cannot be found.
	 */
	Transaction updateTransactionService(Transaction transaction) throws InvalidTransactionObjectException;
}
