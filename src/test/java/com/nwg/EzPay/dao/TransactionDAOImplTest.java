package com.nwg.EzPay.dao;

import com.nwg.EzPay.model.Transaction;
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

import static org.junit.jupiter.api.Assertions.*;

class TransactionDAOImplTest {

    private ITransactionDAO transactionDAO;
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateOnlySdf = new SimpleDateFormat("yyyy-MM-dd");

    // Initial data for the CSV file, ensuring test isolation.
    private static final String INITIAL_CSV_DATA =
            "TRX001,upi,100.00,completed,2024-07-20 10:00:00\n" +
            "TRX002,bank,250.50,pending,2024-07-20 11:30:00\n" +
            "TRX003,upi,50.00,initiated,2024-07-21 09:00:00\n" +
            "TRX004,bank,150.75,completed,2024-07-21 14:45:00\n" +
            "TRX005,upi,300.00,completed,2024-07-22 16:00:00\n" +
            "TRX006,bank,10.00,failed,2024-07-22 17:00:00";

    @BeforeEach
    void setup() throws IOException {
        // Overwrite transactions.csv with a known set of data for each test.
        try (FileWriter writer = new FileWriter("data/transactions.csv")) {
            writer.write(INITIAL_CSV_DATA);
        }

        // Clear and reload the static transactions list in DAO for fresh state.
        TransactionDAOImpl.transactions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("data/transactions.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String transactionId = details[0];
                String type = details[1];
                Double amount = Double.parseDouble(details[2]);
                String status = details[3];
                Date date = dateTimeSdf.parse(details[4]);
                TransactionDAOImpl.transactions.add(new Transaction(transactionId, type, amount, status, date));
            }
        } catch (ParseException e) {
            fail("Failed to parse initial CSV data during setup: " + e.getMessage());
        }

