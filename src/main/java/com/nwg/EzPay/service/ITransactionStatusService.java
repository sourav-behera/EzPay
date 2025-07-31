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

    TransactionStatus getStatusByIdService(String transactionStatusId) throws InvalidTransactionStatusIdException;

    List<TransactionStatus> getStatusesByTypeService(String statusType) throws InvalidTransactionStatusException;

    List<TransactionStatus> getStatusesByReasonService(String reason) throws InvalidTransactionStatusException;

    List<TransactionStatus> getStatusesByDateService(Date date) throws InvalidDateFormatException;

    List<TransactionStatus> getStatusesByDateRangeService(Date startDate, Date endDate) 
        throws InvalidDateFormatException, InvalidRangeException;

    TransactionStatus createStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException;

    boolean deleteStatusService(String transactionStatusId) throws InvalidTransactionStatusIdException;

    TransactionStatus updateStatusService(TransactionStatus transactionStatus) throws InvalidTransactionStatusObjectException;
}
