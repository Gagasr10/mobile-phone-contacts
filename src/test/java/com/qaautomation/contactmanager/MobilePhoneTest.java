package com.qaautomation.contactmanager;

import com.aventstack.extentreports.ExtentTest;
import com.qaautomation.contactmanager.reports.ExtentManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;

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
                "Comprehensive automation testing of contact management system",
                new String[]{ "Unit Tests", "Integration Tests", "Regression"}
        );
        TestReporter.logInfo
    }


}