        transactionDAO = new TransactionDAOImpl(); // Instantiate the DAO
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clear the static list after each test.
        TransactionDAOImpl.transactions.clear();
        // Clear the CSV file as well to ensure a clean slate.
        try (FileWriter writer = new FileWriter("data/transactions.csv")) {
            writer.write("");
        }
    }

    // --- getTransactionById Tests ---
    @Test
    @DisplayName("Get transaction by ID - existing ID returns transaction")
    void testGetTransactionById_ExistingId() {
        Transaction transaction = transactionDAO.getTransactionById("TRX001");
        assertNotNull(transaction);
        assertEquals("TRX001", transaction.getTransactionId());
    }

    @Test
    @DisplayName("Get transaction by ID - non-existing ID returns null")
    void testGetTransactionById_NonExistingId() {
        Transaction transaction = transactionDAO.getTransactionById("NONEXISTENT");
        assertNull(transaction);
    }

    @Test
    @DisplayName("Get transaction by ID - null ID returns null")
    void testGetTransactionById_NullId() {
        Transaction transaction = transactionDAO.getTransactionById(null);
        assertNull(transaction);
    }

    // --- getTransactionByType Tests ---
    @Test
    @DisplayName("Get transactions by type - existing type returns correct list")
    void testGetTransactionByType_ExistingType() {
        List<Transaction> upiTransactions = transactionDAO.getTransactionByType("upi");
        assertNotNull(upiTransactions);
        assertFalse(upiTransactions.isEmpty());
        assertEquals(3, upiTransactions.size());
        assertTrue(upiTransactions.stream().allMatch(t -> t.getType().equals("upi")));
    }

    @Test
    @DisplayName("Get transactions by type - non-existing type returns empty list")
    void testGetTransactionByType_NonExistingType() {
        List<Transaction> cryptoTransactions = transactionDAO.getTransactionByType("crypto");
        assertNotNull(cryptoTransactions);
        assertTrue(cryptoTransactions.isEmpty());
    }

    @Test
    @DisplayName("Get transactions by type - null type returns empty list")
    void testGetTransactionByType_NullType() {
        List<Transaction> nullTypeTransactions = transactionDAO.getTransactionByType(null);
        assertNotNull(nullTypeTransactions);
        assertTrue(nullTypeTransactions.isEmpty());
    }

    // --- getTransactionByStatus Tests ---
    @Test
    @DisplayName("Get transactions by status - existing status returns correct list")
    void testGetTransactionByStatus_ExistingStatus() {
        List<Transaction> completedTransactions = transactionDAO.getTransactionByStatus("completed");
        assertNotNull(completedTransactions);
        assertFalse(completedTransactions.isEmpty());
        assertEquals(3, completedTransactions.size());
        assertTrue(completedTransactions.stream().allMatch(t -> t.getStatus().equals("completed")));
    }

    @Test
    @DisplayName("Get transactions by status - non-existing status returns empty list")
    void testGetTransactionByStatus_NonExistingStatus() {
        List<Transaction> unknownStatusTransactions = transactionDAO.getTransactionByStatus("unknown");
        assertNotNull(unknownStatusTransactions);
        assertTrue(unknownStatusTransactions.isEmpty());
    }

    @Test
    @DisplayName("Get transactions by status - null status returns empty list")
    void testGetTransactionByStatus_NullStatus() {
        List<Transaction> nullStatusTransactions = transactionDAO.getTransactionByStatus(null);
        assertNotNull(nullStatusTransactions);
        assertTrue(nullStatusTransactions.isEmpty());
    }

    // --- getTransactionByDate Tests ---
    @Test
    @DisplayName("Get transactions by date - existing date returns correct list")
    void testGetTransactionByDate_ExistingDate() throws ParseException {
        Date date = dateOnlySdf.parse("2024-07-20");
        List<Transaction> transactionsOnDate = transactionDAO.getTransactionByDate(date);
        assertNotNull(transactionsOnDate);
        assertFalse(transactionsOnDate.isEmpty());
        assertEquals(2, transactionsOnDate.size());
        assertTrue(transactionsOnDate.stream().allMatch(t -> {
            try { return dateOnlySdf.parse(dateOnlySdf.format(t.getDate())).equals(date); }
            catch (ParseException e) { fail("Date parsing error in assertion"); return false; }
        }));
    }

    @Test
    @DisplayName("Get transactions by date - non-existing date returns empty list")
    void testGetTransactionByDate_NonExistingDate() throws ParseException {
        Date date = dateOnlySdf.parse("2025-01-01");
        List<Transaction> transactionsOnDate = transactionDAO.getTransactionByDate(date);
        assertNotNull(transactionsOnDate);
        assertTrue(transactionsOnDate.isEmpty());
    }

    @Test
    @DisplayName("Get transactions by date - null date returns null (as per DAO implementation)")
    void testGetTransactionByDate_NullDate() {
        List<Transaction> transactionsOnDate = transactionDAO.getTransactionByDate(null);
        assertNull(transactionsOnDate); // DAO returns null due to ParseException for null date
    }

    // --- getTransactionByDateRange Tests ---
    @Test
    @DisplayName("Get transactions by date range - valid range returns correct list")
    void testGetTransactionByDateRange_ValidRange() throws ParseException {
        Date startDate = dateOnlySdf.parse("2024-07-20");
        Date endDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByDateRange(startDate, endDate);
        assertNotNull(transactionsInRange);
        assertFalse(transactionsInRange.isEmpty());
        assertEquals(4, transactionsInRange.size());
    }

    @Test
    @DisplayName("Get transactions by date range - no transactions in range returns empty list")
    void testGetTransactionByDateRange_NoTransactionsInRange() throws ParseException {
        Date startDate = dateOnlySdf.parse("2025-01-01");
        Date endDate = dateOnlySdf.parse("2025-01-31");
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByDateRange(startDate, endDate);
        assertNotNull(transactionsInRange);
        assertTrue(transactionsInRange.isEmpty());
    }

    @Test
    @DisplayName("Get transactions by date range - null start date returns null")
    void testGetTransactionByDateRange_NullStartDate() throws ParseException {
        Date endDate = dateOnlySdf.parse("2024-07-21");
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByDateRange(null, endDate);
        assertNull(transactionsInRange);
    }

    @Test
    @DisplayName("Get transactions by date range - null end date returns null")
    void testGetTransactionByDateRange_NullEndDate() throws ParseException {
        Date startDate = dateOnlySdf.parse("2024-07-20");
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByDateRange(startDate, null);
        assertNull(transactionsInRange);
    }

    // --- getTransactionByAmountRange Tests ---
    @Test
    @DisplayName("Get transactions by amount range - valid range returns correct list")
    void testGetTransactionByAmountRange_ValidRange() {
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByAmountRange(50.00, 150.00);
        assertNotNull(transactionsInRange);
        assertFalse(transactionsInRange.isEmpty());
        assertEquals(2, transactionsInRange.size()); // TRX001 (100.00), TRX003 (50.00)
        assertTrue(transactionsInRange.stream().allMatch(t -> t.getAmount() >= 50.00 && t.getAmount() <= 150.00));
    }

    @Test
    @DisplayName("Get transactions by amount range - range with no transactions returns empty list")
    void testGetTransactionByAmountRange_EmptyRange() {
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByAmountRange(1000.00, 2000.00);
        assertNotNull(transactionsInRange);
        assertTrue(transactionsInRange.isEmpty());
    }

    @Test
    @DisplayName("Get transactions by amount range - start amount greater than end amount returns empty list")
    void testGetTransactionByAmountRange_StartGreaterThanEnd() {
        List<Transaction> transactionsInRange = transactionDAO.getTransactionByAmountRange(200.00, 100.00);
        assertNotNull(transactionsInRange);
        assertTrue(transactionsInRange.isEmpty());
    }

    // --- createTransaction Tests ---
    @Test
    @DisplayName("Create transaction - new valid transaction")
    void testCreateTransaction_NewValidTransaction() throws ParseException {
        int initialSize = TransactionDAOImpl.transactions.size();
        Transaction newTransaction = new Transaction("TRX007", "netbanking", 500.00, "completed", dateTimeSdf.parse("2024-07-23 10:00:00"));
        Transaction created = transactionDAO.createTransaction(newTransaction);

        assertNotNull(created);
        assertEquals(initialSize + 1, TransactionDAOImpl.transactions.size());
        assertEquals(newTransaction, created);
        assertTrue(TransactionDAOImpl.transactions.contains(newTransaction));
    }

    @Test
    @DisplayName("Create transaction - null transaction returns null")
    void testCreateTransaction_NullTransaction() {
        int initialSize = TransactionDAOImpl.transactions.size();
        Transaction created = transactionDAO.createTransaction(null);
        assertNull(created);
        assertEquals(initialSize, TransactionDAOImpl.transactions.size());
    }

    // --- deleteTransaction Tests ---
    @Test
    @DisplayName("Delete transaction - existing ID returns true and removes transaction")
    void testDeleteTransaction_ExistingId() {
        int initialSize = TransactionDAOImpl.transactions.size();
        boolean deleted = transactionDAO.deleteTransaction("TRX001");

        assertTrue(deleted);
        assertEquals(initialSize - 1, TransactionDAOImpl.transactions.size());
        assertNull(transactionDAO.getTransactionById("TRX001"));
    }

    @Test
    @DisplayName("Delete transaction - non-existing ID returns false and no change in size")
    void testDeleteTransaction_NonExistingId() {
        int initialSize = TransactionDAOImpl.transactions.size();
        boolean deleted = transactionDAO.deleteTransaction("NONEXISTENT");

        assertFalse(deleted);
        assertEquals(initialSize, TransactionDAOImpl.transactions.size());
    }

    @Test
    @DisplayName("Delete transaction - null ID returns false and no change in size")
    void testDeleteTransaction_NullId() {
        int initialSize = TransactionDAOImpl.transactions.size();
        boolean deleted = transactionDAO.deleteTransaction(null);
        assertFalse(deleted);
        assertEquals(initialSize, TransactionDAOImpl.transactions.size());
    }

    // --- updateTransaction Tests ---
    @Test
    @DisplayName("Update transaction - existing ID updates and returns updated transaction")
    void testUpdateTransaction_ExistingId() throws ParseException {
        Transaction updatedTransaction = new Transaction(
                "TRX002", "bank_new", 260.00, "completed", dateTimeSdf.parse("2024-07-20 12:00:00"));

        Transaction returnedTransaction = transactionDAO.updateTransaction(updatedTransaction);

        assertNotNull(returnedTransaction);
        assertEquals("bank_new", returnedTransaction.getType());

        Transaction fetchedTransaction = transactionDAO.getTransactionById("TRX002");
        assertNotNull(fetchedTransaction);
        assertEquals("bank_new", fetchedTransaction.getType());
    }

    @Test
    @DisplayName("Update transaction - non-existing ID returns null")
    void testUpdateTransaction_NonExistingId() throws ParseException {
        int initialSize = TransactionDAOImpl.transactions.size();
        Transaction nonExistingTransaction = new Transaction(
                "NONEXISTENT", "test", 0.0, "test", dateTimeSdf.parse("2024-01-01 00:00:00"));

        Transaction returnedTransaction = transactionDAO.updateTransaction(nonExistingTransaction);

        assertNull(returnedTransaction);
        assertEquals(initialSize, TransactionDAOImpl.transactions.size());
    }

    @Test
    @DisplayName("Update transaction - null transaction returns null")
    void testUpdateTransaction_NullTransaction() {
        int initialSize = TransactionDAOImpl.transactions.size();
        Transaction returnedTransaction = transactionDAO.updateTransaction(null);
        assertNull(returnedTransaction);
        assertEquals(initialSize, TransactionDAOImpl.transactions.size());
    }
}