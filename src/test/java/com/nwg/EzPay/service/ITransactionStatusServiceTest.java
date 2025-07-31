package com.nwg.ezpay.service;

import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionStatusIdException;
import com.nwg.ezpay.exception.InvalidTransactionStatusObjectException;
import com.nwg.ezpay.model.TransactionStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TransactionStatusServiceImpl} focusing on 
 * correctness and validation of business rules and data handling.
 * 
 * @author Param Shah
 * @version 0.0.1
 * @since 2025-07-29
 */
class TransactionStatusServiceTest {

    private TransactionStatusServiceImpl transactionStatusService;

    private static final String TEST_DATA =
        "TS001,completed,Success,2025-07-28 10:00:00\n" +
        "TS002,failed,Network issue,2025-07-28 12:30:00\n" +
        "TS003,pending,Awaiting confirmation,2025-07-29 09:45:00\n" +
        "TS004,completed,User confirmed,2025-07-29 14:00:00\n";

    @BeforeEach
    void setUp() throws IOException {
        try (FileWriter writer = new FileWriter("data/transaction_statuses.csv")) {
            writer.write(TEST_DATA);
        }
        transactionStatusService = new TransactionStatusServiceImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        try (FileWriter writer = new FileWriter("data/transaction_statuses.csv")) {
            writer.write(""); // clean up
        }
    }

    @Test
    @DisplayName("Get status by ID - valid existing ID")
    void testGetStatusById_Valid() throws InvalidTransactionStatusIdException {
        TransactionStatus transactionStatus = transactionStatusService.getStatusByIdService("TS001");
        assertNotNull(transactionStatus);
        assertEquals("TS001", transactionStatus.getTransactionId());
    }

    @Test
    @DisplayName("Get status by ID - non-existing ID throws exception")
    void testGetStatusById_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.getStatusByIdService("TS999");
        });
    }

    @Test
    @DisplayName("Get status by ID - null throws exception")
    void testGetStatusById_NullId() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.getStatusByIdService(null);
        });
    }

    @Test
    @DisplayName("Get statuses by type - valid")
    void testGetStatusesByType_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByTypeService("completed");
        assertEquals(2, transactionStatusList.size());
        assertTrue(transactionStatusList.stream().allMatch(status -> status.getStatusType().equals("completed")));
    }

    @Test
    @DisplayName("Get statuses by type - null throws exception")
    void testGetStatusesByType_Null() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionStatusService.getStatusesByTypeService(null);
        });
    }

    @Test
    @DisplayName("Get statuses by reason - valid")
    void testGetStatusesByReason_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByReasonService("Success");
        assertEquals(1, transactionStatusList.size());
    }

    @Test
    @DisplayName("Get statuses by reason - empty throws exception")
    void testGetStatusesByReason_Empty() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionStatusService.getStatusesByReasonService("");
        });
    }

    @Test
    @DisplayName("Get statuses by date - valid match")
    void testGetStatusesByDate_Valid() throws InvalidDateFormatException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JULY, 29);
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByDateService(calendar.getTime());
        assertEquals(2, transactionStatusList.size());
    }

    @Test
    @DisplayName("Get statuses by date - null throws exception")
    void testGetStatusesByDate_Null() {
        assertThrows(InvalidDateFormatException.class, () -> {
            transactionStatusService.getStatusesByDateService(null);
        });
    }

    @Test
    @DisplayName("Get statuses by date range - valid")
    void testGetStatusesByDateRange_Valid() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2025-07-28");
        Date endDate = dateFormat.parse("2025-07-29");
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByDateRangeService(startDate, endDate);
        assertEquals(4, transactionStatusList.size());
    }

    @Test
    @DisplayName("Get statuses by date range - invalid range throws exception")
    void testGetStatusesByDateRange_InvalidRange() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2025-07-30");
        Date endDate = dateFormat.parse("2025-07-29");

        assertThrows(InvalidRangeException.class, () -> {
            transactionStatusService.getStatusesByDateRangeService(startDate, endDate);
        });
    }

    @Test
    @DisplayName("Create new transaction status - valid object")
    void testCreateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus newTransactionStatus = new TransactionStatus("TS005", "pending", "Awaiting approval", new Date());
        TransactionStatus createdStatus = transactionStatusService.createStatusService(newTransactionStatus);
        assertNotNull(createdStatus);
        assertEquals("TS005", createdStatus.getTransactionId());
    }

    @Test
    @DisplayName("Create new transaction status - null object throws exception")
    void testCreateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            transactionStatusService.createStatusService(null);
        });
    }

    @Test
    @DisplayName("Update existing status - valid object")
    void testUpdateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus updatedTransactionStatus = new TransactionStatus("TS003", "completed", "Auto-resolved", new Date());
        TransactionStatus returnedStatus = transactionStatusService.updateStatusService(updatedTransactionStatus);
        assertNotNull(returnedStatus);
        assertEquals("completed", returnedStatus.getStatusType());
    }

    @Test
    @DisplayName("Update status - null object throws exception")
    void testUpdateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            transactionStatusService.updateStatusService(null);
        });
    }

    @Test
    @DisplayName("Delete status by ID - existing ID")
    void testDeleteStatus_Valid() throws InvalidTransactionStatusIdException {
        boolean deletionResult = transactionStatusService.deleteStatusService("TS004");
        assertTrue(deletionResult);
    }

    @Test
    @DisplayName("Delete status by ID - non-existing ID throws exception")
    void testDeleteStatus_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.deleteStatusService("TS999");
        });
    }
}