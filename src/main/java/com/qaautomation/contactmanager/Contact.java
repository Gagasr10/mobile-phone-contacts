package com.qaautomation.contactmanager;

public class Contact {

    private final String name;
    private final String phoneNumber;

    public Contact(String name, String phoneNumber) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact name cannot be null or empty");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        this.name = name.trim();
        this.phoneNumber = phoneNumber.trim();
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Static factory method for contact creation
     * Demonstrates factory pattern implementation
     */
    public static Contact createContact(String name, String phoneNumber) {
        return new Contact(name, phoneNumber);
    }

    @Override
    public String toString() {
        return "Contact{name='" + name + "', phoneNumber='" + phoneNumber + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return name.equals(contact.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
