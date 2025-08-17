package com.nwg.ezpay.repository;

import java.util.List;

import com.nwg.ezpay.entity.Transaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class contains the implementation for the transaction data access object.
 * 
 * @author Sourav Behera
 * @author Shiksha Nayan
 * @version 0.0.1
 * @since 2025-07-28
 * @revised 2025-08-01
 */
public class TransactionDAOImpl implements ITransactionDAO {

	public static List<Transaction> transactionsList = new ArrayList<Transaction>();
	static {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader("data/transactions.csv");
			bufferedReader = new BufferedReader(fileReader);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String transactionDetailCSV = "";
			while ((transactionDetailCSV = bufferedReader.readLine()) != null) {
				String details[] = transactionDetailCSV.split(",");
				String transactionId = details[0];
				String type = details[1];
				Double amount = Double.parseDouble(details[2]);
				String status = details[3];
				Date date = sdf.parse(details[4]);
				Transaction transaction = new Transaction(transactionId, type, amount, status, date);
				transactionsList.add(transaction);
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
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method returns {@code Transaction} with the specified UID.
	 *
	 * @param transactionId : UID of the transaction
	 * @return {@code Transaction} if transaction with {@code transactionId} is present
	 * otherwise null
	 */
	@Override
	public Transaction getTransactionById(String transactionId) {
		for (Transaction transaction : transactionsList) {
			if (transaction.getTransactionId().equals(transactionId)) {
				return transaction;
			}
		}
		return null;
	}

	/**
	 * This method returns a {@code List<Transaction>} of {@code type}.
	 * 
	 * @param type : String either "upi" or "bank"
	 * @return {@code List<Transaction>} if transactions of type {@code type} is present
	 * else return empty list.
	 * 
	 */
	@Override
	public List<Transaction> getTransactionByType(String type) {
		List<Transaction> transactionsByType = new ArrayList<Transaction>();
		for (Transaction transaction : transactionsList) {
			if (transaction.getType().equals(type)) {
				transactionsByType.add(transaction);
			}
		}
		return transactionsByType;
	}

	/**
	 * This method returns a {@code List<Transaction>} with {@code status} status.
	 * 
	 * @param type : String either "initiated", "pending", "completed", "failed"
	 * @return {@code List<Transaction>} if transactions of type {@code type} is present
	 * else return empty list.
	 * 
	 */
	@Override
	public List<Transaction> getTransactionByStatus(String status) {
		List<Transaction> transactionsByStatus = new ArrayList<Transaction>();
		for (Transaction transaction : transactionsList) {
			if (transaction.getStatus().equals(status)) {
				transactionsByStatus.add(transaction);
			}
		}
		return transactionsByStatus;
	}

	/**
	 * This code returns a {@code List<Transaction>} performed on the specified date.
	 * 
	 * @param date : Date of transaction
	 * @return {@code List<Transaction} : List of transactions performed on the specified date otherwise null.
	 */
	@Override
	public List<Transaction> getTransactionByDate(Date date) {
	    if (date == null) {
	        return new ArrayList<>();
	    }
	    List<Transaction> transactionsByDate = new ArrayList<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	        for (Transaction transaction : transactionsList) {
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

	/**
	 * This function returns the {@code List<Transaction>} which were performed between 
	 * {@code startDate} and {@code endDate}
	 * 
	 * @param startDate : Start date of the range
	 * @param endDate   : End date of the range
	 * @return {@code List<Transaction>} : List of transactions performed in the date range otherwise empty list.
	 */
	@Override
	public List<Transaction> getTransactionByDateRange(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
	        return null; 
	    }
		List<Transaction> transactionsByDateRange = new ArrayList<Transaction>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (Transaction transaction : transactionsList) {
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

	
	
	/**
	 * This methods returns the {@code List<Transaction>} with amount within the specified range
	 * 
	 * @param startAmount : Lower bound of the amount range.
	 * @param endAmount   : Upper bound of the amount range.
	 * @return {@code List<Transaction>} : A list of transactions with amount in the specified range
	 * other empty list.
	 */
	@Override
	public List<Transaction> getTransactionByAmountRange(Double startAmount, Double endAmount) {
	    // If the start amount is null, no valid range can be determined.
	    if (startAmount == null) {
	        return Collections.emptyList();
	    }
	    
	    // If the end amount is null, assume the range extends to the maximum possible value.
	    if (endAmount == null) {
	        endAmount = Double.MAX_VALUE;
	    }
	    
	    List<Transaction> transactionsByAmountRange = new ArrayList<>();
	    for (Transaction transaction : transactionsList) {
	        Double transactionAmount = transaction.getAmount();
	        if (startAmount <= transactionAmount && transactionAmount <= endAmount) {
	            transactionsByAmountRange.add(transaction);
	        }
	    }
	    return transactionsByAmountRange;
	}

	
	/**
	 * @param transaction : {@code Transaction} object to insert into the transaction table.
	 * @return {@code Transaction} : The transaction object that was created. Duplicate Transaction ID will throw error otherwise null.;
	 */
	@Override
	public Transaction createTransaction(Transaction transaction) {
	    // 1. Check for null input first.
	    if (transaction == null) {
	        return null;
	    }
	    
	    // 2. Check if a transaction with the same ID already exists.
	    boolean idExists = transactionsList.stream()
	            .anyMatch(t -> t.getTransactionId().equals(transaction.getTransactionId()));

	    // 3. If the ID already exists, throw an exception.
	    if (idExists) {
	        throw new IllegalArgumentException("Transaction with ID " + transaction.getTransactionId() + " already exists.");
	    }
	    
	    // 4. If the ID is unique, add the new transaction.
	    transactionsList.add(transaction);
	    
	    // 5. Return the newly added transaction.
	    return transactionsList.get(transactionsList.size() - 1);
	}

	/**
	 * This function deletes a transaction with the {@code transactionId} UID from the transaction table.
	 * 
	 * @param transactionId : Transaction 
	 * @return {@code boolean} : Returns true if deletion successful otherwise false;
	 */
	@Override
	public boolean deleteTransaction(String transactionId) {
		// removeIf() is a safer and concise way to remove items from list.
		return transactionsList.removeIf(transaction -> transaction.getTransactionId().equals(transactionId));
	}

	/**
	 * This methods takes a {@code Transaction} object and updates the transaction
	 * with the same UID in the transaction table.
	 * 
	 * @param transaction : {@code Transaction} object which holds updated data.
	 * @return {@code Transaction} : Returns the updated transaction object if transaction present in 
	 * transaction table otherwise null
	 * */
	@Override
	public Transaction updateTransaction(Transaction transaction) {
        if (transaction == null) {
            return null; 
        }
		for (int i = 0; i < transactionsList.size(); i++) {
			Transaction existingTransaction = transactionsList.get(i);
			if (existingTransaction != null && existingTransaction.getTransactionId().equals(transaction.getTransactionId())) {
				transactionsList.set(i, transaction);
				return transactionsList.get(i);
			}
		}
		return null;	
	}
}
