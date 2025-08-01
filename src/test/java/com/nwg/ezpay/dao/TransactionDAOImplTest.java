package com.nwg.ezpay.dao;

import com.nwg.ezpay.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Description: Unit tests for the TransactionDAOImpl class. This suite ensures that all
 * data access operations for transactions, including CRUD (Create, Read, Update, Delete)
 * and search functionalities, work as expected.
 *
 * <p>Each test is isolated by setting up a fresh, known state in a CSV file
 * and a static list before each test and clearing it afterward.
 *
 * @author Shiksha Nayan
 * @version 0.0.1
 * @since 2025-07-28
 * @revised 2025-08-01
 */


class TransactionDAOImplTest {

    private ITransactionDAO transactionDAO;
    private final SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateOnlySdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final String CSV_FILE_PATH = "data/transactions.csv";

    // Initial data for the CSV file, ensuring test isolation.
    private static final String INITIAL_CSV_DATA =
            "TRX001,upi,100.00,completed,2024-07-20 10:00:00\n" +
            "TRX002,bank,250.50,pending,2024-07-20 11:30:00\n" +
            "TRX003,upi,50.00,initiated,2024-07-21 09:00:00\n" +
            "TRX004,bank,150.75,completed,2024-07-21 14:45:00\n" +
            "TRX005,upi,300.00,completed,2024-07-22 16:00:00\n" +
            "TRX006,bank,10.00,failed,2024-07-22 17:00:00";


    /**
     * Set up a fresh testing environment before each test method.
     * This ensures test isolation by overwriting the CSV file and reloading the DAO's static list.
     *
     * @throws IOException if there is an error writing to the CSV file.
     */
    @BeforeEach
    void setup() throws IOException {
    	
        // Overwrite transactions.csv with a known set of data for each test.
        writeInitialCsvData();
        
        // Clear and reload the static transactions list in DAO for a fresh state.
        reloadTransactionsListFromCsv();

        // Instantiate the DAO for the test.
        transactionDAO = new TransactionDAOImpl();
    }

    

    /**
     * Clean up the testing environment after each test method.
     * This ensures a clean slate for subsequent tests.
     *
     * @throws IOException if there is an error clearing the CSV file.
     */
    @AfterEach
    void tearDown() throws IOException {
        // Clear the static list to release resources.
        TransactionDAOImpl.transactionsList.clear();
        // Clear the CSV file as well to ensure a clean slate.
        clearCsvFile();
    }


    
    // --- getTransactionById Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionById} method.
     * This test suite verifies the method's behavior with various inputs,
     * including valid, non-existent, null, and empty IDs to ensure robustness.
     */
    @Test
    @DisplayName("Get transaction by ID - returns correct transaction for existing, null, non-existing, and empty IDs")
    void testGetTransactionById() {

        //Existing ID returns a transaction
        Transaction existingTransaction = transactionDAO.getTransactionById("TRX001");
        assertNotNull(existingTransaction);
        assertEquals("TRX001", existingTransaction.getTransactionId());
        assertEquals("upi", existingTransaction.getType());

        //Non-existing ID returns null
        Transaction nonExistingTransaction = transactionDAO.getTransactionById("NONEXISTENT");
        assertNull(nonExistingTransaction);

        //Null ID returns null
        Transaction nullIdTransaction = transactionDAO.getTransactionById(null);
        assertNull(nullIdTransaction);
        
        //Empty string ID returns null
        Transaction emptyIdTransaction = transactionDAO.getTransactionById("");
        assertNull(emptyIdTransaction);
    }


    
    // --- getTransactionByType Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionByType} method.
     * This suite verifies the retrieval of transactions based on type, handling
     * valid, non-existing, null, and case-sensitive inputs.
     */
    @Test
    @DisplayName("Get transactions by type - handles existing, non-existing, null, and incorrect case types")
    void testGetTransactionByType() {
    	
        //Existing type returns the correct list
        List<Transaction> upiTransactions = transactionDAO.getTransactionByType("upi");
        assertNotNull(upiTransactions);
        assertFalse(upiTransactions.isEmpty());
        assertEquals(3, upiTransactions.size());
        assertTrue(upiTransactions.stream().allMatch(t -> "upi".equals(t.getType())));

        //Non-existing type returns an empty list
        List<Transaction> cryptoTransactions = transactionDAO.getTransactionByType("crypto");
        assertNotNull(cryptoTransactions);
        assertTrue(cryptoTransactions.isEmpty());

        //Null type returns an empty list
        List<Transaction> nullTypeTransactions = transactionDAO.getTransactionByType(null);
        assertNotNull(nullTypeTransactions);
        assertTrue(nullTypeTransactions.isEmpty());

        //Case-sensitive match returns an empty list for wrong case
        List<Transaction> wrongCaseTransactions = transactionDAO.getTransactionByType("Upi");
        assertNotNull(wrongCaseTransactions);
        assertTrue(wrongCaseTransactions.isEmpty());
    }

    

