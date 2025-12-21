package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.reports.ExtentManager;
import com.qaautomation.contactmanager.reports.TestReporter;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PerformanceTest {

    private MobilePhone phone;
    private static ExtentTest parentTest;

    @BeforeAll
    public static void setUpClass() {
        parentTest = ExtentManager.createTest(
                "Performance Tests",
                "Performance testing of contact management system"
        );
        TestReporter.logInfo("Performance Test Suite initialized");
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        phone = new MobilePhone("061-PERF-TEST");

        ExtentTest test = parentTest.createNode(testInfo.getDisplayName());
        ExtentManager.setTestRunner(test);

        TestReporter.logInfo("Initialized performance test environment");
    }

    @AfterEach
    public void tearDown() {
        TestReporter.logInfo("Performance test completed. Final stats: " + phone.getContactCount() + " contacts");
    }

    @AfterAll
    public static void tearDownClass() {
        TestReporter.logInfo("Performance test suite completed");
        ExtentManager.flushReport();
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("PERF001: Bulk Contact Addition Performance")
    public void testBulkContactAdditionPerformance() {
        TestReporter.startTest("Bulk Contact Addition Performance");

        List<Contact> contacts = TestDataProvider.getContactsFromExcel();
        TestReporter.logTestData("Dataset Size", contacts.size() + " contacts");

        TestReporter.logStep("Starting bulk addition performance test");
        long startTime = System.currentTimeMillis();

        int addedCount = phone.addAllContacts(contacts);

        long totalTime = System.currentTimeMillis() - startTime;
        double averageTimePerContact = (double) totalTime / contacts.size();

        TestReporter.logPerformanceMetric("Bulk Addition of " + contacts.size() + " contacts", totalTime);
        TestReporter.logInfo("Average time per contact: " + String.format("%.3f", averageTimePerContact) + " ms");
        TestReporter.logInfo("Contacts added: " + addedCount + "/" + contacts.size());

        assertTrue(totalTime < 5000, "Bulk addition should complete within 5 seconds");
        assertTrue(averageTimePerContact < 10, "Average time per contact should be less than 10ms");

        TestReporter.logPass("Bulk addition performance test passed");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("PERF002: Contact Query Performance")
    public void testContactQueryPerformance() {
        TestReporter.startTest("Contact Query Performance");

        List<Contact> contacts = TestDataProvider.getContactsFromExcel();
        phone.addAllContacts(contacts);
        TestReporter.logTestData("Pre-loaded Contacts", contacts.size());

        TestReporter.logStep("Starting query performance test");
        long startTime = System.currentTimeMillis();

        for (Contact contact : contacts) {
            Contact result = phone.queryContact(contact.getName());
            assertNotNull(result, "Contact should be found: " + contact.getName());
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double averageTimePerQuery = (double) totalTime / contacts.size();

        TestReporter.logPerformanceMetric("Query " + contacts.size() + " contacts", totalTime);
        TestReporter.logInfo("Average time per query: " + String.format("%.3f", averageTimePerQuery) + " ms");

        assertTrue(totalTime < 3000, "Querying should complete within 3 seconds");
        assertTrue(averageTimePerQuery < 5, "Average time per query should be less than 5ms");

        TestReporter.logPass("Query performance test passed");
    }



}

