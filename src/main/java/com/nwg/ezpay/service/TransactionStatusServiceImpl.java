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

public class TransactionStatusServiceImpl implements ITransactionStatusService {

    ITransactionStatusDAO iTransactionStatusDAO;

    public TransactionStatusServiceImpl() {
        iTransactionStatusDAO = new TransactionStatusDAOImpl();
    }

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
