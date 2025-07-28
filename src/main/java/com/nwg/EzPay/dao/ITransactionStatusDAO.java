package com.nwg.EzPay.dao;

import java.util.List;
import java.util.Date;
import com.nwg.EzPay.model.TransactionStatus;

/**
 * This interface contains method declarations for the TransactionStatus DAO
 * 
 * @author Palak Deb Patra
 * @version 0.0.1
 */

public interface ITransactionStatusDAO {

    /**
     * Returns the TransactionStatus with the given transactionStatusId if present.
     * 
     * @param transactionStatusId : UID of the transaction status
     * @return {@code TransactionStatus}
     */
    TransactionStatus getStatusById(String transactionStatusId);

    /**
     * Returns a list of TransactionStatus entries with the specified statusType.
     * 
     * @param statusType : status type (e.g., INITIATED, COMPLETED, FAILED)
     * @return {@code List<TransactionStatus>} : list of TransactionStatus with given statusType
     */
    List<TransactionStatus> getStatusesByType(String statusType);

    /**
     * Returns a list of TransactionStatus entries with the specified reason.
     * 
     * @param reason : reason for the status (e.g., "Insufficient funds")
     * @return {@code List<TransactionStatus>}
     */
    List<TransactionStatus> getStatusesByReason(String reason);

    /**
     * Returns a list of TransactionStatus records updated at the given timestamp date.
     * 
     * @param date : the date part of timestamp to filter by (format: "yyyy-MM-dd")
     * @return {@code List<TransactionStatus>} : statuses updated on provided date
     */
    List<TransactionStatus> getStatusesByDate(Date date);

    /**
     * Returns a list of TransactionStatus entries updated within the specified timestamp range.
     * 
     * @param startDate : start date (format: "yyyy-MM-dd")
     * @param endDate   : end date (format: "yyyy-MM-dd")
     * @return {@code List<TransactionStatus>} : statuses updated within the date range
     */
    List<TransactionStatus> getStatusesByDateRange(Date startDate, Date endDate);

    // CRUD Operations

    /**
     * Creates a new TransactionStatus record.
     * 
     * @param transactionStatus : TransactionStatus object to create
     * @return {@code TransactionStatus} : created object with generated fields if any
     */
    TransactionStatus createStatus(TransactionStatus transactionStatus);

    /**
     * Updates an existing TransactionStatus record.
     * 
     * @param transactionStatus : TransactionStatus object with updated info
     * @return {@code TransactionStatus} : the updated object
     */
    TransactionStatus updateStatus(TransactionStatus transactionStatus);

    /**
     * Deletes the TransactionStatus record with the specified transactionStatusId.
     * 
     * @param transactionStatusId : UID of the transaction status to delete
     * @return {@code boolean} : true if deletion was successful, false otherwise
     */
    boolean deleteStatusById(String transactionStatusId);
}
