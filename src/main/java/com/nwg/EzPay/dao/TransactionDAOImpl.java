package com.nwg.EzPay.dao;

import java.util.List;
import java.awt.font.TransformAttribute;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLNonTransientConnectionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
	public List<Transaction> getTranstaionByDate(Date date) {
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
		// TODO Auto-generated method stub
		List<Transaction> transactionsByDateRange = new ArrayList<Transaction>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (Transaction transaction : transactions) {
				Date transactionDate = transaction.getDate();
				transactionDate = sdf.parse(sdf.format(transactionDate));
				if (transactionDate.compareTo(startDate) <= 0 && transactionDate.compareTo(endDate) >= 0) {
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
	public boolean createTransaction(Transaction transaction) {
		return transactions.add(transaction);
	}

	@Override
	public boolean deleteTransaction(Transaction transaction) {
		boolean status = false;
		for (Transaction tr : transactions) {
			if (tr.getTransactionId().equals(transaction.getTransactionId())) {
				transactions.remove(tr);
				status = true;
				break;
			}
		}
		return status;
	}

	@Override
	public boolean readTransaction(Transaction transaction) {

		return false;
	}

	@Override
	public boolean updateTransaction(Transaction transaction) {
		boolean status = false;
		for (int i = 0; i < transactions.size(); i++) {
			Transaction tr = transactions.get(i);
			if (tr.getTransactionId().equals(transaction.getTransactionId())) {
				transactions.set(i, transaction);
				status = true;
			}
		}
		return status;
	}

}
