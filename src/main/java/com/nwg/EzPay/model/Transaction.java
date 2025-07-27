package com.nwg.EzPay.model;

import java.util.Date;

/**
 * This class {@code Transaction} represents a transaction statement which is
 * generated for every payment/bank transfer
 * 
 * 
 * @author : Sourav Behera
 * @version : 0.0.1
 * @date : 27-07-2025
 * 
 */

public class Transaction {
	private String transactionId;
	private String type;
	private Double amount;
	private String status;
	private Date date;

	public Transaction() {
	}

	/**
	 * Allocates a {@code Transaction} object and initializes it so that it
	 * represents a valid transaction.
	 * 
	 * @param transactionId: the UID of the transaction
	 * @param type           : the type of transaction (UPI, Bank Transfer)
	 * @param amount         : the amount transferred
	 * @param status         : the transaction status (initiated, pending, :
	 *                       completed/failed)
	 * @param date           : the date and time of the transaction. Format :
	 *                       "yyyy-MM-dd HH:mm:ss"
	 */

	public Transaction(String transactionId, String type, Double amount, String status, Date date) {
		super();
		this.transactionId = transactionId;
		this.type = type;
		this.amount = amount;
		this.status = status;
		this.date = date;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void reviewTransaction() {

	}

	@Override
	public String toString() {
		return String.format("%s, %s, %.2f, %s", transactionId, type, amount, status);
	}
}
