package com.nwg.EzPay.dao;

import java.util.List;
import java.util.Date;

import com.nwg.EzPay.model.Transaction;

/**
 * This interface contains method declaration for the Transaction DAO
 * 
 * @author Sourav Behera
 * @version 0.0.1
 */

public interface ITransactionDAO {

	/**
	 * Returns {@code Transaction} if transaction with the supplied transactionId is
	 * present in the database.
	 * 
	 * @param transactionId : UID of the transaction
	 * @return {@code Transaction}
	 */
	Transaction getTransactionById(String transactionId);

	/**
	 * Returns {@code List<Transaction>} a list of transactions that belong to the
	 * type.
	 * 
	 * @param type : Type of transaction (upi/bank)
	 * @return {@code List<Transaction>} : A list of transactions belonging to the
	 *         type
	 */
	List<Transaction> getTransactionByType(String type);

	/**
	 * Returns {@code List<Transaction>} a list of transactions that have the
	 * provided status.
	 * 
	 * @param status : Status of the transaction(initiated, pending, completed)
	 * @return {@code List<Transaction>} : List of transactions that have the status
	 */
	List<Transaction> getTransactionByStatus(String status);

	/**
	 * Returns {@code List<Transaction>} a list of transactions that have the
	 * provided date.
	 * 
	 * @param date : Date of the transaction. Format ("yyyy-MM-dd")
	 * @return {@code List<Transaction>} : List of transactions performed on the
	 *         specified date.
	 */
	List<Transaction> getTransactionByDate(Date date);

	/**
	 * Returns {@code List<Transaction>} a list of transactions performed within the
	 * specified date range.
	 * 
	 * @param startDate : Start date for the transactions. Format ("yyyy-MM-dd")
	 * @param endDate   : End date for the transactions. Format ("yyyy-MM-dd")
	 * @return {@code List<Transaction>} : List of transactions performed on the
	 *         specified date range.
	 */
	List<Transaction> getTransactionByDateRange(Date startDate, Date endDate);

	/**
	 * Returns {@code List<Transaction>} a list of transactions whose amount lie
	 * between the specified range.
	 * 
	 * @param startAmoutn : Start amount for the transactions.
	 * @param endAmount   : End amount for the transactions.
	 * @return {@code List<Transaction>} : List of transactions whose amount lie in
	 *         the specified range.
	 */
	List<Transaction> getTransactionByAmountRange(Double startAmount, Double endAmount);

	// CRUD Operation for single transactions
	Transaction createTransaction(Transaction transaction);

	boolean deleteTransaction(String transactionId);

	Transaction updateTransaction(Transaction transaction);
}
