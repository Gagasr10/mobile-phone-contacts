package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.ExcelDataReader;
import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.data.DataValidator;
import com.qaautomation.contactmanager.reports.ExtentManager;
import com.qaautomation.contactmanager.reports.TestReporter;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataDrivenTest {

    private MobilePhone phone;
    private static ExtentTest parentTest;
    private List<Contact> testContacts;

    @BeforeAll
    public static void setUpClass() {
        parentTest = ExtentManager.createTest(
                "Data-Driven Contact Tests",
                "Comprehensive data-driven testing using Excel test data"
        );
        TestReporter.logInfo("Data-Driven Test Suite initialized");
        TestReporter.logInfo("Data Source: " + TestDataProvider.getDataSourceInfo());
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        phone = new MobilePhone("061-999888");
        testContacts = TestDataProvider.getContactsFromExcel();

        ExtentTest test = parentTest.createNode(testInfo.getDisplayName());
        ExtentManager.setTestRunner(test);

        TestReporter.logInfo("Loaded " + testContacts.size() + " test contacts from external source");
        if (!testContacts.isEmpty()) {
            TestReporter.logTestData("Sample Contact", testContacts.get(0));
        }
    }

    @AfterEach
    public void tearDown() {
        TestReporter.logInfo("Test completed. Final contact count: " + phone.getContactCount());
    }

    @AfterAll
    public static void tearDownClass() {
        TestReporter.logInfo("Data-Driven test suite completed");
        ExtentManager.flushReport();
    }

    private static Stream<Contact> contactDataProvider() {
        return TestDataProvider.getContactsFromExcel().stream();
    }

    @ParameterizedTest(name = "Data-Driven Add: {0}")
    @DisplayName("DD001: Parameterized Contact Addition from Excel")
    @MethodSource("contactDataProvider")
    public void testAddContact_DataDriven(Contact contact) {
        TestReporter.startTest("Data-Driven Contact Addition: " + contact.getName());
        TestReporter.logDataIteration(1, contact.toString());

        DataValidator.ValidationResult validation = DataValidator.validateContact(contact);
        TestReporter.logValidationResult("Contact Validation", validation.isValid(), validation.getMessage());

        assertTrue(validation.isValid(), "Contact data should be valid: " + validation.getMessage());

        TestReporter.logStep("Adding contact from Excel data");
        long startTime = System.currentTimeMillis();
        boolean result = phone.addNewContact(contact);
        long operationTime = System.currentTimeMillis() - startTime;

        TestReporter.logPerformanceMetric("Add Contact", operationTime);

        assertTrue(result, "Should successfully add contact: " + contact.getName());
        TestReporter.logPass("Successfully added contact: " + contact.getName());

        TestReporter.logStep("Verifying contact persistence");
        Contact retrieved = phone.queryContact(contact.getName());
        assertNotNull(retrieved, "Added contact should be retrievable");

        TestReporter.logDataComparison(contact.getPhoneNumber(), retrieved.getPhoneNumber(),
                contact.getPhoneNumber().equals(retrieved.getPhoneNumber()));
    }

    @Test
    @DisplayName("DD002: Bulk Operations with Excel Data")
    public void testBulkContactOperations_ExcelData() {
        TestReporter.startTest("Bulk Contact Operations with Excel Data");

        TestReporter.logInfo("Starting bulk operations with " + testContacts.size() + " contacts from Excel");
        if (!testContacts.isEmpty()) {
            TestReporter.logTestData("Contact Sample",
                    testContacts.subList(0, Math.min(3, testContacts.size())));
        }

        TestReporter.logStep("Performing bulk add operation");
        long startTime = System.currentTimeMillis();
        int addedCount = phone.addAllContacts(testContacts);
        long bulkAddTime = System.currentTimeMillis() - startTime;

        TestReporter.logPerformanceMetric("Bulk Add " + testContacts.size() + " contacts", bulkAddTime);
        TestReporter.logPass("Successfully added " + addedCount + " out of " + testContacts.size() + " contacts");

        TestReporter.logStep("Verifying all contacts are retrievable");
        int retrievableCount = 0;
        startTime = System.currentTimeMillis();

        for (Contact contact : testContacts) {
            if (phone.queryContact(contact.getName()) != null) {
                retrievableCount++;
            }
        }

        long bulkQueryTime = System.currentTimeMillis() - startTime;
        TestReporter.logPerformanceMetric("Bulk Query " + testContacts.size() + " contacts", bulkQueryTime);

        TestReporter.logValidationResult("Contact Retrieval Validation",
                retrievableCount == addedCount,
                retrievableCount + " out of " + addedCount + " contacts retrievable");

        assertEquals(addedCount, retrievableCount, "All added contacts should be retrievable");
        TestReporter.logInfo("Final contact count: " + phone.getContactCount());
    }

    @ParameterizedTest
    @DisplayName("DD003: Data-Driven Error Scenarios")
    @MethodSource("invalidContactDataProvider")
    public void testDataDrivenErrorScenarios(Contact invalidContact) {
        TestReporter.startTest("Data-Driven Error Scenario: " +
                (invalidContact.getName().isEmpty() ? "Empty Name" : invalidContact.getName()));

        TestReporter.logStep("Attempting to add invalid contact");
        boolean result = phone.addNewContact(invalidContact);

        assertFalse(result, "Should reject invalid contact: " + invalidContact);
        TestReporter.logPass("Invalid contact correctly rejected: " + invalidContact);

        TestReporter.logStep("Verifying invalid contact is not in system");
        Contact retrieved = phone.queryContact(invalidContact.getName());
        assertNull(retrieved, "Invalid contact should not be retrievable");
    }

    private static Stream<Contact> invalidContactDataProvider() {
        return TestDataProvider.getInvalidContacts().stream();
    }


}


