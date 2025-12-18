package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.data.DataValidator;
import com.qaautomation.contactmanager.reports.ExtentManager;
import com.qaautomation.contactmanager.reports.TestReporter;
import com.aventstack.extentreports.ExtentTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

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


    @ParameterizedTest
    @ValueSource(strings = {"duplicate", "boundary"})
    @DisplayName("TC004: Parameterized Contact Scenarios")
    public void testParameterizedContactScenarios(String scenario){
        TestReporter.startTest("Parametrized Test - Scenario: " + scenario);
        TestReporter.logScenarioSetup("Testing with scenario: " + scenario);

        List<Contact> scenarioContacts = TestDataProvider.getContactsForScenario(scenario);
        TestReporter.logTestData("Scenario Contacts", scenarioContacts.size() + " contacts");

        int addedCount = 0;
        for(int i = 0; i<scenarioContacts.size(); i++){
            Contact contact = scenarioContacts.get(i);
            TestReporter.logDataIteration(i+ 1, contact.getName());
            boolean result = phone.addNewContact(contact);
            if(result){
                addedCount++;
                TestReporter.logPass("Added: " + contact.getName());
            } else{
                TestReporter.logWarning("Skipped (duplicate): " + contact.getName());
            }
        }
        TestReporter.logInfo(("Successfully added " + addedCount + " out of " + scenarioContacts.size()
                + " contacts"));
        assertTrue(addedCount > 0, "Should add at least one contact in scenario: " + scenario);
    }

    @Test
    @DisplayName("TC005: Bulk Contact Operations")
    public void testBulkContactOperations() {
        TestReporter.startTest("Bulk Contact Operations");

        List<Contact> contacts = TestDataProvider.getContactsFromExcel();
        TestReporter.logTestData("Total contacts for bulk operations", contacts.size());

        TestReporter.logStep("Performing bulk add operation");
        long startTime = System.currentTimeMillis();
        int addedCount = phone.addAllContacts(contacts);
        long addTime = System.currentTimeMillis() - startTime;

        TestReporter.logPerformanceMetric("Bulk add of " + contacts.size() + " contacts", addTime);
        TestReporter.logInfo("Successfully added " + addedCount + " contacts");

        TestReporter.logStep("Verifying all added contacts are retrievable");
        int retrievableCount = 0;
        for (Contact contact : contacts) {
            if (phone.queryContact(contact.getName()) != null) {
                retrievableCount++;
            }
        }

        TestReporter.logValidationResult("Contact Retrieval",
                retrievableCount == addedCount,
                retrievableCount + " out of " + addedCount + " contacts retrievable");

        assertEquals(addedCount, retrievableCount, "All added contacts should be retrievable");
    }


    @Test
    @DisplayName("TC006: Contact Validation Integration")
    public void testContactValidationIntegration() {
        TestReporter.startTest("Contact Validation Integration");

        List<Contact> mixedContacts = Arrays.asList(
                TestDataProvider.getContact(0),
                Contact.createContact("", "555-0001"),
                TestDataProvider.getContact(1),
                Contact.createContact("Valid Name", "")
        );

        TestReporter.logTestData("Mixed validity contacts", mixedContacts.size());

        int validAdded = 0;
        int invalidSkipped = 0;

        for (Contact contact : mixedContacts) {
            DataValidator.ValidationResult validation = DataValidator.validateContact(contact);

            if (validation.isValid()) {
                boolean added = phone.addNewContact(contact);
                if (added) {
                    validAdded++;
                    TestReporter.logPass("Valid contact added: " + contact.getName());
                }
            } else {
                invalidSkipped++;
                TestReporter.logWarning("Invalid contact skipped: " + contact.getName() + " - " + validation.getMessage());
            }
        }

        TestReporter.logInfo("Results: " + validAdded + " valid added, " + invalidSkipped + " invalid skipped");
        assertTrue(validAdded > 0, "Should add valid contacts");
        assertTrue(invalidSkipped > 0, "Should skip invalid contacts");
    }

    @Test
    @DisplayName("TC007: Comprehensive Contact Lifecycle")
    public void testComprehensiveContactLifecycle() {
        TestReporter.startTest("Comprehensive Contact Lifecycle");

        Contact contact = TestDataProvider.getContact(0);
        TestReporter.logStep("Starting lifecycle test for: " + contact.getName());

        TestReporter.logStep("Phase 1 - Create");
        boolean createResult = phone.addNewContact(contact);
        assertTrue(createResult);
        TestReporter.logPass("Creation successful");

        TestReporter.logStep("Phase 2 - Read");
        Contact readContact = phone.queryContact(contact.getName());
        assertNotNull(readContact);
        TestReporter.logPass("Read successful");

        TestReporter.logStep("Phase 3 - Update");
        Contact updatedContact = Contact.createContact(contact.getName(), "99999999");
        boolean updateResult = phone.updateContact(contact, updatedContact);
        assertTrue(updateResult);
        TestReporter.logPass("Update successful");

        TestReporter.logStep("Phase 4 - Delete");
        boolean deleteResult = phone.removeContact(updatedContact);
        assertTrue(deleteResult);
        TestReporter.logPass("Delete successful");

        TestReporter.logStep("Phase 5 - Verify Deletion");
        Contact deletedContact = phone.queryContact(contact.getName());
        assertNull(deletedContact);
        TestReporter.logPass("Deletion verification successful");

        TestReporter.logPass("Complete contact lifecycle test passed - All CRUD operations working correctly");
    }




}



