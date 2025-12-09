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
            Contact.createContact("Charlie Wilson", "555-0105"),
            Contact.createContact("David Lee", "555-0106"),
            Contact.createContact("Emma Davis", "555-0107"),
            Contact.createContact("Frank Miller", "555-0108"),
            Contact.createContact("Grace Taylor", "555-0109"),
            Contact.createContact("Henry Clark", "555-0110")

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
     * Returns predefined contact lists based on the requested test scenario
     * such as duplicate entries, update testing, boundary validation,
     * special character input, or performance dataset generation.
     */
    public static List<Contact> getContactsForScenario(String scenario) {
        switch (scenario.toLowerCase()) {
            case "duplicate":
                return Arrays.asList(
                        Contact.createContact("Duplicate User", "555-1001"),
                        Contact.createContact("Duplicate User", "555-1002"), // Same name
                        Contact.createContact("Unique User", "555-1003")

                );

            case "update":
                return Arrays.asList(
                        Contact.createContact("Update User", "555-2001"),
                        Contact.createContact("Update Candidate New", "555-2002")


                );

            case "boundary":
                return Arrays.asList(
                        Contact.createContact("A", "1"),
                        Contact.createContact("Normal User", "555-1234"),
                        Contact.createContact("Very Long Name Here For Testing Boundary Conditions", "555-1234567890")
                );


            case "specialchars":
                return Arrays.asList(
                        Contact.createContact("User-O'Conner", "555-3001"),
                        Contact.createContact("User & Partner", "555-3002"),
                        Contact.createContact("User@Company", "555-3003")
                );

            case "performance":
                return ExcelDataReader.getContactsForPerformanceTest(100);

            default:
                return getContactsFromExcel();
        }
    }


    /**
     * Provides a list of intentionally invalid contacts
     * used for negative validation testing.
     */
    public static List<Contact> getInvalidContacts() {
        return Arrays.asList(
                Contact.createContact("", "555-0001"),
                Contact.createContact("Valid Name", ""),
                Contact.createContact("  ", "555-0002"),
                Contact.createContact("Valid Name", "  ")
        );
    }

    /**
     * Returns a mixed dataset containing both valid and invalid contacts
     * to simulate real-world inconsistent test data.
     */
    public static List<Contact> getMixedValidityContacts() {
        return Arrays.asList(
                Contact.createContact("Valid User 1", "555-4001"),
                Contact.createContact("", "555-4002"),
                Contact.createContact("Valid User 2", "555-4003"),
                Contact.createContact("Valid User 3", ""),
                Contact.createContact("Valid User 4", "555-4004")
        );
    }

    /**
     * Retrieves a single contact by index from the available dataset.
     * Performs bounds validation before access.
     */
    public static Contact getContact(int index) {
        List<Contact> contacts = getContactsFromExcel();

        if (index < 0) {
            throw new IllegalArgumentException("Contact index cannot be negative: " + index);
        }

        if (index >= contacts.size()) {
            throw new IllegalArgumentException(
                    "Contact index out of bounds: " + index +
                            ". Available contacts: " + contacts.size()
            );
        }

        return contacts.get(index);
    }

    /**
     * Returns a sublist of contacts within the specified index range.
     * Used for batch or partial dataset testing.
     */
    public static List<Contact> getContactRange(int start, int end) {
        List<Contact> contacts = getContactsFromExcel();

        if (start < 0 || end >= contacts.size() || start > end) {
            throw new IllegalArgumentException(
                    "Invalid range: start=" + start + ", end=" + end +
                            ", available contacts=" + contacts.size()
            );
        }

        return contacts.subList(start, end + 1);
    }

    /**
     * Provides human-readable information about the current test data source,
     * including origin (Excel or default) and sample content.
     */
    public static String getDataSourceInfo() {
        List<Contact> excelContacts = ExcelDataReader.readContactsFromExcel();

        StringBuilder info = new StringBuilder();
        info.append("=== TEST DATA SOURCE INFORMATION ===\n");

        if (excelContacts.isEmpty()) {
            info.append("Primary Source: DEFAULT (Excel not available)\n");
            info.append("Contact Count: ").append(DEFAULT_CONTACTS.size()).append("\n");
            info.append("Sample: ").append(DEFAULT_CONTACTS.get(0).getName());
        } else {
            info.append("Primary Source: EXCEL\n");
            info.append("Contact Count: ").append(excelContacts.size()).append("\n");
            info.append("Sample: ").append(excelContacts.get(0).getName());
        }

        return info.toString();
    }

}