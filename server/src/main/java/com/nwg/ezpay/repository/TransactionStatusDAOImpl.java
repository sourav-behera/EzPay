package com.nwg.ezpay.repository;

import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nwg.ezpay.entity.TransactionStatus;

/**
 * This class contains the implementation for the transaction status data access object.
 * 
 * @author Palak Deb Patra
 * @version 0.0.1
 */

public class TransactionStatusDAOImpl implements ITransactionStatusDAO {

    public static List<TransactionStatus> transactionStatuses = new ArrayList<>();

    static {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader("data/transaction_statuses.csv");
            bufferedReader = new BufferedReader(fileReader);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 4) {
                    continue; // skip invalid lines
                }
                String transactionStatusId = details[0];
                String statusType = details[1];
                String reason = details[2];
                Date timestamp = simpleDateFormat.parse(details[3]);
                TransactionStatus transactionStatus = new TransactionStatus(transactionStatusId, statusType, reason, timestamp);
                transactionStatuses.add(transactionStatus);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a transaction status by its ID.
     *
     * @param transactionStatusId unique ID of the status
     * @return matching {@link TransactionStatus}, or {@code null} if not found
     */
    @Override
    public TransactionStatus getStatusById(String transactionStatusId) {
        for (TransactionStatus transactionStatus : transactionStatuses) {
            if (transactionStatus.getTransactionStatusId().equals(transactionStatusId)) {
                return transactionStatus;
            }
        }
        return null;
    }

    /**
     * Retrieves all transaction statuses matching the provided type.
     *
     * @param statusType status type to filter by
     * @return list of matching {@link TransactionStatus} entries
     */
    @Override
    public List<TransactionStatus> getStatusesByType(String statusType) {
        List<TransactionStatus> list = new ArrayList<>();
        for (TransactionStatus transactionStatus : transactionStatuses) {
            if (transactionStatus.getStatusType().equals(statusType)) {
                list.add(transactionStatus);
            }
        }
        return list;
    }

    /**
     * Retrieves all transaction statuses matching the provided reason.
     *
     * @param reason status reason to filter by
     * @return list of matching {@link TransactionStatus} entries
     */
    @Override
    public List<TransactionStatus> getStatusesByReason(String reason) {
        List<TransactionStatus> list = new ArrayList<>();
        for (TransactionStatus transactionStatus : transactionStatuses) {
            if (transactionStatus.getReason().equals(reason)) {
                list.add(transactionStatus);
            }
        }
        return list;
    }

    /**
     * Retrieves all transaction statuses that match a specific date (ignores time).
     *
     * @param date date to match
     * @return list of {@link TransactionStatus} created on that date
     */
    @Override
    public List<TransactionStatus> getStatusesByDate(Date date) {
        List<TransactionStatus> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date targetDate = simpleDateFormat.parse(simpleDateFormat.format(date));
            for (TransactionStatus transactionStatus : transactionStatuses) {
                Date tsDate = simpleDateFormat.parse(simpleDateFormat.format(transactionStatus.getTimestamp()));
                if (tsDate.equals(targetDate)) {
                    list.add(transactionStatus);
                }
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves all transaction statuses within a date range (inclusive).
     *
     * @param startDate start of the range
     * @param endDate end of the range
     * @return list of matching {@link TransactionStatus} entries
     */
    @Override
    public List<TransactionStatus> getStatusesByDateRange(Date startDate, Date endDate) {
        List<TransactionStatus> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDateNormalized = simpleDateFormat.parse(simpleDateFormat.format(startDate));
            Date endDateNormalized = simpleDateFormat.parse(simpleDateFormat.format(endDate));
            for (TransactionStatus transactionStatus : transactionStatuses) {
                Date tsDate = simpleDateFormat.parse(simpleDateFormat.format(transactionStatus.getTimestamp()));
                if (tsDate.compareTo(startDateNormalized) >= 0 && tsDate.compareTo(endDateNormalized) <= 0) {
                    list.add(transactionStatus);
                }
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Adds a new transaction status to the in-memory list.
     *
     * @param transactionStatus new status object
     * @return created {@link TransactionStatus} if successful, {@code null} otherwise
     */
    @Override
    public TransactionStatus createStatus(TransactionStatus transactionStatus) {
    	return transactionStatuses.get(transactionStatuses.size() - 1);
    }
    
    /**
     * Updates an existing transaction status.
     * Matches using the transactionStatusId field.
     *
     * @param transactionStatus updated status object
     * @return updated {@link TransactionStatus} if successful, {@code null} otherwise
     */
    @Override
    public TransactionStatus updateStatus(TransactionStatus transactionStatus) {
        for (int i = 0; i < transactionStatuses.size(); i++) {
            TransactionStatus existingTransactionStatus = transactionStatuses.get(i);
            if (existingTransactionStatus.getTransactionStatusId().equals(transactionStatus.getTransactionStatusId())) {
                transactionStatuses.set(i, transactionStatus);
                return transactionStatuses.get(i);
            }
        }
        return null;
    }
    
    /**
     * Deletes a transaction status by ID.
     *
     * @param transactionStatusId unique ID of the status to delete
     * @return {@code true} if deletion was successful, {@code false} otherwise
     */
    @Override
    public boolean deleteStatusById(String transactionStatusId) {
        return transactionStatuses.removeIf(ts -> ts.getTransactionStatusId().equals(transactionStatusId));
    }
}
