package com.qaautomation.contactmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Mobile Phone contact management system
 * Demonstrates CRUD operations, collection management, and enterprise OOP principles
 */
public class MobilePhone {
    private final String myNumber;
    private final ArrayList<Contact> myContacts;

    public MobilePhone(String myNumber) {
        if (myNumber == null || myNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        this.myNumber = myNumber.trim();
        this.myContacts = new ArrayList<>();
    }

    public String getMyNumber() {
        return myNumber;
    }

    /**
     * Finds contact by Contact object
     * Returns position or -1 if not found
     */
    private int findContact(Contact contact) {
        for (int i = 0; i < myContacts.size(); i++) {
            Contact existingContact = myContacts.get(i);
            if (existingContact.getName().equalsIgnoreCase(contact.getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds contact by contact name (case-insensitive)
     * Returns position or -1 if not found
     */
    private int findContact(String contactName) {
        for (int i = 0; i < myContacts.size(); i++) {
            if (myContacts.get(i).getName().equalsIgnoreCase(contactName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds new contact if it doesn't exist
     * Returns true if added, false if duplicate
     */
    public boolean addNewContact(Contact contact) {
        if (findContact(contact) >= 0) {
            return false;
        }
        myContacts.add(contact);
        return true;
    }

    /**
     * Updates existing contact with new contact data
     * Returns true if updated, false if contact doesn't exist
     */
    public boolean updateContact(Contact oldContact, Contact newContact) {
        int position = findContact(oldContact);
        if (position >= 0) {
            myContacts.set(position, newContact);
            return true;
        }
        return false;
    }

    /**
     * Removes contact from the list
     * Returns true if removed, false if contact doesn't exist
     */
    public boolean removeContact(Contact contact) {
        int position = findContact(contact);
        if (position >= 0) {
            myContacts.remove(position);
            return true;
        }
        return false;
    }

    /**
     * Queries contact by name (case-insensitive)
     * Returns Contact object or null if not found
     */
    public Contact queryContact(String contactName) {
        int position = findContact(contactName);
        if (position >= 0) {
            return myContacts.get(position);
        }
        return null;
    }

    /**
     * Prints all contacts in formatted output
     */
    public void printContacts() {
        System.out.println("Contact List:");
        if (myContacts.isEmpty()) {
            System.out.println("  No contacts available");
            return;
        }

        for (int i = 0; i < myContacts.size(); i++) {
            Contact contact = myContacts.get(i);
            System.out.println((i + 1) + ". " + contact.getName() + " -> " + contact.getPhoneNumber());
        }
    }

    /**
     * Returns number of contacts
     */
    public int getContactCount() {
        return myContacts.size();
    }

    /**
     * Returns copy of all contacts for testing purposes
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(myContacts);
    }

    /**
     * Bulk add contacts from list
     * Returns number of successfully added contacts
     */
    public int addAllContacts(List<Contact> contacts) {
        int addedCount = 0;
        for (Contact contact : contacts) {
            if (addNewContact(contact)) {
                addedCount++;
            }
        }
        return addedCount;
    }

    /**
     * Clears all contacts
     */
    public void clearAllContacts() {
        myContacts.clear();
    }

    /**
     * Checks if contact exists by name
     */
    public boolean contactExists(String contactName) {
        return findContact(contactName) >= 0;
    }

    @Override
    public String toString() {
        return "MobilePhone{number='" + myNumber + "', contacts=" + myContacts.size() + "}";
    }
}
