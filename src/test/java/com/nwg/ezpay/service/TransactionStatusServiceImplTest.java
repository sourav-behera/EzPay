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

class TransactionStatusServiceImplTest {
	
	  private TransactionStatusServiceImpl transactionStatusService;
	  private static final String CSV_FILE_PATH = "data/transaction_statuses.csv";

	    private static final String TEST_DATA =
	        "TS001,completed,Success,2025-07-28 10:00:00\n" +
	        "TS002,failed,Network issue,2025-07-28 12:30:00\n" +
	        "TS003,pending,Awaiting confirmation,2025-07-29 09:45:00\n" +
	        "TS004,completed,User confirmed,2025-07-29 14:00:00\n";

	    
	 /**
	 * Sets up a fresh testing environment before each test.
	 * This ensures test isolation by overwriting the CSV file and
	 * re-initializing the service.
	 *
	 * @throws IOException if there is an error writing to the CSV file.
	 */	
    @BeforeEach
    void setUp() throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            writer.write(TEST_DATA);
        }
        transactionStatusService = new TransactionStatusServiceImpl();
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
            writer.write(""); 
        }
    }
    
    
    

    /**
     * Tests retrieval of transaction status by a valid existing ID.
     * Verifies that the returned object is not null and has the correct ID.
     */
    @Test
    @DisplayName("Get status by ID - valid existing ID")
    void testGetStatusById_Valid() throws InvalidTransactionStatusIdException {
        TransactionStatus transactionStatus = transactionStatusService.getStatusByIdService("TS001");
        assertNotNull(transactionStatus);
        assertEquals("TS001", transactionStatus.getTransactionStatusId());
    }
    
    

    /**
     * Tests retrieval of transaction status using a non-existing ID.
     * Expects {@link InvalidTransactionStatusIdException}.
     */
    @Test
    @DisplayName("Get status by ID - non-existing ID throws exception")
    void testGetStatusById_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.getStatusByIdService("TS999");
        });
    }
    
    
    
    /**
     * Tests retrieval of transaction status using a null ID.
     * Expects {@link InvalidTransactionStatusIdException}.
     */
    @Test
    @DisplayName("Get status by ID - null throws exception")
    void testGetStatusById_NullId() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.getStatusByIdService(null);
        });
    }
    
    
    
    /**
     * Tests retrieval of all statuses matching a given type.
     * Expects a list with correct size and status type.
     */
    @Test
    @DisplayName("Get statuses by type - valid")
    void testGetStatusesByType_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByTypeService("completed");
        assertEquals(2, transactionStatusList.size());
        assertTrue(transactionStatusList.stream().allMatch(status -> status.getStatusType().equals("completed")));
    }
    
    
    
    /**
     * Tests behavior when querying statuses by a null type.
     * Expects {@link InvalidTransactionStatusException}.
     */
    @Test
    @DisplayName("Get statuses by type - null throws exception")
    void testGetStatusesByType_Null() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionStatusService.getStatusesByTypeService(null);
        });
    }
    
    
    
    /**
     * Tests retrieval of all statuses by a valid reason.
     * Verifies correct number of results.
     */
    @Test
    @DisplayName("Get statuses by reason - valid")
    void testGetStatusesByReason_Valid() throws InvalidTransactionStatusException {
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByReasonService("Success");
        assertEquals(1, transactionStatusList.size());
    }
    
    
    
    /**
     * Tests behavior when querying statuses by an empty reason string.
     * Expects {@link InvalidTransactionStatusException}.
     */
    @Test
    @DisplayName("Get statuses by reason - empty throws exception")
    void testGetStatusesByReason_Empty() {
        assertThrows(InvalidTransactionStatusException.class, () -> {
            transactionStatusService.getStatusesByReasonService("");
        });
    }
    
    
    
    /**
     * Tests retrieval of statuses by a specific valid date.
     * Verifies that correct entries are returned.
     */
    @Test
    @DisplayName("Get statuses by date - valid match")
    void testGetStatusesByDate_Valid() throws InvalidDateFormatException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JULY, 29);
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByDateService(calendar.getTime());
        assertEquals(2, transactionStatusList.size());
    }
    
    
    
    /**
     * Tests behavior when querying by a null date.
     * Expects {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Get statuses by date - null throws exception")
    void testGetStatusesByDate_Null() {
        assertThrows(InvalidDateFormatException.class, () -> {
            transactionStatusService.getStatusesByDateService(null);
        });
    }
    
    
    
    /**
     * Tests retrieval of transaction statuses within a valid date range.
     * Verifies that the number of returned results is as expected.
     */
    @Test
    @DisplayName("Get statuses by date range - valid")
    void testGetStatusesByDateRange_Valid() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2025-07-28");
        Date endDate = dateFormat.parse("2025-07-29");
        List<TransactionStatus> transactionStatusList = transactionStatusService.getStatusesByDateRangeService(startDate, endDate);
        assertEquals(4, transactionStatusList.size());
    }
    
    
    
    /**
     * Tests behavior when start date is after end date.
     * Expects {@link InvalidRangeException}.
     */
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
    
    
    
    /**
     * Tests creation of a valid transaction status.
     * Verifies that the returned object is non-null and has the correct ID.
     */
    @Test
    @DisplayName("Create new transaction status - valid object")
    void testCreateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus newTransactionStatus = new TransactionStatus("TS005", "pending", "Awaiting approval", new Date());
        TransactionStatus createdStatus = transactionStatusService.createStatusService(newTransactionStatus);
        assertNotNull(createdStatus);
        assertEquals("TS005", createdStatus.getTransactionStatusId());
    }
    
    
    
    /**
     * Tests behavior when creating a transaction status with a null object.
     * Expects {@link InvalidTransactionStatusObjectException}.
     */
    @Test
    @DisplayName("Create new transaction status - null object throws exception")
    void testCreateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            transactionStatusService.createStatusService(null);
        });
    }
    
    
    
    /**
     * Tests update of an existing transaction status.
     * Verifies that the status type is updated correctly.
     */
    @Test
    @DisplayName("Update existing status - valid object")
    void testUpdateStatus_Valid() throws InvalidTransactionStatusObjectException {
        TransactionStatus updatedTransactionStatus = new TransactionStatus("TS003", "completed", "Auto-resolved", new Date());
        TransactionStatus returnedStatus = transactionStatusService.updateStatusService(updatedTransactionStatus);
        assertNotNull(returnedStatus);
        assertEquals("completed", returnedStatus.getStatusType());
    }
    
    
    
    /**
     * Tests update of an existing transaction status.
     * Verifies that the status type is updated correctly.
     */
    @Test
    @DisplayName("Update status - null object throws exception")
    void testUpdateStatus_Null() {
        assertThrows(InvalidTransactionStatusObjectException.class, () -> {
            transactionStatusService.updateStatusService(null);
        });
    }
    
    
    
    /**
     * Tests deletion of an existing transaction status by ID.
     * Verifies the operation returns true.
     */
    @Test
    @DisplayName("Delete status by ID - existing ID")
    void testDeleteStatus_Valid() throws InvalidTransactionStatusIdException {
        boolean deletionResult = transactionStatusService.deleteStatusService("TS004");
        assertTrue(deletionResult);
    }
    
    
    
    /**
     * Tests behavior when attempting to delete a non-existing transaction status.
     * Expects {@link InvalidTransactionStatusIdException}.
     */
    @Test
    @DisplayName("Delete status by ID - non-existing ID throws exception")
    void testDeleteStatus_NonExisting() {
        assertThrows(InvalidTransactionStatusIdException.class, () -> {
            transactionStatusService.deleteStatusService("TS999");
        });
    }
    
}
