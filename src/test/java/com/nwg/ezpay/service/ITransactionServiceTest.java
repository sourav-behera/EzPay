package com.nwg.ezpay.service;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionObjectException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


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
     * Tests the successful retrieval of a transaction using an existing ID.
     * Verifies that the returned transaction is not null and has the correct ID and type.
     */
    @Test
    @DisplayName("Get by ID - existing ID returns correct transaction")
    void testGetTransactionByIDService_ExistingID() throws InvalidTransactionIDException {
        Transaction found = transactionService.getTransactionByIdService("TRX001");
        assertNotNull(found);
        assertEquals("TRX001", found.getTransactionId());
        assertEquals("upi", found.getType());
    }

    
    
    /**
     * Tests the behavior when searching for a non-existent transaction ID.
     * Verifies that the service correctly returns null.
     */
    @Test
    @DisplayName("Get by ID - non-existing ID returns null")
    void testGetTransactionByIDService_NonExistingIDReturnsNull() throws InvalidTransactionIDException {
        Transaction found = transactionService.getTransactionByIdService("TRX999");
        assertNull(found, "Non-existing ID should return null.");
    }

    
    
    /**
     * Tests the exception handling when a null transaction ID is provided.
     * Verifies that the service throws an {@link InvalidTransactionIDException}
     * with the correct error message.
     */
    @Test
    @DisplayName("Get by ID - null ID throws InvalidTransactionIDException")
    void testGetTransactionByIDService_NullIDThrowsException() {
        InvalidTransactionIDException exception = assertThrows(InvalidTransactionIDException.class, () -> {
            transactionService.getTransactionByIdService(null);
        });
        assertEquals("ID doesn't match transaction ID semantics.", exception.getMessage());
    }

    
    
    /**
     * Tests the behavior when an empty string is provided as the transaction ID.
     * This is an edge case that should result in no transaction being found, returning null.
     */
    @Test
    @DisplayName("Get by ID - empty ID returns null")
    void testGetTransactionByIDService_EmptyIDReturnsNull() throws InvalidTransactionIDException {
        Transaction found = transactionService.getTransactionByIdService("");
        assertNull(found, "Empty ID should return null.");
    }


    
    // --- getTransactionByTypeService Tests ---
    /**
     * Tests the successful retrieval of transactions for a valid type ("upi").
     * Verifies that the returned list is not null, not empty, and contains the correct number
     * of transactions of the specified type.
     */
    @Test
    @DisplayName("Get transactions by type service - valid type returns list")
    void testGetTransactionByTypeService_ValidTypeReturnsList() throws InvalidTransactionTypeException {
        List<Transaction> upiTransactions = transactionService.getTransactionByTypeService("upi");
        assertNotNull(upiTransactions);
        assertFalse(upiTransactions.isEmpty());
        assertEquals(3, upiTransactions.size());
        assertTrue(upiTransactions.stream().allMatch(t -> t.getType().equalsIgnoreCase("upi")));
    }

    
    
    /**
     * Tests the exception handling when an invalid transaction type is provided.
     * Verifies that the service throws an {@link InvalidTransactionTypeException}.
     */
    @Test
    @DisplayName("Get transactions by type service - invalid type throws exception")
    void testGetTransactionByTypeService_InvalidTypeThrowsException() {
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("invalid");
        }, "Invalid transaction type should throw InvalidTransactionTypeException.");
    }

    
    
    /**
     * Tests the exception handling for a type that is not in the list of valid types.
     * Verifies that the service throws an {@link InvalidTransactionTypeException}.
     */
    @Test
    @DisplayName("Get transactions by type service - non-existing type throws exception")
    void testGetTransactionByTypeService_NonExistingTypeThrowsException() {
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("crypto");
        }, "Non-existing type that is not valid should throw InvalidTransactionTypeException.");
    }

    
    
    /**
     * Tests the exception handling when a null transaction type is provided.
     * Verifies that the service throws an {@link InvalidTransactionTypeException}.
     */
    @Test
    @DisplayName("Get transactions by type service - null type throws InvalidTransactionTypeException")
    void testGetTransactionByTypeService_NullTypeThrowsException() {
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService(null);
        }, "Null type should throw InvalidTransactionTypeException.");
    }

    

    // --- getTransactionByStatusService Tests ---
    /**
     * Tests the successful retrieval of transactions for a valid status ("completed").
     * Verifies the returned list is not null, not empty, and contains the correct number of transactions.
     */
    @Test
    @DisplayName("Get transactions by status service - valid status returns list")
    void testGetTransactionByStatusService_ValidStatusReturnsList() throws InvalidTransactionStatusException {
        List<Transaction> completedTransactions = transactionService.getTransactionByStatusService("completed");
        assertNotNull(completedTransactions);
        assertFalse(completedTransactions.isEmpty());
        assertEquals(2, completedTransactions.size());
        assertTrue(completedTransactions.stream().allMatch(t -> t.getStatus().equals("completed")));
    }

    
    
    /**
     * Tests the exception handling when an invalid status string is provided.
     * Verifies that the service throws an {@link InvalidTransactionStatusException}.
     */
    @Test
    @DisplayName("Get transactions by status service - invalid status throws exception")
    void testGetTransactionByStatusService_InvalidStatusThrowsException() {
        InvalidTransactionStatusException exception = assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionService.getTransactionByStatusService("unknown");
        });
        assertTrue(exception.getMessage().contains("Invalid transaction status."));
    }

    
    
    /**
     * Tests the exception handling when a null status is provided.
     * Verifies that the service throws an {@link InvalidTransactionStatusException}.
     */
    @Test
    @DisplayName("Get transactions by status service - null status throws exception")
    void testGetTransactionByStatusService_NullStatusThrowsException() {
        InvalidTransactionStatusException exception = assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionService.getTransactionByStatusService(null);
        });
        assertTrue(exception.getMessage().contains("Invalid transaction status."));
    }


    
    // --- getTransactionByDateService Tests ---
    /**
     * Tests the successful retrieval of transactions for a valid date.
     * Verifies that the returned list is not null, not empty, and contains the correct number of transactions.
     */
    @Test
    @DisplayName("Get transactions by date service - valid date returns list")
    void testGetTransactionByDateService_ValidDateReturnsList() throws InvalidDateFormatException, ParseException {
        Date date = dateOnlySdf.parse("2024-07-20");
        List<Transaction> transactions = transactionService.getTransactionByDateService(date);
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(2, transactions.size());
    }

    
    
    /**
     * Tests the exception handling when a null date object is provided.
     * Verifies that the service throws an {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Get transactions by date service - null date throws exception")
    void testGetTransactionByDateService_NullDateThrowsException() {
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> {
            transactionService.getTransactionByDateService(null);
        });
        assertEquals("Invalid date format", exception.getMessage());
    }


    
    // --- getTransactionByDateRangeService Tests ---
    /**
     * Tests the successful retrieval of transactions for a valid date range.
     * Verifies that the returned list is not null and contains the expected number of transactions.
     */
    @Test
    @DisplayName("Get transactions by date range service - valid range returns list")
    void testGetTransactionByDateRangeService_ValidRangeReturnsList() throws InvalidRangeException, InvalidDateFormatException, ParseException {
        Date startDate = dateOnlySdf.parse("2024-07-20");
        Date endDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> transactions = transactionService.getTransactionByDateRangeService(startDate, endDate);
        assertNotNull(transactions);
        assertEquals(4, transactions.size());
    }

    
    
    /**
     * Tests the exception handling when an invalid date range is provided (start date after end date).
     * Verifies that the service throws an {@link InvalidRangeException}.
     */
    @Test
    @DisplayName("Get transactions by date range service - start date after end date throws exception")
    void testGetTransactionByDateRangeService_InvalidRangeThrowsException() throws ParseException {
        Date startDate = dateOnlySdf.parse("2024-07-22");
        Date endDate = dateOnlySdf.parse("2024-07-21");
        InvalidRangeException exception = assertThrows(InvalidRangeException.class, () -> {
            transactionService.getTransactionByDateRangeService(startDate, endDate);
        });
        assertEquals("Start should be smaller or equal to end.", exception.getMessage());
    }

    
    
    /**
     * Tests the behavior when a null start date is provided.
     * This test exposes a bug where a NullPointerException would be thrown.
     */
    @Test
    @DisplayName("Get transactions by date range service - null start date throws NullPointerException (exposing bug)")
    void testGetTransactionByDateRangeService_NullStartDateThrowsException() throws ParseException {
        Date endDate = dateOnlySdf.parse("2024-07-21");
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByDateRangeService(null, endDate);
        }, "Service should throw an exception if start date is null, as it's not handled.");
    }

    
    
    /**
     * Tests the behavior when a null end date is provided.
     * This test exposes a bug where a NullPointerException would be thrown.
     */
    @Test
    @DisplayName("Get transactions by date range service - null end date throws NullPointerException (exposing bug)")
    void testGetTransactionByDateRangeService_NullEndDateThrowsException() throws ParseException {
        Date startDate = dateOnlySdf.parse("2024-07-21");
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByDateRangeService(startDate, null);
        }, "Service should throw an exception if end date is null, as it's not handled.");
    }


    
    // --- getTransactionByAmountRangeService Tests ---
    /**
     * Tests the successful retrieval of transactions for a valid amount range.
     * Verifies that the returned list is not null and contains the expected number of transactions.
     */
    @Test
    @DisplayName("Get transactions by amount range service - valid range returns list")
    void testGetTransactionByAmountRangeService_ValidRangeReturnsList() throws InvalidRangeException {
        List<Transaction> transactions = transactionService.getTransactionByAmountRangeService(50.00, 150.00);
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }

    
    
    /**
     * Tests the exception handling when an invalid amount range is provided (start amount greater than end amount).
     * Verifies that the service throws an {@link InvalidRangeException}.
     */
    @Test
    @DisplayName("Get transactions by amount range service - start amount greater than end amount throws exception")
    void testGetTransactionByAmountRangeService_InvalidRangeThrowsException() {
        InvalidRangeException exception = assertThrows(InvalidRangeException.class, () -> {
            transactionService.getTransactionByAmountRangeService(200.00, 100.00);
        });
        assertEquals("Start should be smaller or equal to end.", exception.getMessage());
    }

    
    
    /**
     * Tests the behavior when a null start amount is provided.
     * This test exposes a bug where a NullPointerException would be thrown.
     */
    @Test
    @DisplayName("Get transactions by amount range service - null start amount throws NullPointerException (exposing bug)")
    void testGetTransactionByAmountRangeService_NullStartAmountThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByAmountRangeService(null, 100.00);
        }, "Service should throw an exception if start amount is null, as it's not handled.");
    }

    
    
    /**
     * Tests the behavior when a null end amount is provided.
     * This test exposes a bug where a NullPointerException would be thrown.
     */
    @Test
    @DisplayName("Get transactions by amount range service - null end amount throws NullPointerException (exposing bug)")
    void testGetTransactionByAmountRangeService_NullEndAmountThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            transactionService.getTransactionByAmountRangeService(100.00, null);
        }, "Service should throw an exception if end amount is null, as it's not handled.");
    }


    
    // --- createTransactionService Tests ---
    /**
     * Tests the successful creation of a new, valid transaction.
     * Verifies that the service returns a non-null transaction object.
     */
    @Test
    @DisplayName("Create transaction - new valid transaction returns transaction")
    void testCreateTransactionService_ValidTransactionReturnsTransaction() throws InvalidTransactionObjectException, ParseException {
        Transaction newTransaction = new Transaction("TRX007", "netbanking", 500.00, "completed", dateTimeSdf.parse("2024-07-23 10:00:00"));
        Transaction created = transactionService.createTransactionService(newTransaction);
        assertNotNull(created);
        assertEquals(newTransaction, created);
    }

    
    
    /**
     * Tests the exception handling when a null transaction object is provided for creation.
     * Verifies that the service throws an {@link InvalidTransactionObjectException}.
     */
    @Test
    @DisplayName("Create transaction - null transaction throws exception")
    void testCreateTransactionService_NullTransactionThrowsException() {
        InvalidTransactionObjectException exception = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.createTransactionService(null);
        });
        assertEquals("Invalid Transaction object. Ensure fields are correct", exception.getMessage());
    }


    
    // --- deleteTransactionService Tests ---
    /**
     * Tests the successful deletion of a transaction by an existing ID.
     * Verifies that the service returns true.
     */
    @Test
    @DisplayName("Delete transaction - existing ID returns true")
    void testDeleteTransactionService_ExistingIDReturnsTrue() throws InvalidTransactionIDException {
        assertTrue(transactionService.deleteTransactionService("TRX001"));
    }

    
    
    /**
     * Tests the behavior when a non-existing ID is provided for deletion.
     * Verifies that the service returns false.
     */
    @Test
    @DisplayName("Delete transaction - non-existing ID returns false")
    void testDeleteTransactionService_NonExistingIDReturnsFalse() throws InvalidTransactionIDException {
        assertFalse(transactionService.deleteTransactionService("TRX999"));
    }

    
    
    /**
     * Tests the exception handling when a null ID is provided for deletion.
     * Verifies that the service throws an {@link InvalidTransactionIDException}.
     */
    @Test
    @DisplayName("Delete transaction - null ID throws exception")
    void testDeleteTransactionService_NullIDThrowsException() {
        InvalidTransactionIDException exception = assertThrows(InvalidTransactionIDException.class, () -> {
            transactionService.deleteTransactionService(null);
        });
        assertEquals("ID doesn't match transaction ID sematics", exception.getMessage());
    }


    
    // --- updateTransactionService Tests ---
    /**
     * Tests the successful update of a transaction using an existing ID.
     * Verifies that the service returns the updated transaction with the new details.
     */
    @Test
    @DisplayName("Update transaction - existing ID returns updated transaction")
    void testUpdateTransactionService_ExistingIDReturnsUpdatedTransaction() throws InvalidTransactionObjectException, ParseException {
        Transaction transactionToUpdate = new Transaction("TRX001", "netbanking", 123.45, "pending", dateTimeSdf.parse("2024-07-25 12:00:00"));
        Transaction updated = transactionService.updateTransactionService(transactionToUpdate);
        assertNotNull(updated);
        assertEquals("netbanking", updated.getType());
        assertEquals(123.45, updated.getAmount());
    }

    
    
    /**
     * Tests the exception handling when a null transaction object is provided for update.
     * Verifies that the service throws an {@link InvalidTransactionObjectException}.
     */
    @Test
    @DisplayName("Update transaction - null transaction throws exception")
    void testUpdateTransactionService_NullTransactionThrowsException() {
        InvalidTransactionObjectException exception = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.updateTransactionService(null);
        });
        assertEquals("Invalid Transaction object. Cannot update a null transaction.", exception.getMessage());
    }

    
    
    /**
     * Tests the exception handling when an update is attempted with a non-existing transaction ID.
     * Verifies that the service throws an {@link InvalidTransactionObjectException}.
     */
    @Test
    @DisplayName("Update transaction - non-existing ID throws exception")
    void testUpdateTransactionService_NonExistingIDThrowsException() throws ParseException {
        Transaction transactionToUpdate = new Transaction("TRX999", "netbanking", 123.45, "pending", dateTimeSdf.parse("2024-07-25 12:00:00"));
        InvalidTransactionObjectException exception = assertThrows(InvalidTransactionObjectException.class, () -> {
            transactionService.updateTransactionService(transactionToUpdate);
        });
        assertEquals("Transaction with ID 'TRX999' not found for update, or update failed.", exception.getMessage());
    }
}