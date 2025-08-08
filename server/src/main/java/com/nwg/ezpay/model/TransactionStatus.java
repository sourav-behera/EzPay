package com.nwg.ezpay.model;

//import java.io.Serializable;
import java.util.Date;

public class TransactionStatus{
	/**
	 * This class {@code TransactionStatus} represents the status of a transaction,
	 * including the status type, reason, and timestamp of when the status was updated.
	 * 
	 * 
	 * @author : Palak Deb Patra
	 * @version : 0.0.1
	 * 
	 */
	
	private String transactionStatusId;
	private String statusType;
	private String reason;
	private Date timestamp;
	
	// Default Constructor
	public TransactionStatus() {};
	
	// Parameterized Constructor
	public TransactionStatus(String transactionStatusId,String statusType, String reason, Date timestamp) {
		super();
		this.transactionStatusId = transactionStatusId;
		this.statusType = statusType;
		this.reason = reason;
		this.timestamp = timestamp;
	}
	
	//Getters and Setters
	public String getTransactionStatusId() {
		return transactionStatusId;
	}
	public void setTransactionStatusId(String transactionStatusId) {
		this.transactionStatusId = transactionStatusId;
	}
	
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s, %s", transactionStatusId, statusType, reason, timestamp);
	}
}
