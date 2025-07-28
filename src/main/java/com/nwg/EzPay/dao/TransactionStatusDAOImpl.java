package com.nwg.EzPay.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nwg.EzPay.model.TransactionStatus;

public class TransactionStatusDAOImpl implements ITransactionStatusDAO {

    public static List<TransactionStatus> transactionStatuses = new ArrayList<>();

    static {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("data/transaction_statuses.csv");
            br = new BufferedReader(fr);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 4) {
                    continue; // skip invalid lines
                }
                String transactionStatusId = details[0];
                String statusType = details[1];
                String reason = details[2];
                Date timestamp = sdf.parse(details[3]);
                TransactionStatus ts = new TransactionStatus(transactionStatusId, statusType, reason, timestamp);
                transactionStatuses.add(ts);
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
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TransactionStatus getStatusById(String transactionStatusId) {
        for (TransactionStatus ts : transactionStatuses) {
            if (ts.getTransactionId().equals(transactionStatusId)) {
                return ts;
            }
        }
        return null;
    }

    @Override
    public List<TransactionStatus> getStatusesByType(String statusType) {
        List<TransactionStatus> list = new ArrayList<>();
        for (TransactionStatus ts : transactionStatuses) {
            if (ts.getStatusType().equals(statusType)) {
                list.add(ts);
            }
        }
        return list;
    }

    @Override
    public List<TransactionStatus> getStatusesByReason(String reason) {
        List<TransactionStatus> list = new ArrayList<>();
        for (TransactionStatus ts : transactionStatuses) {
            if (ts.getReason().equals(reason)) {
                list.add(ts);
            }
        }
        return list;
    }

    @Override
    public List<TransactionStatus> getStatusesByDate(Date date) {
        List<TransactionStatus> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date targetDate = sdf.parse(sdf.format(date));
            for (TransactionStatus ts : transactionStatuses) {
                Date tsDate = sdf.parse(sdf.format(ts.getTimestamp()));
                if (tsDate.equals(targetDate)) {
                    list.add(ts);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sDate = sdf.parse(sdf.format(startDate));
            Date eDate = sdf.parse(sdf.format(endDate));
            for (TransactionStatus ts : transactionStatuses) {
                Date tsDate = sdf.parse(sdf.format(ts.getTimestamp()));
                if (tsDate.compareTo(sDate) >= 0 && tsDate.compareTo(eDate) <= 0) {
                    list.add(ts);
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
            TransactionStatus ts = transactionStatuses.get(i);
            if (ts.getTransactionId().equals(transactionStatus.getTransactionId())) {
                transactionStatuses.set(i, transactionStatus);
                return transactionStatuses.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean deleteStatusById(String transactionStatusId) {
        return transactionStatuses.removeIf(ts -> ts.getTransactionId().equals(transactionStatusId));
    }
}
