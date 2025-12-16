package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.data.DataValidator;
import com.qaautomation.contactmanager.reports.ExtentManager;
import com.qaautomation.contactmanager.reports.TestReporter;
import com.aventstack.extentreports.ExtentTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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


    @Test
    @DisplayName("TC001: Add New Contact - Success Scenario")
    public void testAddNewConatact_Sucess() {
        TestReporter.startTest("Add New Contact - Success Scenario");
        Contact contact = TestDataProvider.getContact(0);
        TestReporter.logTestData("Test Contact", contact);

        TestReporter.logStep("Attempting to add new contact");
        boolean result = phone.addNewContact(contact);

        assertTrue(result, "Should successfully add new contact");
        TestReporter.logPass("Contact '" + contact.getName()+ "' added successfully");

        TestReporter.logStep("Verifying contact was added correctly");
        Contact retrieved = phone.queryContact(contact.getName());
        assertNotNull(retrieved, "Added contact should be retrievalble");
        TestReporter.logDataComparison(contact.getPhoneNumber(), retrieved.getPhoneNumber(),
                contact.getPhoneNumber().equals(retrieved.getPhoneNumber()));

        TestReporter.logInfo("Current contact count: " + phone.getContactCount());
    }


    @Test
    @DisplayName("TC002: Add Duplicate Contact - Failure Scenario")
    public void testAddDuplicateContact_Failure(){
        TestReporter.startTest("Add Duplicate Contact - Failure Scenario");

        Contact contact = TestDataProvider.getContact(0);
        TestReporter.logStep("Adding initial contact");
        phone.addNewContact(contact);

        TestReporter.logStep("Attempting to add duplicate contact");
        boolean result = phone.addNewContact(contact);

        assertFalse(result, "Should reject duplicate contact");
        TestReporter.logPass("Duplicate contact corectly rejected - data integrity maintained");

        TestReporter.logStep("Verifying original contact remains unchanged");
        Contact retrieved = phone.queryContact(contact.getName());
        assertNotNull(retrieved, "Original contact should still exist");
        assertEquals(contact.getPhoneNumber(), retrieved.getPhoneNumber(),
                "Original contact data should be preserved");
    }

    @Test
    @DisplayName("TC003: Update Contact - Success Scenario")
    public void testUpdateContact_Success() {
        TestReporter.startTest("Update Contact - Success Scenario");

        Contact originalContact = TestDataProvider.getContact(0);
        Contact updatedContact = Contact.createContact(originalContact.getName(), "77777777");

        TestReporter.logStep("Adding original contact");
        phone.addNewContact(originalContact);

        TestReporter.logStep("Updating contact phone number");
        boolean result = phone.updateContact(originalContact, updatedContact);

        assertTrue(result, "Should successfully update existing contact");
        TestReporter.logPass("Contact updated successfully with new phone number");

        TestReporter.logStep("Verifying the update");
        Contact retrieved = phone.queryContact(originalContact.getName());
        assertNotNull(retrieved, "Contact should still exist after update");
        assertEquals("77777777", retrieved.getPhoneNumber(), "Phone number should be updated");
        TestReporter.logDataComparison("77777777", retrieved.getPhoneNumber(),
                "77777777".equals(retrieved.getPhoneNumber()));
    }



    }


}
