package com.nwg.EzPay.service;

import java.util.Date;
import java.util.List;

import com.nwg.EzPay.exception.InvalidDateFormatException;
import com.nwg.EzPay.exception.InvalidRangeException;
import com.nwg.EzPay.exception.InvalidTransactionStatusException;
import com.nwg.EzPay.exception.InvalidTransactionStatusIdException;
import com.nwg.EzPay.exception.InvalidTransactionStatusObjectException;
import com.nwg.EzPay.model.TransactionStatus;

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