    // --- getTransactionByStatus Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionByStatus} method.
     * This suite verifies the retrieval of transactions based on status, handling
     * valid, non-existent, and null inputs.
     */
    @Test
    @DisplayName("Get transactions by status - handles existing, non-existing, and null status")
    void testGetTransactionByStatus() {
    	
        //Existing status returns the correct list
        List<Transaction> completedTransactions = transactionDAO.getTransactionByStatus("completed");
        assertNotNull(completedTransactions);
        assertFalse(completedTransactions.isEmpty());
        assertEquals(3, completedTransactions.size());
        assertTrue(completedTransactions.stream().allMatch(t -> "completed".equals(t.getStatus())));

        //Non-existing status returns an empty list
        List<Transaction> unknownStatusTransactions = transactionDAO.getTransactionByStatus("unknown");
        assertNotNull(unknownStatusTransactions);
        assertTrue(unknownStatusTransactions.isEmpty());

        //Null status returns an empty list
        List<Transaction> nullStatusTransactions = transactionDAO.getTransactionByStatus(null);
        assertNotNull(nullStatusTransactions);
        assertTrue(nullStatusTransactions.isEmpty());
    }

    

    // --- getTransactionByDate Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionByDate} method.
     * This suite verifies date-based retrieval, including existing dates,
     * non-existing dates, and null input, with special attention to date-only comparison.
     */
    @Test
    @DisplayName("Get transactions by date - handles existing, non-existing, null, and dates with time components")
    void testGetTransactionByDate() throws ParseException {
    	
        //Existing date returns the correct list
        Date date = dateOnlySdf.parse("2024-07-20");
        List<Transaction> transactionsOnDate = transactionDAO.getTransactionByDate(date);
        assertNotNull(transactionsOnDate);
        assertFalse(transactionsOnDate.isEmpty());
        assertEquals(2, transactionsOnDate.size());
        assertTrue(transactionsOnDate.stream().allMatch(t -> {
            try { return dateOnlySdf.parse(dateOnlySdf.format(t.getDate())).equals(date); }
            catch (ParseException e) { fail("Date parsing error in assertion"); return false; }
        }));

        //Non-existing date returns an empty list
        Date nonExistingDate = dateOnlySdf.parse("2025-01-01");
        List<Transaction> transactionsOnNonExistingDate = transactionDAO.getTransactionByDate(nonExistingDate);
        assertNotNull(transactionsOnNonExistingDate);
        assertTrue(transactionsOnNonExistingDate.isEmpty());

        //Null date returns an empty list
        List<Transaction> transactionsOnNullDate = transactionDAO.getTransactionByDate(null);
        assertNotNull(transactionsOnNullDate);
        assertTrue(transactionsOnNullDate.isEmpty());
        
    }

    

    // --- getTransactionByDateRange Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionByDate} method.
     * This suite verifies date-based retrieval, including existing dates,
     * non-existing dates, and null input, with special attention to date-only comparison.
     */
    @Test
    @DisplayName("Get transactions by date range - handles valid, empty, single, null, and invalid ranges")
    void testGetTransactionByDateRange() throws ParseException {
    	
        //Valid range returns the correct list
        Date startDate = dateOnlySdf.parse("2024-07-20");
        Date endDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByDateRange(startDate, endDate);
        assertNotNull(transactionsInRange);
        assertFalse(transactionsInRange.isEmpty());
        assertEquals(4, transactionsInRange.size());

        //Single date range returns the correct list
        Date singleDate = dateOnlySdf.parse("2024-07-22");
        List<Transaction> transactionsOnSingleDate = transactionDAO.getTransactionByDateRange(singleDate, singleDate);
        assertNotNull(transactionsOnSingleDate);
        assertFalse(transactionsOnSingleDate.isEmpty());
        assertEquals(2, transactionsOnSingleDate.size());

        //Range with no transactions returns null
        Date futureStartDate = dateOnlySdf.parse("2025-01-01");
        Date futureEndDate = dateOnlySdf.parse("2025-01-31");
        List<Transaction> futureTransactions = transactionDAO.getTransactionByDateRange(futureStartDate, futureEndDate);
        assertNull(futureTransactions);

        //Null start date returns null
        Date nullStartDate = null;
        List<Transaction> nullStartRange = transactionDAO.getTransactionByDateRange(nullStartDate, endDate);
        assertNull(nullStartRange);

        //Null end date returns null
        Date nullEndDate = null;
        List<Transaction> nullEndRange = transactionDAO.getTransactionByDateRange(startDate, nullEndDate);
        assertNull(nullEndRange);
        
        //Start date after end date returns null
        Date invalidStartDate = dateOnlySdf.parse("2024-07-22");
        Date invalidEndDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> invalidRange = transactionDAO.getTransactionByDateRange(invalidStartDate, invalidEndDate);
        assertNull(invalidRange);
    }

    

