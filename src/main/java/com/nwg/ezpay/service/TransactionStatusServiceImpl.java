package com.nwg.ezpay.service;

import java.util.Date;
import java.util.List;

import com.nwg.ezpay.dao.ITransactionStatusDAO;
import com.nwg.ezpay.dao.TransactionStatusDAOImpl;
import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionStatusIdException;
import com.nwg.ezpay.exception.InvalidTransactionStatusObjectException;
import com.nwg.ezpay.model.TransactionStatus;

/**
 * Service implementation for managing transaction statuses.
 * Provides methods for CRUD operations and various filters.
 * 
 * <p>This class implements the {@link ITransactionStatusService} interface,
 * handling business logic for transaction status management.</p>
 * 
 * @author Palak Deb Patra
 * @version 0.0.1
 */

public class TransactionStatusServiceImpl implements ITransactionStatusService {

    ITransactionStatusDAO iTransactionStatusDAO;

    /**
     * Default constructor. Initializes the DAO layer.
     */
    public TransactionStatusServiceImpl() {
        iTransactionStatusDAO = new TransactionStatusDAOImpl();
    }

    /**
     * Retrieves a transaction by its unique ID.
     *
     * @param transactionID unique transaction identifier
     * @return {@link Transaction} object if found
     * @throws InvalidTransactionIDException if ID is null or invalid
     */
    @Override
    public TransactionStatus getStatusByIdService(String transactionStatusId) throws InvalidTransactionStatusIdException {
        TransactionStatus status = null;
        if (transactionStatusId != null && !transactionStatusId.trim().isEmpty()) {
            status = iTransactionStatusDAO.getStatusById(transactionStatusId);
            if (status == null) {
                throw new InvalidTransactionStatusIdException("TransactionStatus not found for ID: " + transactionStatusId);
            }
        } else {
            throw new InvalidTransactionStatusIdException("ID doesn't match transactionStatus ID semantics.");
        }
        return status;
    }

    /**
     * Fetches all transactions of a specified type.
     *
     * @param type transaction type (e.g., "upi", "bank")
     * @return list of matching transactions
     * @throws InvalidTransactionTypeException if type is null or not one of the allowed values
     */
    @Override
    public List<TransactionStatus> getStatusesByTypeService(String statusType) throws InvalidTransactionStatusException {
        List<TransactionStatus> statusesByType = null;
        if (statusType != null && !statusType.trim().isEmpty()) {
            statusesByType = iTransactionStatusDAO.getStatusesByType(statusType);
        } else {
            throw new InvalidTransactionStatusException("Transaction status type cannot be null or empty.");
        }
        return statusesByType;
    }
   
    /**
     * Fetches all transactions that match a given status.
     *
     * @param status status value (e.g., "initiated", "pending", "completed", "failed")
     * @return list of matching transactions
     * @throws InvalidTransactionStatusException if status is null or invalid
     */
    @Override
    public List<TransactionStatus> getStatusesByReasonService(String reason) throws InvalidTransactionStatusException {
        List<TransactionStatus> statusesByReason = null;
        if (reason != null && !reason.trim().isEmpty()) {
            statusesByReason = iTransactionStatusDAO.getStatusesByReason(reason);
        } else {
            throw new InvalidTransactionStatusException("Reason cannot be null or empty.");
        }
        return statusesByReason;
    }

    /**
     * Retrieves all transactions that occurred on a specific date.
     *
     * @param date the date to match
     * @return list of transactions on that date
     * @throws InvalidDateFormatException if the date is null
     */
    @Override
    public List<TransactionStatus> getStatusesByDateService(Date date) throws InvalidDateFormatException {
        List<TransactionStatus> statusesByDate = null;
        if (date != null) {
            statusesByDate = iTransactionStatusDAO.getStatusesByDate(date);
        } else {
            throw new InvalidDateFormatException("Invalid date format.");
        }
        return statusesByDate;
    }

    /**
     * Retrieves transactions that fall within the specified date range.
     *
     * @param startDate beginning of the range
     * @param endDate end of the range
     * @return list of matching transactions
     * @throws InvalidDateFormatException if either date is null
     * @throws InvalidRangeException if startDate is after endDate
     */
    @Override
    public List<TransactionStatus> getStatusesByDateRangeService(Date startDate, Date endDate)
            throws InvalidDateFormatException, InvalidRangeException {
        List<TransactionStatus> statusesByDateRange = null;
        if (startDate == null || endDate == null) {
            throw new InvalidDateFormatException("Start date and end date cannot be null.");
        }
        if (startDate.compareTo(endDate) > 0) {
            throw new InvalidRangeException("Start date should be smaller or equal to end date.");
        }
        statusesByDateRange = iTransactionStatusDAO.getStatusesByDateRange(startDate, endDate);
        return statusesByDateRange;
    }

    /**
     * Creates a new transaction status entry.
     *
     * @param transaction transaction object to be persisted
     * @return the newly created transaction
     * @throws InvalidTransactionObjectException if the object is null or invalid
     */
    @Override
    public TransactionStatus createStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException {
        TransactionStatus newStatus = null;
        if (transactionStatus != null) {
            newStatus = iTransactionStatusDAO.createStatus(transactionStatus);
        } else {
            throw new InvalidTransactionStatusObjectException("Invalid TransactionStatus object. Ensure fields are correct.");
        }
        return newStatus;
    }

    /**
     * Deletes a transaction status using its ID.
     *
     * @param transactionID ID of the transaction to delete
     * @return true if deletion is successful
     * @throws InvalidTransactionIDException if ID is null
     */
    @Override
    public boolean deleteStatusService(String transactionStatusId) throws InvalidTransactionStatusIdException {
        boolean status = false;
        if (transactionStatusId != null && !transactionStatusId.trim().isEmpty()) {
            status = iTransactionStatusDAO.deleteStatusById(transactionStatusId);
            if (!status) {
                throw new InvalidTransactionStatusIdException(
                        "ID doesn't match any existing TransactionStatus or delete failed.");
            }
        } else {
            throw new InvalidTransactionStatusIdException("ID doesn't match transactionStatus ID semantics.");
        }
        return status;
    }
    
    /**
     * Updates an existing transaction status.
     *
     * @param transaction updated transaction object
     * @return updated transaction from the DB
     * @throws InvalidTransactionObjectException if object is null or update fails
     */
    @Override
    public TransactionStatus updateStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException {
        TransactionStatus updatedStatus = null;
        if (transactionStatus != null) {
            updatedStatus = iTransactionStatusDAO.updateStatus(transactionStatus);
        } else {
            throw new InvalidTransactionStatusObjectException("Invalid TransactionStatus object. Ensure fields are correct.");
        }
        return updatedStatus;
    }
}
