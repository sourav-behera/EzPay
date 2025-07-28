package com.nwg.EzPay.dao;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.nwg.EzPay.model.Transaction;

public class TransactionDAOImpl implements ITransactionDAO {

	public static List<Transaction> transactions = new ArrayList<Transaction>();
	static {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("data/transactions.csv");
			br = new BufferedReader(fr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String transactionDetailCSV = "";
			while ((transactionDetailCSV = br.readLine()) != null) {
				String details[] = transactionDetailCSV.split(",");
				String transactionId = details[0];
				String type = details[1];
				Double amount = Double.parseDouble(details[2]);
				String status = details[3];
				Date date = sdf.parse(details[4]);
				Transaction transaction = new Transaction(transactionId, type, amount, status, date);
				transactions.add(transaction);
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
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Transaction getTransactionById(String transactionId) {
		for (Transaction transaction : transactions) {
			if (transaction.getTransactionId().equals(transactionId)) {
				return transaction;
			}
		}
		return null;
	}

	@Override
	public List<Transaction> getTransactionByType(String type) {
		List<Transaction> transactionsByType = new ArrayList<Transaction>();
		for (Transaction transaction : transactions) {
			if (transaction.getType().equals(type)) {
				transactionsByType.add(transaction);
			}
		}
		return transactionsByType;
	}

	@Override
	public List<Transaction> getTransactionByStatus(String status) {
		List<Transaction> transactionsByStatus = new ArrayList<Transaction>();
		for (Transaction transaction : transactions) {
			if (transaction.getStatus().equals(status)) {
				transactionsByStatus.add(transaction);
			}
		}
		return transactionsByStatus;
	}

	@Override
	public List<Transaction> getTransactionByDate(Date date) {
		List<Transaction> transactionsByDate = new ArrayList<Transaction>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (Transaction transaction : transactions) {
				Date transactionDate = transaction.getDate();
				transactionDate = sdf.parse(sdf.format(transactionDate));
				if (transactionDate.equals(date)) {
					transactionsByDate.add(transaction);
				}
			}
			return transactionsByDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Transaction> getTransactionByDateRange(Date startDate, Date endDate) {
		List<Transaction> transactionsByDateRange = new ArrayList<Transaction>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (Transaction transaction : transactions) {
				Date transactionDate = transaction.getDate();
				transactionDate = sdf.parse(sdf.format(transactionDate));
				if (transactionDate.compareTo(startDate) >= 0 && transactionDate.compareTo(endDate) <= 0) {
					transactionsByDateRange.add(transaction);
				}
			}
			return transactionsByDateRange;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Transaction> getTransactionByAmountRange(Double startAmount, Double endAmount) {
		List<Transaction> transactionsByAmountRange = new ArrayList<Transaction>();
		for (Transaction transaction : transactions) {
			Double transactionAmount = transaction.getAmount();
			if (startAmount <= transactionAmount && transactionAmount <= endAmount) {
				transactionsByAmountRange.add(transaction);
			}
		}
		return transactionsByAmountRange;
	}

	@Override
	public Transaction createTransaction(Transaction transaction) {
	    if (transactions.add(transaction)) {
	        // Get the last element of the list, which is the one just added
	        return transactions.get(transactions.size() - 1);
	    }
	    return null;
	}

	@Override
	public boolean deleteTransaction(String transactionId) {
		// removeIf() is a safer and concise way to remove items from list.
		return transactions.removeIf(transaction -> transaction.getTransactionId().equals(transactionId));
	}

	@Override
	public Transaction updateTransaction(Transaction transaction) {
        if (transaction == null) {
            return null; 
        }
		for (int i = 0; i < transactions.size(); i++) {
			Transaction tr = transactions.get(i);
			if (tr != null && tr.getTransactionId().equals(transaction.getTransactionId())) {
				transactions.set(i, transaction);
				return transactions.get(i);
			}
		}
		return null;	
	}
}