    // --- getTransactionByAmountRange Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.getTransactionByDateRange} method.
     * This comprehensive test suite covers all scenarios for date range retrieval,
     * including valid ranges, single-day ranges, ranges with no data,
     * null inputs, and invalid ranges.
     */
    @Test
    @DisplayName("Get transactions by amount range - handles valid, empty, and invalid ranges")
    void testGetTransactionByAmountRange() {
    	
        //Valid range returns the correct list
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByAmountRange(50.00, 150.00);
        assertNotNull(transactionsInRange);
        assertFalse(transactionsInRange.isEmpty());
        assertEquals(2, transactionsInRange.size()); // TRX001 (100.00), TRX003 (50.00)
        assertTrue(transactionsInRange.stream().allMatch(t -> t.getAmount() >= 50.00 && t.getAmount() <= 150.00));

        //Range with no transactions returns an empty list
        List<Transaction> emptyRange = transactionDAO.getTransactionByAmountRange(1000.00, 2000.00);
        assertNotNull(emptyRange);
        assertTrue(emptyRange.isEmpty());

        //Start amount greater than end amount returns an empty list
        List<Transaction> invalidRange = transactionDAO.getTransactionByAmountRange(200.00, 100.00);
        assertNotNull(invalidRange);
        assertTrue(invalidRange.isEmpty());
        
        //Null start amount returns an empty list
        List<Transaction> nullStartAmount = transactionDAO.getTransactionByAmountRange(null, 100.00);
        // Assertion: The DAO should return an empty list, not null, for an invalid range.
        assertNotNull(nullStartAmount);
        assertTrue(nullStartAmount.isEmpty());
        
        // Null end amount returns transactions up to max amount
        List<Transaction> nullEndAmount = transactionDAO.getTransactionByAmountRange(100.00, null);
        assertNotNull(nullEndAmount);
        assertFalse(nullEndAmount.isEmpty());
        // Should include TRX001 (100.00), TRX002 (250.50), TRX004 (150.75), TRX005 (300.00)
        assertTrue(nullEndAmount.stream().allMatch(t -> t.getAmount() >= 100.00));
        
    }

    

    // --- createTransaction Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.createTransaction} method.
     * This suite tests the creation of a new valid transaction and the
     * behavior when the input transaction object is null.
     */
    @Test
    @DisplayName("Create transaction - handles new valid transaction and null input")
    void testCreateTransaction() throws ParseException {
    	
        //New valid transaction is added successfully
        int initialSize = TransactionDAOImpl.transactionsList.size();
        Transaction newTransaction = new Transaction("TRX007", "netbanking", 500.00, "completed", dateTimeSdf.parse("2024-07-23 10:00:00"));
        Transaction created = transactionDAO.createTransaction(newTransaction);

        assertNotNull(created);
        assertEquals(initialSize + 1, TransactionDAOImpl.transactionsList.size());
        assertEquals(newTransaction, created);
        assertTrue(TransactionDAOImpl.transactionsList.contains(newTransaction));

        //Null transaction returns null
        int currentSize = TransactionDAOImpl.transactionsList.size();
        Transaction nullCreated = transactionDAO.createTransaction(null);
        assertNull(nullCreated);
        assertEquals(currentSize, TransactionDAOImpl.transactionsList.size());
    }

    
    
