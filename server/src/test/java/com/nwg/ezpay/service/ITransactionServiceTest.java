package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.Transaction;
import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for the TransactionServiceImpl class, ensuring proper business logic
 * and exception handling for transaction-related operations.
 * <p>
 * This test suite is an integration test, as it relies on the concrete
 * implementation of the DAO and the `transactions.csv` file.
 *
 * @author Shiksha Nayan
 * @version 0.0.1
 * @since 2025-07-28
 * @revised 2025-08-01
 */


class ITransactionServiceTest {

    private TransactionServiceImpl transactionService;
    private final SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateOnlySdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String CSV_FILE_PATH = "data/transactions.csv";

    // Static data for easy setup and reset
    private static final String INITIAL_DATA =
            "TRX001,upi,100.00,completed,2024-07-20 10:00:00\n" +
            "TRX002,bank,250.50,pending,2024-07-20 11:30:00\n" +
            "TRX003,upi,50.00,initiated,2024-07-21 09:00:00\n" +
            "TRX004,bank,150.75,completed,2024-07-21 14:45:00\n" +
            "TRX005,upi,300.00,completed,2024-07-22 16:00:00\n";


    /**
     * Sets up a fresh testing environment before each test.
     * This ensures test isolation by overwriting the CSV file and
     * re-initializing the service.
     *
     * @throws IOException if there is an error writing to the CSV file.
     */
 // In your ITransactionServiceTest file
    @BeforeEach
    void setup() throws IOException {
    	try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
    		writer.write(INITIAL_DATA);
    		}
    	transactionService = new TransactionServiceImpl(); // Initialize service for each test
    }
    


    /**
     * Cleans up the testing environment after each test method.
     * This ensures a clean slate for subsequent tests by clearing the file.
     *
     * @throws IOException if there is an error clearing the CSV file.
     */
    @AfterEach
    void tearDown() throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            writer.write(""); // Clear the file
        }
    }


    // --- getTransactionByIDService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByIdService} method.
     * This suite verifies the method's behavior with various inputs,
     * including valid, non-existent, null, and empty IDs to ensure robustness and proper exception handling.
     */
    @Test
    @DisplayName("Get by ID service - handles existing, non-existing, null, and empty IDs with correct return values or exceptions")
    void testGetTransactionByIdService() throws InvalidTransactionIDException {
    	
        // Test case 1: Existing ID returns correct transaction
        Transaction found = transactionService.getTransactionByIdService("TRX001");
        assertNotNull(found, "Existing ID should return a transaction.");
        assertEquals("TRX001", found.getTransactionId());
        assertEquals("upi", found.getType());

        // Test case 2: Non-existing ID returns null
        Transaction nonExistingFound = transactionService.getTransactionByIdService("TRX999");
        assertNull(nonExistingFound, "Non-existing ID should return null.");
        
        // Test case 3: Null ID throws InvalidTransactionIDException
        InvalidTransactionIDException nullIdException = assertThrows(InvalidTransactionIDException.class, () -> {
            transactionService.getTransactionByIdService(null);
        }, "Null ID should throw InvalidTransactionIDException.");
        assertEquals("ID doesn't match transaction ID semantics.", nullIdException.getMessage());
        
        // Test case 4: Empty string ID returns null
        Transaction emptyIdFound = transactionService.getTransactionByIdService("");
        assertNull(emptyIdFound, "Empty ID should return null.");
    }


    
    // --- getTransactionByTypeService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByTypeService} method.
     * This suite verifies the retrieval of transactions based on type, handling
     * valid, invalid, and null inputs.
     */
    @Test
    @DisplayName("Get by type service - handles valid, invalid, and null types with correct return values or exceptions")
    void testGetTransactionByTypeService() throws InvalidTransactionTypeException {
    	
        // Test case 1: Valid type returns the correct list
        List<Transaction> upiTransactions = transactionService.getTransactionByTypeService("upi");
        assertNotNull(upiTransactions, "Valid type should return a non-null list.");
        assertFalse(upiTransactions.isEmpty(), "List for valid type should not be empty.");
        assertEquals(3, upiTransactions.size());
        assertTrue(upiTransactions.stream().allMatch(t -> t.getType().equalsIgnoreCase("upi")));

        // Test case 2: Invalid type throws InvalidTransactionTypeException
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("invalid");
        }, "Invalid transaction type should throw InvalidTransactionTypeException.");

        // Test case 3: Non-existing type throws InvalidTransactionTypeException
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("crypto");
        }, "Non-existing type that is not valid should throw InvalidTransactionTypeException.");

        // Test case 4: Null type throws InvalidTransactionTypeException
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService(null);
        }, "Null type should throw InvalidTransactionTypeException.");
    }


    
    // --- getTransactionByStatusService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByStatusService} method.
     * This suite verifies the retrieval of transactions based on status, handling
     * valid, invalid, and null inputs.
     */
    @Test
    @DisplayName("Get by status service - handles valid, invalid, and null statuses with correct return values or exceptions")
    void testGetTransactionByStatusService() throws InvalidTransactionStatusException {
    	
        // Test case 1: Valid status returns the correct list
        List<Transaction> completedTransactions = transactionService.getTransactionByStatusService("completed");
        assertNotNull(completedTransactions, "Valid status should return a non-null list.");
        assertFalse(completedTransactions.isEmpty(), "List for valid status should not be empty.");
        assertEquals(2, completedTransactions.size());
        assertTrue(completedTransactions.stream().allMatch(t -> t.getStatus().equals("completed")));
        
        // Test case 2: Invalid status throws InvalidTransactionStatusException
        InvalidTransactionStatusException invalidStatusException = assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionService.getTransactionByStatusService("unknown");
        }, "Invalid status should throw InvalidTransactionStatusException.");
        assertTrue(invalidStatusException.getMessage().contains("Invalid transaction status."));

        // Test case 3: Null status throws InvalidTransactionStatusException
        InvalidTransactionStatusException nullStatusException = assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionService.getTransactionByStatusService(null);
        }, "Null status should throw InvalidTransactionStatusException.");
        assertTrue(nullStatusException.getMessage().contains("Invalid transaction status."));
    }


    
    // --- getTransactionByDateService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByDateService} method.
     * This suite verifies date-based retrieval, including valid and null date inputs.
     */
    @Test
    @DisplayName("Get by date service - handles valid and null dates with correct return values or exceptions")
    void testGetTransactionByDateService() throws InvalidDateFormatException, ParseException {
    	
        // Test case 1: Valid date returns the correct list
        Date date = dateOnlySdf.parse("2024-07-20");
        List<Transaction> transactions = transactionService.getTransactionByDateService(date);
        assertNotNull(transactions, "Valid date should return a non-null list.");
        assertFalse(transactions.isEmpty(), "List for valid date should not be empty.");
        assertEquals(2, transactions.size());

        // Test case 2: Null date throws InvalidDateFormatException
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> {
            transactionService.getTransactionByDateService(null);
        }, "Null date should throw InvalidDateFormatException.");
        assertEquals("Invalid date format", exception.getMessage());
    }


    
    // --- getTransactionByDateRangeService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByDateRangeService} method.
     * This suite verifies date-range-based retrieval, including valid, invalid, and null inputs.
     */
    @Test
    @DisplayName("Get by date range service - handles valid, invalid, and null date ranges with correct return values or exceptions")
    void testGetTransactionByDateRangeService() throws InvalidRangeException, InvalidDateFormatException, ParseException {
    	
        // Test case 1: Valid date range returns the correct list
        Date startDate = dateOnlySdf.parse("2024-07-20");
        Date endDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> transactions = transactionService.getTransactionByDateRangeService(startDate, endDate);
        assertNotNull(transactions, "Valid date range should return a non-null list.");
        assertEquals(4, transactions.size());

        // Test case 2: Invalid date range (start date after end date) throws InvalidRangeException
        Date invalidStartDate = dateOnlySdf.parse("2024-07-22");
        Date invalidEndDate = dateOnlySdf.parse("2024-07-21");
        InvalidRangeException invalidRangeException = assertThrows(InvalidRangeException.class, () -> {
            transactionService.getTransactionByDateRangeService(invalidStartDate, invalidEndDate);
        }, "Invalid date range should throw InvalidRangeException.");
        assertEquals("Start should be smaller or equal to end.", invalidRangeException.getMessage());

        // Test case 3: Null start date throws NullPointerException (exposing bug)
        Date nullStartDate = null;
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByDateRangeService(nullStartDate, endDate);
        }, "Service should throw an exception if start date is null, as it's not handled.");

        // Test case 4: Null end date throws NullPointerException (exposing bug)
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByDateRangeService(startDate, null);
        }, "Service should throw an exception if end date is null, as it's not handled.");
    }

    

    // --- getTransactionByAmountRangeService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.getTransactionByAmountRangeService} method.
     * This suite verifies amount-range-based retrieval, including valid, invalid, and null inputs.
     */
    @Test
    @DisplayName("Get by amount range service - handles valid, invalid, and null amounts with correct return values or exceptions")
    void testGetTransactionByAmountRangeService() throws InvalidRangeException {
    	
        // Test case 1: Valid amount range returns the correct list
        List<Transaction> transactions = transactionService.getTransactionByAmountRangeService(50.00, 150.00);
        assertNotNull(transactions, "Valid amount range should return a non-null list.");
        assertEquals(2, transactions.size());

        // Test case 2: Invalid amount range (start amount greater than end amount) throws InvalidRangeException
        InvalidRangeException invalidAmountException = assertThrows(InvalidRangeException.class, () -> {
            transactionService.getTransactionByAmountRangeService(200.00, 100.00);
        }, "Invalid amount range should throw InvalidRangeException.");
        assertEquals("Start should be smaller or equal to end.", invalidAmountException.getMessage());

        // Test case 3: Null start amount throws NullPointerException (exposing bug)
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByAmountRangeService(null, 100.00);
        }, "Service should throw an exception if start amount is null, as it's not handled.");
        
        // Test case 4: Null end amount throws NullPointerException (exposing bug)
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByAmountRangeService(100.00, null);
        }, "Service should throw an exception if end amount is null, as it's not handled.");
    }


    
    // --- createTransactionService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.createTransactionService} method.
     * This suite verifies the creation of new transactions, including valid and null inputs.
     */
    @Test
    @DisplayName("Create transaction service - handles new valid transactions and null input with correct return values or exceptions")
    void testCreateTransactionService() throws InvalidTransactionObjectException, ParseException {
    	
        // Test case 1: New valid transaction is created successfully
        Transaction newTransaction = new Transaction("TRX007", "netbanking", 500.00, "completed", dateTimeSdf.parse("2024-07-23 10:00:00"));
        Transaction created = transactionService.createTransactionService(newTransaction);
        assertNotNull(created, "Creating a valid transaction should return a non-null object.");
        assertEquals(newTransaction, created);
        
        // Test case 2: Null transaction object throws InvalidTransactionObjectException
        InvalidTransactionObjectException nullTransactionException = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.createTransactionService(null);
        }, "Null transaction should throw InvalidTransactionObjectException.");
        assertEquals("Invalid Transaction object. Ensure fields are correct", nullTransactionException.getMessage());
    }


    
    // --- deleteTransactionService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.deleteTransactionService} method.
     * This suite verifies the deletion of transactions by ID, including existing, non-existing, and null inputs.
     */
    @Test
    @DisplayName("Delete transaction service - handles existing, non-existing, and null IDs with correct return values or exceptions")
    void testDeleteTransactionService() throws InvalidTransactionIDException {
    	
        // Test case 1: Existing ID returns true
        assertTrue(transactionService.deleteTransactionService("TRX001"), "Deleting an existing ID should return true.");

        // Test case 2: Non-existing ID returns false
        assertFalse(transactionService.deleteTransactionService("TRX999"), "Deleting a non-existing ID should return false.");

        // Test case 3: Null ID throws InvalidTransactionIDException
        InvalidTransactionIDException nullIdException = assertThrows(InvalidTransactionIDException.class, () -> {
            transactionService.deleteTransactionService(null);
        }, "Null ID should throw InvalidTransactionIDException.");
        assertEquals("ID doesn't match transaction ID sematics", nullIdException.getMessage());
    }


    
    // --- updateTransactionService Tests ---
    /**
     * Unit tests for the {@code TransactionServiceImpl.updateTransactionService} method.
     * This suite verifies the update of transactions by ID, including valid, non-existent, and null inputs.
     */
    @Test
    @DisplayName("Update transaction service - handles existing, non-existing, and null transactions with correct return values or exceptions")
    void testUpdateTransactionService() throws InvalidTransactionObjectException, ParseException {
    	
        // Test case 1: Existing ID successfully updates and returns the updated transaction
        Transaction transactionToUpdate = new Transaction("TRX001", "netbanking", 123.45, "pending", dateTimeSdf.parse("2024-07-25 12:00:00"));
        Transaction updated = transactionService.updateTransactionService(transactionToUpdate);
        assertNotNull(updated, "Updating an existing transaction should return a non-null object.");
        assertEquals("netbanking", updated.getType());
        assertEquals(123.45, updated.getAmount());
        
        // Test case 2: Null transaction object throws InvalidTransactionObjectException
        InvalidTransactionObjectException nullTransactionException = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.updateTransactionService(null);
        }, "Null transaction should throw InvalidTransactionObjectException.");
        assertEquals("Invalid Transaction object. Cannot update a null transaction.", nullTransactionException.getMessage());
        
        // Test case 3: Non-existing ID throws InvalidTransactionObjectException
        Transaction nonExistingTransaction = new Transaction("TRX999", "netbanking", 123.45, "pending", dateTimeSdf.parse("2024-07-25 12:00:00"));
        InvalidTransactionObjectException nonExistingException = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.updateTransactionService(nonExistingTransaction);
        }, "Update with a non-existing ID should throw InvalidTransactionObjectException.");
        assertEquals("Transaction with ID 'TRX999' not found for update, or update failed.", nonExistingException.getMessage());
    }
}