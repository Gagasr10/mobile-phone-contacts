package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.data.DataValidator;
import com.qaautomation.contactmanager.reports.ExtentManager;
import com.qaautomation.contactmanager.reports.TestReporter;
import com.aventstack.extentreports.ExtentTest;

import org.junit.jupiter.api.*;

/**
 * Comprehensive test suite for MobilePhone class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class MobilePhoneTest {
    private MobilePhone phone;
    private static ExtentTest parentTest ;

    @BeforeAll
    public static void setUpClass() {
        parentTest = ExtentManager.createTest(
                "Mobile Phone Contacts - Complete Test Suite",
                "Comprehensive automation testing of contact management system"
        );
        TestReporter.logInfo("Test Suite initialized with Extent Reports");
        TestReporter.logInfo("Data Source: " + TestDataProvider.getDataSourceInfo());
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        phone = new MobilePhone("061-123456");

        ExtentTest test = parentTest.createNode(testInfo.getDisplayName());
        ExtentManager.setTestRunner(test);

        TestReporter.logInfo("Initialized MobilePhone instance: " + phone.toString());
    }

    @AfterEach
    public void tearDown() {
        TestReporter.logInfo("Test completed. Final contact count: " + phone.getContactCount());
    }

    @AfterAll
    public static void tearDownClass() {
        TestReporter.logInfo("All tests completed - Generating HTML report");
        ExtentManager.flushReport();
    }


}
