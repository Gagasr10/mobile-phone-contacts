package com.qaautomation.contactmanager.data;

import com.qaautomation.contactmanager.Contact;

public class DataValidator {

    /**
     * Validates a single contact
     */
    public static ValidationResult validateContact(Contact contact) {
        if (contact == null) {
            return new ValidationResult(false, "Contact cannot be null");
        }

        // Validate name
        String name = contact.getName();
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Contact name cannot be null or empty");
        }

        if (name.trim().length() < 1 || name.trim().length() > 100) {
            return new ValidationResult(false,
                    "Contact name must be between 1 and 100 characters: " + name);
        }

        // Validate phone number
        String phone = contact.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            return new ValidationResult(false, "Phone number cannot be null or empty");
        }

        if (phone.trim().length() < 1 || phone.trim().length() > 20) {
            return new ValidationResult(false,
                    "Phone number must be between 1 and 20 characters: " + phone);
        }

        return new ValidationResult(true, "Contact is valid");
    }

    // Simple validation result class
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }

}
