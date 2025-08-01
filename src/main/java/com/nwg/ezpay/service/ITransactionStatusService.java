package com.nwg.ezpay.service;

import java.util.Date;
import java.util.List;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionStatusIdException;
import com.nwg.ezpay.exception.InvalidTransactionStatusObjectException;
import com.nwg.ezpay.model.TransactionStatus;

public interface ITransactionStatusService {
	
    /**
     * Fetches a transaction status using its unique ID.
     *
     * @param transactionStatusId unique identifier of the transaction status
     * @return corresponding {@link TransactionStatus} object
     * @throws InvalidTransactionStatusIdException if the ID is null or invalid
     */
    TransactionStatus getStatusByIdService(String transactionStatusId) throws InvalidTransactionStatusIdException;

    /**
     * Retrieves a list of transaction statuses filtered by type.
     *
     * @param statusType the type to filter by (e.g., completed, pending)
     * @return list of matching {@link TransactionStatus} objects
     * @throws InvalidTransactionStatusException if the status type is null or invalid
     */
    List<TransactionStatus> getStatusesByTypeService(String statusType) throws InvalidTransactionStatusException;

    /**
     * Retrieves a list of transaction statuses based on the given reason.
     *
     * @param reason status description or reason
     * @return list of matching {@link TransactionStatus} objects
     * @throws InvalidTransactionStatusException if the reason is null or invalid
     */
    List<TransactionStatus> getStatusesByReasonService(String reason) throws InvalidTransactionStatusException;

    /**
     * Retrieves all transaction statuses created on a specific date.
     *
     * @param date date to filter by
     * @return list of matching {@link TransactionStatus} objects
     * @throws InvalidDateFormatException if the date is null
     */
    List<TransactionStatus> getStatusesByDateService(Date date) throws InvalidDateFormatException;

    /**
     * Retrieves transaction statuses within a specified date range.
     *
     * @param startDate starting date
     * @param endDate ending date
     * @return list of {@link TransactionStatus} within the range
     * @throws InvalidDateFormatException if either date is null
     * @throws InvalidRangeException if start date is after end date
     */
    List<TransactionStatus> getStatusesByDateRangeService(Date startDate, Date endDate) 
        throws InvalidDateFormatException, InvalidRangeException;

    /**
     * Creates a new transaction status entry.
     *
     * @param transactionStatus the object to be saved
     * @return the newly created {@link TransactionStatus}
     * @throws InvalidTransactionStatusObjectException if the object is null or invalid
     */
    TransactionStatus createStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException;
   
    /**
     * Deletes a transaction status by its ID.
     *
     * @param transactionStatusId ID of the status to be deleted
     * @return {@code true} if deletion is successful
     * @throws InvalidTransactionStatusIdException if the ID is null or invalid
     */
    boolean deleteStatusService(String transactionStatusId) throws InvalidTransactionStatusIdException;
    
    /**
     * Updates an existing transaction status.
     *
     * @param transactionStatus the updated status object
     * @return updated {@link TransactionStatus}
     * @throws InvalidTransactionStatusObjectException if the object is null or invalid
     */
    TransactionStatus updateStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException;
}
