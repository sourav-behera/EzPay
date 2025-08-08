package com.nwg.ezpay;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.model.Transaction;
import com.nwg.ezpay.service.ITransactionService;
import com.nwg.ezpay.service.TransactionServiceImpl;

public class EzPayTransactionController {
	
	public static ITransactionService itransactionService = new TransactionServiceImpl();
	public static Scanner scanner = new Scanner(System.in);
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void choiceOneHandler() {
		System.out.println("Enter type:");
		String type = scanner.nextLine();
		System.out.println("Enter amount:");
		Double amount = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter status:");
		String status = scanner.nextLine();
		System.out.println("Enter date:");
		try {
			Date date = simpleDateFormat.parse(scanner.nextLine());
			String transactionId = "11";	// TODO: Implement UID generation
			Transaction newTransaction = new Transaction(transactionId, type, amount, status, date);
			if (itransactionService.createTransactionService(newTransaction) != null) {
				System.out.println("Transaction created successfully");
				System.out.println(newTransaction);
			}
		} catch (ParseException | InvalidTransactionObjectException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	public static void choiceTwoHandler() {
		System.out.println("Set Filters");
		System.out.println("1. Get Transactions by date");
		System.out.println("2. Get Transactions by date range");
		System.out.println("3. Get Transactions by amount range");
		System.out.println("4. Get Transactions by status");
		System.out.println("5. Get Transactions by type");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
		case 1:
			System.out.println("Enter date in (yyyy-MM-dd) format");
			String dateString = scanner.nextLine();
			try {
				Date date = simpleDateFormat.parse(dateString);
				List<Transaction> transactionsByDate = itransactionService.getTransactionByDateService(date);
				for (Transaction transaction : transactionsByDate) {
					System.out.println(transaction);
				}
			} catch (ParseException | InvalidDateFormatException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				System.out.println("Enter start date in (yyyy-MM-dd) format");
				Date startDate = simpleDateFormat.parse(scanner.nextLine());
				System.out.println("Enter end date in (yyyy-MM-dd) format");
				Date endDate = simpleDateFormat.parse(scanner.nextLine());
				List<Transaction> transactionsByDate = itransactionService.getTransactionByDateRangeService(startDate, endDate);
				for (Transaction transaction : transactionsByDate) {
					System.out.println(transaction);
				}
			} catch (ParseException | InvalidDateFormatException | InvalidRangeException e) {
				e.printStackTrace();
			}
			break;
			
		case 3:
			try {
				System.out.println("Enter start amount");
				Double startAmount = Double.parseDouble(scanner.nextLine());
				System.out.println("Enter end amount");
				Double endAmount = Double.parseDouble(scanner.nextLine());
				List<Transaction> transactionsByAmount = itransactionService.getTransactionByAmountRangeService(startAmount, endAmount);				
				for (Transaction transaction : transactionsByAmount) {
					System.out.println(transaction);
				}
			} catch (InvalidRangeException e){
				e.printStackTrace();
			}
			break;
			
		case 4:
			try {
				System.out.println("Enter status");
				String status = scanner.nextLine();
				List<Transaction> transactionsByStatus = itransactionService.getTransactionByStatusService(status);
				for (Transaction transaction : transactionsByStatus) {
					System.out.println(transaction);
				}
				
			} catch (InvalidTransactionStatusException e) {
				e.printStackTrace();
			}
			break;
		case 5:
			try {
				System.out.println("Enter the type to show");
				String type = scanner.nextLine();
				List<Transaction> transactionsByType = itransactionService.getTransactionByTypeService(type);
				for (Transaction transaction : transactionsByType) {
					System.out.println(transaction);
				}
			} catch (InvalidTransactionTypeException e) {
				e.printStackTrace();
			}
			break;
		default :
			System.out.println("Invalid choice");
		}
	}
	
	public static void choiceThreeHandler() {
		System.out.println("Enter transaction ID");
		String transactionID = scanner.nextLine();
		System.out.println("Enter type:");
		String type = scanner.nextLine();
		System.out.println("Enter amount:");
		Double amount = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter status:");
		String status = scanner.nextLine();
		System.out.println("Enter date:");
		try {
			Date date = simpleDateFormat.parse(scanner.nextLine());
			Transaction updatedTransaction = new Transaction(transactionID, type, amount, status, date);
			if (itransactionService.updateTransactionService(updatedTransaction) != null) {
				System.out.println("Transaction updated successfully.");
				System.out.println(itransactionService);
			}
		} catch (ParseException | InvalidTransactionObjectException e) {
			e.printStackTrace();
		}
	}
	
	public static void choiceFourHandler() {
		System.out.println("Enter the transaction ID to delete");
		String transactionId = scanner.nextLine();
		try {
			if (itransactionService.deleteTransactionService(transactionId)) {
				System.out.println("Transaction deleted successfully.");
			} else {
				System.out.println("Invalid transaction ID");
			}
			
		} catch (InvalidTransactionIDException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("Enter choice");
		System.out.println("1. Create a new transaction");
		System.out.println("2. Fetch transaction");
		System.out.println("3. Update transaction");
		System.out.println("4. Delte transaction");
		int choice = Integer.parseInt(scanner.nextLine());
		
		switch(choice) {
		case 1:
			choiceOneHandler();
			break;
		case 2:
			choiceTwoHandler();
			break;
		case 3:
			choiceThreeHandler();
			break;
		case 4:
			choiceFourHandler();
			break;
		}
		scanner.close();
	}
}
