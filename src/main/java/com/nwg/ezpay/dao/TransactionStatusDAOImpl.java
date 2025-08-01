package com.nwg.ezpay.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nwg.ezpay.model.TransactionStatus;

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

    @Override
    public TransactionStatus getStatusById(String transactionStatusId) {
        for (TransactionStatus transactionStatus : transactionStatuses) {
            if (transactionStatus.getTransactionStatusId().equals(transactionStatusId)) {
                return transactionStatus;
            }
        }
        return null;
    }

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

    @Override
    public List<TransactionStatus> getStatusesByDateRange(Date startDate, Date endDate) {
        List<TransactionStatus> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sDate = simpleDateFormat.parse(simpleDateFormat.format(startDate));
            Date eDate = simpleDateFormat.parse(simpleDateFormat.format(endDate));
            for (TransactionStatus transactionStatus : transactionStatuses) {
                Date tsDate = simpleDateFormat.parse(simpleDateFormat.format(transactionStatus.getTimestamp()));
                if (tsDate.compareTo(sDate) >= 0 && tsDate.compareTo(eDate) <= 0) {
                    list.add(transactionStatus);
                }
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TransactionStatus createStatus(TransactionStatus transactionStatus) {
        if (transactionStatuses.add(transactionStatus)) {
            return transactionStatuses.get(transactionStatuses.size() - 1);
        }
        return null;
    }

    @Override
    public TransactionStatus updateStatus(TransactionStatus transactionStatus) {
        for (int i = 0; i < transactionStatuses.size(); i++) {
            TransactionStatus tranStatus = transactionStatuses.get(i);
            if (tranStatus.getTransactionStatusId().equals(transactionStatus.getTransactionStatusId())) {
                transactionStatuses.set(i, transactionStatus);
                return transactionStatuses.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean deleteStatusById(String transactionStatusId) {
        return transactionStatuses.removeIf(ts -> ts.getTransactionStatusId().equals(transactionStatusId));
    }
}
