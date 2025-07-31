package com.nwg.ezpay.service;

import com.nwg.ezpay.exception.InvalidTransactionIDException;
import com.nwg.ezpay.exception.InvalidTransactionTypeException;
import com.nwg.ezpay.model.Transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

//Author Block
/**
* Unit tests for the TransactionServiceImpl class, ensuring proper business logic
* and exception handling for transaction-related operations.
* 
* 
* @author Shiksha Nayan
* @version 0.0.1
* @since 2024-07-28
* 
* 
*/

import static org.junit.jupiter.api.Assertions.*;

class ITransactionServiceTest { // Removed 'public' for class as it's typically package-private for tests

    private TransactionServiceImpl transactionService;
    // Static data for easy setup and reset
    private static final String INITIAL_DATA =
            "TRX001,upi,100.00,completed,2024-07-20 10:00:00\n" +
            "TRX002,bank,250.50,pending,2024-07-20 11:30:00\n" +
            "TRX003,upi,50.00,initiated,2024-07-21 09:00:00\n" +
            "TRX004,bank,150.75,completed,2024-07-21 14:45:00\n" +
            "TRX005,upi,300.00,completed,2024-07-22 16:00:00\n";

    @BeforeEach
    void setup() throws IOException {
        // Overwrite transactions.csv with a known set of data for each test.
        // This ensures tests are independent and don't rely on previous test runs.
        try (FileWriter writer = new FileWriter("data/transactions.csv")) {
            writer.write(INITIAL_DATA);
        }
        transactionService = new TransactionServiceImpl(); // Initialize service for each test
    }

    @AfterEach
    void tearDown() throws IOException {
        // Optional: Clean up after each test if needed, e.g., clear the CSV
        try (FileWriter writer = new FileWriter("data/transactions.csv")) {
            writer.write(""); // Clear the file
        }
    }


    // --- Failing Tests from your output ---

    @Test
    @DisplayName("Get transactions by type service - invalid type throws exception")
    void testGetTransactionByTypeService_InvalidTypeThrowsException() {
        // The service *should* throw an InvalidTransactionTypeException for "invalid"
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("invalid"); // "invalid" is not "upi" or "bank"
        }, "Invalid transaction type should throw InvalidTransactionTypeException.");
    }

    @Test
    @DisplayName("Get transactions by type service - non-existing type throws exception")
    void testGetTransactionByTypeService_NonExistingTypeThrowsException() {
        // The service *should* throw an InvalidTransactionTypeException for "non-existing" (e.g., "crypto")
        // because it's not one of the VALID_TYPES.
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService("crypto"); // "crypto" is not in VALID_TYPES
        }, "Non-existing type that is not valid should throw InvalidTransactionTypeException.");
    }

    // --- Other Transaction Type Tests (Ensure these pass as well) ---

    @Test
    @DisplayName("Get transactions by type service - valid type returns list")
    void testGetTransactionByTypeService_ValidTypeReturnsList() throws InvalidTransactionTypeException, ParseException {
        List<Transaction> upiTransactions = transactionService.getTransactionByTypeService("upi");
        assertNotNull(upiTransactions);
        assertFalse(upiTransactions.isEmpty());
        assertEquals(3, upiTransactions.size()); // Assuming 3 UPI transactions in INITIAL_DATA
        assertTrue(upiTransactions.stream().allMatch(t -> t.getType().equalsIgnoreCase("upi")));
    }

    @Test
    @DisplayName("Get transactions by type service - null type throws InvalidTransactionTypeException")
    void testGetTransactionByTypeService_NullTypeThrowsException() {
        assertThrows(InvalidTransactionTypeException.class, () -> {
            transactionService.getTransactionByTypeService(null);
        }, "Null type should throw InvalidTransactionTypeException.");
    }


    // --- (Other tests like create, update, delete, getById, getByDateRange, getByStatus should follow) ---

    // Example of a passing test for get by ID
    @Test
    @DisplayName("Get transaction by ID service - existing ID")
    void testGetTransactionByIDService_ExistingID() throws InvalidTransactionIDException, ParseException {
        Transaction found = transactionService.getTransactionByIdService("TRX001");
        assertNotNull(found);
        assertEquals("TRX001", found.getTransactionId());
        assertEquals("upi", found.getType());
    }

    @Test
    @DisplayName("Get transaction by ID service - non-existing ID returns null")
    void testGetTransactionByIDService_NonExistingIDReturnsNull() throws InvalidTransactionIDException, ParseException {
        Transaction found = transactionService.getTransactionByIdService("TRX999");
        assertNull(found, "Non-existing ID should return null.");
    }
}