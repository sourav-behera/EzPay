package com.nwg.EzPay.service;

import com.nwg.EzPay.exception.*;
import com.nwg.EzPay.model.TransactionStatus;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TransactionStatusServiceImpl} focusing on 
 * correctness and validation of business rules and data handling.
 *
 * @author Param Shah
 * @version 0.0.1
 * @since 2025-07-29
 */
class TransactionStatusServiceTest {

    private TransactionStatusServiceImpl service;
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
        service = new TransactionStatusServiceImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        try (FileWriter writer = new FileWriter("data/transaction_statuses.csv")) {
            writer.write(""); // clean up
        }
    }

    // --- Get By ID Tests ---

    @Test
    @DisplayName("Get status by ID - valid existing ID")
    void testGetStatusById_Valid() throws InvalidTransactionStatusIdException {
        TransactionStatus ts = service.getStatusByIdService("TS001");
        assertNotNull(ts);
        assertEquals("TS001", ts.getTransactionId());
    }

    @Test
    @DisplayName("Get status by ID - non-existing ID throws exception")
    void testGetStatusById_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            service.getStatusByIdService("TS999");
        });
    }

    @Test
    @DisplayName("Get status by ID - null throws exception")
    void testGetStatusById_NullId() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            service.getStatusByIdService(null);
        });
    }

    // --- Get By Type Tests ---

    @Test
    @DisplayName("Get statuses by type - valid")
    void testGetStatusesByType_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> statuses = service.getStatusesByTypeService("completed");
        assertEquals(2, statuses.size());
        assertTrue(statuses.stream().allMatch(ts -> ts.getStatusType().equals("completed")));
    }

    @Test
    @DisplayName("Get statuses by type - null throws exception")
    void testGetStatusesByType_Null() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            service.getStatusesByTypeService(null);
        });
    }

    // --- Get By Reason Tests ---

    @Test
    @DisplayName("Get statuses by reason - valid")
    void testGetStatusesByReason_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> statuses = service.getStatusesByReasonService("Success");
        assertEquals(1, statuses.size());
    }

    @Test
    @DisplayName("Get statuses by reason - empty throws exception")
    void testGetStatusesByReason_Empty() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            service.getStatusesByReasonService("");
        });
    }

    // --- Get By Date Tests ---

    @Test
    @DisplayName("Get statuses by date - valid match")
    void testGetStatusesByDate_Valid() throws InvalidDateFormatException {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JULY, 29); // 2025-07-29
        List<TransactionStatus> statuses = service.getStatusesByDateService(cal.getTime());
        assertEquals(2, statuses.size());
    }

    @Test
    @DisplayName("Get statuses by date - null throws exception")
    void testGetStatusesByDate_Null() {
        assertThrows(InvalidDateFormatException.class, () -> {
            service.getStatusesByDateService(null);
        });
    }

    // --- Get By Date Range Tests ---

    @Test
    @DisplayName("Get statuses by date range - valid")
    void testGetStatusesByDateRange_Valid() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse("2025-07-28");
        Date end = sdf.parse("2025-07-29");
        List<TransactionStatus> statuses = service.getStatusesByDateRangeService(start, end);
        assertEquals(4, statuses.size());
    }

    @Test
    @DisplayName("Get statuses by date range - invalid range throws exception")
    void testGetStatusesByDateRange_InvalidRange() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse("2025-07-30");
        Date end = sdf.parse("2025-07-29");

        assertThrows(InvalidRangeException.class, () -> {
            service.getStatusesByDateRangeService(start, end);
        });
    }

    // --- Create Tests ---

    @Test
    @DisplayName("Create new transaction status - valid object")
    void testCreateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus ts = new TransactionStatus("TS005", "pending", "Awaiting approval", new Date());
        TransactionStatus created = service.createStatusService(ts);
        assertNotNull(created);
        assertEquals("TS005", created.getTransactionId());
    }

    @Test
    @DisplayName("Create new transaction status - null object throws exception")
    void testCreateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            service.createStatusService(null);
        });
    }

    // --- Update Tests ---

    @Test
    @DisplayName("Update existing status - valid object")
    void testUpdateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus ts = new TransactionStatus("TS003", "completed", "Auto-resolved", new Date());
        TransactionStatus updated = service.updateStatusService(ts);
        assertNotNull(updated);
        assertEquals("completed", updated.getStatusType());
    }

    @Test
    @DisplayName("Update status - null object throws exception")
    void testUpdateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            service.updateStatusService(null);
        });
    }

    // --- Delete Tests ---

    @Test
    @DisplayName("Delete status by ID - existing ID")
    void testDeleteStatus_Valid() throws InvalidTransactionStatusIdException {
        boolean result = service.deleteStatusService("TS004");
        assertTrue(result);
    }

    @Test
    @DisplayName("Delete status by ID - non-existing ID throws exception")
    void testDeleteStatus_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            service.deleteStatusService("TS999");
        });
    }
}
