package com.qaautomation.contactmanager;

import com.qaautomation.contactmanager.data.TestDataProvider;
import com.qaautomation.contactmanager.data.DataValidator;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("MOBILE PHONE CONTACTS MANAGER");
        System.out.println("=============================\n");

        MobilePhone phone = new MobilePhone("061-123456");
        System.out.println("Created Mobile Phone: " + phone.getMyNumber() + "\n");

        demonstrateBasicOperations(phone);
        demonstrateDataDrivenFeatures(phone);
        demonstrateAdvancedFeatures(phone);
        demonstrateDataValidation();

        System.out.println("\nDemo Completed Successfully!");
        System.out.println("Final Statistics:");
        System.out.println("   - Phone Number: " + phone.getMyNumber());
        System.out.println("   - Total Contacts: " + phone.getContactCount());
    }

    private static void demonstrateBasicOperations(MobilePhone phone) {
        System.out.println("=== DEMO 1: BASIC CONTACT OPERATIONS ===");

        Contact contact1 = Contact.createContact("John Doe", "555-0101");
        Contact contact2 = Contact.createContact("Jane Smith", "555-0102");

        System.out.println("Adding contacts...");
        phone.addNewContact(contact1);
        phone.addNewContact(contact2);
        phone.printContacts();

        System.out.println("\nUpdating John Doe's phone number...");
        Contact updatedJohn = Contact.createContact("John Doe", "555-9999");
        phone.updateContact(contact1, updatedJohn);
        phone.printContacts();

        System.out.println("\nQuerying contact...");
        Contact found = phone.queryContact("Jane Smith");
        System.out.println("Found: " + (found != null ? found.getName() + " -> " + found.getPhoneNumber() : "Not found"));

        System.out.println("\nRemoving contact...");
        phone.removeContact(contact2);
        phone.printContacts();
        System.out.println();
    }

    private static void demonstrateDataDrivenFeatures(MobilePhone phone) {
        System.out.println("=== DEMO 2: DATA-DRIVEN FEATURES ===");

        System.out.println("Loading contacts from Excel...");
        List<Contact> excelContacts = TestDataProvider.getContactsFromExcel();

        System.out.println("Loaded " + excelContacts.size() + " contacts from external source");

        System.out.println("Adding all contacts to phone...");
        int added = phone.addAllContacts(excelContacts);
        System.out.println("Successfully added " + added + " contacts");

        System.out.println("\nSample of added contacts:");
        for (int i = 0; i < Math.min(5, excelContacts.size()); i++) {
            Contact contact = excelContacts.get(i);
            System.out.println("   " + (i + 1) + ". " + contact.getName() + " -> " + contact.getPhoneNumber());
        }

        System.out.println("Total contacts in phone: " + phone.getContactCount() + "\n");
    }

    private static void demonstrateAdvancedFeatures(MobilePhone phone) {
        System.out.println("=== DEMO 3: ADVANCED FEATURES ===");

        System.out.println("Testing bulk operations...");

        List<Contact> duplicateContacts = TestDataProvider.getContactsForScenario("duplicate");
        List<Contact> boundaryContacts = TestDataProvider.getContactsForScenario("boundary");

        System.out.println("Adding duplicate scenario contacts...");
        int duplicateAdded = phone.addAllContacts(duplicateContacts);
        System.out.println("   Added " + duplicateAdded + " out of " + duplicateContacts.size() + " (duplicates handled)");

        System.out.println("Adding boundary scenario contacts...");
        int boundaryAdded = phone.addAllContacts(boundaryContacts);
        System.out.println("   Added " + boundaryAdded + " out of " + boundaryContacts.size() + " (boundary cases handled)");

        System.out.println("Current contact count: " + phone.getContactCount());

        System.out.println("\nTesting contact existence...");
        System.out.println("   'Duplicate User' exists: " + phone.contactExists("Duplicate User"));
        System.out.println("   'NonExistent User' exists: " + phone.contactExists("NonExistent User"));

        System.out.println();
    }

    private static void demonstrateDataValidation() {
        System.out.println("=== DEMO 4: DATA VALIDATION ===");

        Contact validContact = Contact.createContact("Valid User", "555-1234");
        DataValidator.ValidationResult validResult = DataValidator.validateContact(validContact);
        System.out.println("Valid contact test:");
        System.out.println("   Contact: " + validContact);
        System.out.println("   Validation: " + (validResult.isValid() ? "PASS" : "FAIL"));
        System.out.println("   Message: " + validResult.getMessage());

        Contact invalidContact = Contact.createContact("", "555-1234");
        DataValidator.ValidationResult invalidResult = DataValidator.validateContact(invalidContact);
        System.out.println("\nInvalid contact test:");
        System.out.println("   Contact: " + invalidContact);
        System.out.println("   Validation: " + (invalidResult.isValid() ? "PASS" : "FAIL"));
        System.out.println("   Message: " + invalidResult.getMessage());

        System.out.println();
    }
}