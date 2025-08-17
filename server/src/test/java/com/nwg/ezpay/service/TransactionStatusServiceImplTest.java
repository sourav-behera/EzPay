package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.TransactionStatus;
import com.nwg.ezpay.exception.InvalidDateFormatException;
import com.nwg.ezpay.exception.InvalidRangeException;
import com.nwg.ezpay.exception.InvalidTransactionStatusException;
import com.nwg.ezpay.exception.InvalidTransactionStatusIdException;
import com.nwg.ezpay.exception.InvalidTransactionStatusObjectException;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
     * Tests the getStatusByIdService method with valid, non-existing, and null ID inputs.
     * Verifies correct object is returned for valid ID and appropriate exceptions are thrown otherwise.
     */
    @Test
    @DisplayName("Get status by ID - handles valid, non-existing, and null ID cases")
    void testGetStatusById() {
    	
        // Valid ID
        assertDoesNotThrow(() -> {
            TransactionStatus status = transactionStatusService.getStatusByIdService("TS001");
            assertNotNull(status);
            assertEquals("TS001", status.getTransactionStatusId());
        });

        // Non-existing ID
        assertThrows(InvalidTransactionStatusIdException.class, () ->
            transactionStatusService.getStatusByIdService("TS999")
        );

        // Null ID
        assertThrows(InvalidTransactionStatusIdException.class, () ->
            transactionStatusService.getStatusByIdService(null)
        );
    }
    
    
    
    
    /**
     * Tests the getStatusesByTypeService method with valid and null type inputs.
     * Verifies correct list is returned for valid type and exception is thrown for null input.
     */
    @Test
    @DisplayName("Get statuses by type - handles valid and null type cases")
    void testGetStatusesByType() {
    	
    	//Valid Type
        assertDoesNotThrow(() -> {
            List<TransactionStatus> list = transactionStatusService.getStatusesByTypeService("completed");
            assertEquals(2, list.size());
            assertTrue(list.stream().allMatch(status -> "completed".equals(status.getStatusType())));
        });
        
        //Null Input
        assertThrows(InvalidTransactionStatusException.class, () ->
            transactionStatusService.getStatusesByTypeService(null)
        );
    }
    
    
    
    /**
     * Tests the getStatusesByReasonService method with valid and empty reason inputs.
     * Verifies correct list is returned for valid reason and exception is thrown for empty input.
     */
    @Test
    @DisplayName("Get statuses by reason - handles valid and empty reason cases")
    void testGetStatusesByReason() {
    	
    	//Valid Reason
        assertDoesNotThrow(() -> {
            List<TransactionStatus> list = transactionStatusService.getStatusesByReasonService("Success");
            assertEquals(1, list.size());
        });
        
        //Null Input
        assertThrows(InvalidTransactionStatusException.class, () ->
            transactionStatusService.getStatusesByReasonService("")
        );
    }
    
    
    
    /**
     * Tests the getStatusesByDateService method with valid and null date inputs.
     * Verifies correct list is returned for a valid date and exception is thrown for null input.
     */
    @Test
    @DisplayName("Get statuses by date - handles valid and null date cases")
    void testGetStatusesByDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JULY, 29);
        
        //Valid date
        assertDoesNotThrow(() -> {
            List<TransactionStatus> list = transactionStatusService.getStatusesByDateService(calendar.getTime());
            assertEquals(2, list.size());
        });
        
        //Null Date
        assertThrows(InvalidDateFormatException.class, () ->
            transactionStatusService.getStatusesByDateService(null)
        );
    }
    
    
    
    
    /**
     * Tests the getStatusesByDateRangeService method with valid and invalid date ranges.
     * Verifies correct list is returned for valid range and exception is thrown for invalid range.
     */
    @Test
    @DisplayName("Get statuses by date range - handles valid and invalid range cases")
    void testGetStatusesByDateRange() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateValid = dateFormat.parse("2025-07-28");
        Date endDateValid = dateFormat.parse("2025-07-29");
        
        //Valid Date Range
        assertDoesNotThrow(() -> {
            List<TransactionStatus> list = transactionStatusService.getStatusesByDateRangeService(startDateValid, endDateValid);
            assertEquals(4, list.size());
        });

        Date startDateInvalid = dateFormat.parse("2025-07-30");
        Date endDateInvalid = dateFormat.parse("2025-07-29");
        
        //Invalid Date Range
        assertThrows(InvalidRangeException.class, () ->
            transactionStatusService.getStatusesByDateRangeService(startDateInvalid, endDateInvalid)
        );
    }
    
    
    
    /**
     * Tests the createStatusService method with valid and null inputs.
     * Verifies successful creation for a valid object and exception for null input.
     */
    @Test
    @DisplayName("Create transaction status - handles valid and null object cases")
    void testCreateStatus() {
    	
    	//Valid object creation
        assertDoesNotThrow(() -> {
            TransactionStatus newStatus = new TransactionStatus("TS005", "pending", "Awaiting approval", new Date());
            TransactionStatus created = transactionStatusService.createStatusService(newStatus);
            assertNotNull(created);
            assertEquals("TS005", created.getTransactionStatusId());
        });
        
        //Null object
        assertThrows(InvalidTransactionStatusObjectException.class, () ->
            transactionStatusService.createStatusService(null)
        );
    }
    
    
    
    /**
     * Tests the updateStatusService method with valid and null inputs.
     * Verifies update is successful for a valid object and exception is thrown for null input.
     */
    @Test
    @DisplayName("Update transaction status - handles valid and null object cases")
    void testUpdateStatus() {
    	
    	//Valid update 
        assertDoesNotThrow(() -> {
            TransactionStatus updatedStatus = new TransactionStatus("TS003", "completed", "Auto-resolved", new Date());
            TransactionStatus result = transactionStatusService.updateStatusService(updatedStatus);
            assertNotNull(result);
            assertEquals("completed", result.getStatusType());
        });
        
        //Null 
        assertThrows(InvalidTransactionStatusObjectException.class, () ->
            transactionStatusService.updateStatusService(null)
        );
    }
    
    
    
    /**
     * Tests the deleteStatusService method with existing and non-existing ID inputs.
     * Verifies successful deletion for an existing ID and exception for a non-existing ID.
     */
    @Test
    @DisplayName("Delete transaction status - handles existing and non-existing ID cases")
    void testDeleteStatus() {
    	
    	//Valid delete operation 
        assertDoesNotThrow(() -> {
            boolean deleted = transactionStatusService.deleteStatusService("TS004");
            assertTrue(deleted);
        });
        
        //Invalid ID delete operation
        assertThrows(InvalidTransactionStatusIdException.class, () ->
            transactionStatusService.deleteStatusService("TS999")
        );
    }
    
}
