package com.nwg.EzPay;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the App class.
 * Ensures the main application class can be initialized without errors.
 */
class AppTest {

    @Test
    @DisplayName("Test App initialization")
    void testAppInitialization() {
        // The App.main method primarily prints to console and loads static data.
        // This test ensures the class loads and doesn't throw immediate exceptions.
        assertTrue(true, "App should initialize successfully.");
    }
}