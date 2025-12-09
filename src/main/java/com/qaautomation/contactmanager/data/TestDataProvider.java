package com.qaautomation.contactmanager.data;

import com.qaautomation.contactmanager.Contact;

import java.util.Arrays;
import java.util.List;

/**
 * Test Data Provider for contact management system
 * Provides test data for various scenarios
 */
public class TestDataProvider {

    // Default test contacts for fallback
    private static final List<Contact> DEFAULT_CONTACTS = Arrays.asList(
            Contact.createContact("John Doe", "555-0101"),
            Contact.createContact("Jane Smith", "555-0102"),
            Contact.createContact("Bob Johnson", "555-0103"),
            Contact.createContact("Alice Brown", "555-0104"),
            Contact.createContact("Charlie Wilson", "555-0105")
    );

    /**
     * Provides contacts from Excel file (primary data source)
     * Falls back to default contacts if Excel is not available
     */
    public static List<Contact> getContactsFromExcel() {
        List<Contact> excelContacts = ExcelDataReader.tryReadContactsFromExcel();
        return excelContacts.isEmpty() ? DEFAULT_CONTACTS : excelContacts;
    }

    /**
     * Provides contacts for specific test scenarios
     */
    public static List<Contact> getContactsForScenario(String scenario) {
        switch (scenario.toLowerCase()) {
            case "duplicate":
                return Arrays.asList(
                        Contact.createContact("Duplicate User", "555-1001"),
                        Contact.createContact("Duplicate User", "555-1002") // Same name
                );

            case "update":
                return Arrays.asList(
                        Contact.createContact("Update User", "555-2001")
                );

            case "boundary":
                return Arrays.asList(
                        Contact.createContact("A", "1"), // Minimal
                        Contact.createContact("Very Long Name For Testing", "555-9999") // Max
                );

            default:
                return getContactsFromExcel();
        }
    }

    /**
     * Provides invalid test data for negative testing
     */
    public static List<Contact> getInvalidContacts() {
        return Arrays.asList(
                Contact.createContact("", "555-0001"), // Empty name
                Contact.createContact("Valid Name", "")  // Empty phone
        );
    }

    /**
     * Gets basic contact by index
     */
    public static Contact getContact(int index) {
        List<Contact> contacts = getContactsFromExcel();

        if (index < 0 || index >= contacts.size()) {
            throw new IllegalArgumentException("Invalid contact index: " + index);
        }

        return contacts.get(index);
    }

    /**
     * Provides data source information
     */
    public static String getDataSourceInfo() {
        List<Contact> excelContacts = ExcelDataReader.tryReadContactsFromExcel();

        if (excelContacts.isEmpty()) {
            return "Using DEFAULT test data (" + DEFAULT_CONTACTS.size() + " contacts)";
        } else {
            return "Using EXCEL test data (" + excelContacts.size() + " contacts)";
        }
    }
}