    /**
     * Test case specifically designed to expose a loophole in the DAO's {@code createTransaction}
     * method, which currently allows the creation of a new transaction with an
     * ID that already exists in the data source.
     */
    @Test
    @DisplayName("Create transaction - allows duplicate ID, exposing a loophole in the DAO")
    void testCreateTransaction_DuplicateId() throws ParseException {
        int initialSize = TransactionDAOImpl.transactionsList.size();
        Transaction duplicateTransaction = new Transaction(
                "TRX001", "duplicate_type", 999.99, "duplicate_status", dateTimeSdf.parse("2024-07-23 10:00:00"));

        transactionDAO.createTransaction(duplicateTransaction);

        
        // This assertion passes, but it highlights the DAO's loophole:
        // A new transaction with an existing ID has been added to the list.
        assertEquals(initialSize + 1, TransactionDAOImpl.transactionsList.size());
        
        long count = TransactionDAOImpl.transactionsList.stream()
                .filter(t -> "TRX001".equals(t.getTransactionId()))
                .count();
        assertEquals(2, count, "The DAO should have allowed a duplicate ID to be created.");
    }


    
    // --- deleteTransaction Tests ---
    /**
    * Unit tests for the {@code TransactionDAOImpl.deleteTransaction} method.
 	* This suite verifies the deletion of a transaction by ID, including
 	* existing, non-existing, and null inputs.
 	*/
    @Test
    @DisplayName("Delete transaction - handles existing, non-existing, and null ID")
    void testDeleteTransaction() {
    	
        //Existing ID returns true and removes the transaction
        int initialSize = TransactionDAOImpl.transactionsList.size();
        boolean deleted = transactionDAO.deleteTransaction("TRX001");

        assertTrue(deleted);
        assertEquals(initialSize - 1, TransactionDAOImpl.transactionsList.size());
        assertNull(transactionDAO.getTransactionById("TRX001"));

        //Non-existing ID returns false and list size is unchanged
        int currentSize = TransactionDAOImpl.transactionsList.size();
        boolean nonExistingDeleted = transactionDAO.deleteTransaction("NONEXISTENT");

        assertFalse(nonExistingDeleted);
        assertEquals(currentSize, TransactionDAOImpl.transactionsList.size());

        //Null ID returns false and list size is unchanged
        boolean nullIdDeleted = transactionDAO.deleteTransaction(null);
        assertFalse(nullIdDeleted);
        assertEquals(currentSize, TransactionDAOImpl.transactionsList.size());
    }


    
    // --- updateTransaction Tests ---
    /**
     * Unit tests for the {@code TransactionDAOImpl.updateTransaction} method.
     * This comprehensive test suite covers updating a transaction with an existing
     * ID, and handling failure scenarios such as a non-existing ID, null input
     * object, and a null ID within the object.
     */
    @Test
    @DisplayName("Update transaction - handles existing ID, non-existing ID, null transaction, and null ID in object")
    void testUpdateTransaction() throws ParseException {
    	
        //Existing ID updates and returns the updated transaction
        Transaction updatedTransaction = new Transaction(
                "TRX002", "bank_new", 260.00, "completed", dateTimeSdf.parse("2024-07-20 12:00:00"));

        Transaction returnedTransaction = transactionDAO.updateTransaction(updatedTransaction);

        assertNotNull(returnedTransaction);
        assertEquals("bank_new", returnedTransaction.getType());

        Transaction fetchedTransaction = transactionDAO.getTransactionById("TRX002");
        assertNotNull(fetchedTransaction);
        assertEquals("bank_new", fetchedTransaction.getType());

        //Non-existing ID returns null and the list size is unchanged
        int initialSize = TransactionDAOImpl.transactionsList.size();
        Transaction nonExistingTransaction = new Transaction(
                "NONEXISTENT", "test", 0.0, "test", dateTimeSdf.parse("2024-01-01 00:00:00"));

        Transaction nonExistingReturned = transactionDAO.updateTransaction(nonExistingTransaction);
        assertNull(nonExistingReturned);
        assertEquals(initialSize, TransactionDAOImpl.transactionsList.size());

        //Null transaction input returns null and the list size is unchanged
        Transaction nullReturned = transactionDAO.updateTransaction(null);
        assertNull(nullReturned);
        assertEquals(initialSize, TransactionDAOImpl.transactionsList.size());

        //Update with a valid transaction object but a null ID returns null
        Transaction transactionWithNullId = new Transaction(
                null, "test_type", 123.45, "test_status", dateTimeSdf.parse("2024-01-01 00:00:00"));

        Transaction returnedWithNullId = transactionDAO.updateTransaction(transactionWithNullId);
        assertNull(returnedWithNullId);
        assertEquals(initialSize, TransactionDAOImpl.transactionsList.size());
    }

    

    /**
     * Helper method to write initial data to the CSV file.
     * This centralizes file-writing logic and improves the readability of the setup method.
     *
     * @throws IOException if the file cannot be written.
     */
    private void writeInitialCsvData() throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            writer.write(INITIAL_CSV_DATA);
        }
    }


    /**
     * Helper method to clear the content of the CSV file.
     *
     * @throws IOException if the file cannot be cleared.
     */
    private void clearCsvFile() throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            writer.write("");
        }
    }

    

    /**
     * Helper method to reload the static transactions list from the CSV file.
     * This ensures the DAO starts with a known state for each test.
     */
    private void reloadTransactionsListFromCsv() {
        TransactionDAOImpl.transactionsList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String transactionId = details[0];
                String type = details[1];
                Double amount = Double.parseDouble(details[2]);
                String status = details[3];
                Date date = dateTimeSdf.parse(details[4]);
                TransactionDAOImpl.transactionsList.add(new Transaction(transactionId, type, amount, status, date));
            }
        } catch (IOException | ParseException e) {
            // Fail the test immediately if there is a problem parsing the setup data.
            // This prevents false positives by ensuring a correct initial state.
            fail("Failed to parse initial CSV data during setup: " + e.getMessage());
        }
    }
}