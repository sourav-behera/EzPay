package com.nwg.ezpay;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionStatusIdException;
import com.nwg.ezpay.exception.InvalidTransactionStatusObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.model.TransactionStatus;
import com.nwg.ezpay.service.ITransactionStatusService;
import com.nwg.ezpay.service.TransactionStatusServiceImpl;

public class EzPayTransactionStatusController {

    public static ITransactionStatusService iTransactionStatusService = new TransactionStatusServiceImpl();
    public static Scanner scanner = new Scanner(System.in);
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void choiceOneHandler() {
        System.out.println("Enter status type:");
        String statusType = scanner.nextLine();
        System.out.println("Enter reason:");
        String reason = scanner.nextLine();
        System.out.println("Enter timestamp:");
        try {
            Date timestamp = simpleDateFormat.parse(scanner.nextLine());
            String transactionStatusId = "11"; // TODO: Implement UID generation
            TransactionStatus newStatus = new TransactionStatus(transactionStatusId, statusType, reason, timestamp);
            if (iTransactionStatusService.createStatusService(newStatus) != null) {
                System.out.println("TransactionStatus created successfully");
                System.out.println(newStatus);
            }
        } catch (ParseException | InvalidTransactionStatusObjectException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void choiceTwoHandler() {
        System.out.println("Set Filters");
        System.out.println("1. Get TransactionStatus by date");
        System.out.println("2. Get TransactionStatus by date range");
        System.out.println("3. Get TransactionStatus by status type");
        System.out.println("4. Get TransactionStatus by reason");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                System.out.println("Enter date in (yyyy-MM-dd) format");
                String dateString = scanner.nextLine();
                try {
                    Date date = simpleDateFormat.parse(dateString);
                    List<TransactionStatus> statusesByDate = iTransactionStatusService.getStatusesByDateService(date);
                    for (TransactionStatus status : statusesByDate) {
                        System.out.println(status);
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
                    List<TransactionStatus> statusesByDateRange = iTransactionStatusService.getStatusesByDateRangeService(startDate,
                            endDate);
                    for (TransactionStatus status : statusesByDateRange) {
                        System.out.println(status);
                    }
                } catch (ParseException | InvalidDateFormatException | InvalidRangeException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    System.out.println("Enter status type");
                    String statusType = scanner.nextLine();
                    List<TransactionStatus> statusesByType = iTransactionStatusService.getStatusesByTypeService(statusType);
                    for (TransactionStatus status : statusesByType) {
                        System.out.println(status);
                    }
                } catch (InvalidTransactionStatusException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    System.out.println("Enter reason");
                    String reason = scanner.nextLine();
                    List<TransactionStatus> statusesByReason = iTransactionStatusService.getStatusesByReasonService(reason);
                    for (TransactionStatus status : statusesByReason) {
                        System.out.println(status);
                    }
                } catch (InvalidTransactionStatusException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    public static void choiceThreeHandler() {
        System.out.println("Enter transaction status ID");
        String transactionStatusId = scanner.nextLine();
        System.out.println("Enter status type:");
        String statusType = scanner.nextLine();
        System.out.println("Enter reason:");
        String reason = scanner.nextLine();
        System.out.println("Enter timestamp:");
        try {
            Date timestamp = simpleDateFormat.parse(scanner.nextLine());
            TransactionStatus updatedStatus = new TransactionStatus(transactionStatusId, statusType, reason, timestamp);
            if (iTransactionStatusService.updateStatusService(updatedStatus) != null) {
                System.out.println("TransactionStatus updated successfully.");
                System.out.println(updatedStatus);
            }
        } catch (ParseException | InvalidTransactionStatusObjectException e) {
            e.printStackTrace();
        }
    }

    public static void choiceFourHandler() {
        System.out.println("Enter the transaction status ID to delete");
        String transactionStatusId = scanner.nextLine();
        try {
            if (iTransactionStatusService.deleteStatusService(transactionStatusId)) {
                System.out.println("TransactionStatus deleted successfully.");
            } else {
                System.out.println("Invalid transaction status ID");
            }
        } catch (InvalidTransactionStatusIdException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter choice");
        System.out.println("1. Create a new transaction status");
        System.out.println("2. Fetch transaction status");
        System.out.println("3. Update transaction status");
        System.out.println("4. Delete transaction status");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
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
            default:
                System.out.println("Invalid choice");
        }
        scanner.close();
    }
}
