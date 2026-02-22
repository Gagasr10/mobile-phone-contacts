Mobile Phone Contacts Manager
An enterprise-ready Java application for managing contacts with comprehensive test automation and Excel data integration. This project demonstrates OOP principles, data validation, parameterized testing, and professional reporting using Extent Reports.

Table of Contents
Overview

Features

Technologies

Project Structure

Getting Started

Prerequisites

Installation

Running the Application

Usage

Testing

Running Tests

Test Reports

Test Suites

Data-Driven Testing

Performance Testing

Configuration

Contributing

License

Overview
The Mobile Phone Contacts Manager is a Java console application that allows users to manage contacts (add, update, delete, query). It reads contact data from an Excel file (test-data/test-contacts.xlsx) and provides fallback default contacts if the file is unavailable. The project is built with Maven and includes a robust test suite using JUnit 5, parameterized tests, and Extent Reports for professional HTML test reports.

This project is ideal for learning Java OOP, data-driven testing, and integration of external libraries like Apache POI and Extent Reports.

Features
Contact CRUD Operations: Create, read, update, and delete contacts.

Data Validation: Ensures contact names and phone numbers are non‑empty and within length limits.

Excel Integration: Load contacts from an Excel file (.xlsx) using Apache POI.

Fallback Default Data: If Excel file is missing, the application uses a predefined list of contacts.

Comprehensive Test Suite:

Unit tests for MobilePhone class.

Data-driven tests using Excel data.

Performance tests with timing assertions.

Parameterized tests for duplicate and boundary scenarios.

Professional Reporting: Extent Reports generates beautiful HTML reports with test details, steps, and performance metrics.

Maven Build: Easy dependency management and build automation.

Technologies
Java 11 – Core language

Maven – Build and dependency management

JUnit 5 – Testing framework

Extent Reports 5 – HTML reporting

Apache POI – Reading Excel files

Jackson – CSV/JSON support (optional)

Commons IO – File utilities

SLF4J – Logging

Project Structure
text
contactManager/
├── pom.xml
├── .gitignore
├── test-data/
│   └── test-contacts.xlsx          # Excel test data (name, phone)
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.qaautomation.contactmanager/
│   │           ├── Contact.java            # Contact entity
│   │           ├── MobilePhone.java        # Core contact management
│   │           ├── Main.java                # Console entry point
│   │           ├── data/
│   │           │   ├── DataValidator.java   # Validation logic
│   │           │   ├── ExcelDataReader.java # Excel reading
│   │           │   └── TestDataProvider.java # Provides test data
│   │           └── reports/
│   │               ├── ExtentManager.java   # ExtentReports setup
│   │               └── TestReporter.java    # Reporting utilities
│   └── test/
│       └── java/
│           └── com.qaautomation.contactmanager/
│               ├── DataDrivenTest.java      # Parameterized tests with Excel
│               ├── MobilePhoneTest.java     # Unit tests for MobilePhone
│               └── PerformanceTest.java     # Performance benchmarks
└── test-output/                             # Generated test reports
└── html-report/                          # Extent Reports HTML files
Getting Started
Prerequisites
Java 11 or higher

Maven 3.6+

Git (optional)

Installation
Clone the repository:

bash
git clone https://github.com/Gagasr10/contactManager.git
cd contactManager
Build the project and download dependencies:

bash
mvn clean compile
Running the Application
Run the main class directly using Maven:

bash
mvn exec:java -Dexec.mainClass="com.qaautomation.contactmanager.Main"
Or run from your favorite IDE by executing the Main class.

The console menu will appear:

text
MOBILE PHONE CONTACTS MANAGER
=============================

Created Mobile Phone: 061-123456

=== DEMO 1: BASIC CONTACT OPERATIONS ===
...
The application automatically loads contacts from test-data/test-contacts.xlsx (if available) and demonstrates CRUD operations, validation, and data-driven features.

Usage
The MobilePhone class provides the following public methods:

boolean addNewContact(Contact contact) – Adds a contact if not duplicate.

boolean updateContact(Contact oldContact, Contact newContact) – Updates an existing contact.

boolean removeContact(Contact contact) – Removes a contact.

Contact queryContact(String contactName) – Finds a contact by name.

void printContacts() – Prints all contacts.

int getContactCount() – Returns number of contacts.

int addAllContacts(List<Contact> contacts) – Bulk adds contacts.

boolean contactExists(String contactName) – Checks if a contact exists.

Testing
Running Tests
Execute all tests with Maven:

bash
mvn clean test
Test results are displayed in the console, and detailed HTML reports are generated in test-output/html-report/.

Test Reports
Extent Reports create a timestamped HTML file (e.g., extent-report-2026-02-22-15-30-45.html) containing:

Test suite overview

Pass/fail/skip counts

Detailed logs for each test

Performance metrics

Data tables for test inputs

Test Suites
Test Class	Description
MobilePhoneTest	Unit tests for all CRUD operations, validation, and edge cases.
DataDrivenTest	Parameterized tests that read contact data from Excel.
PerformanceTest	Measures execution time of bulk operations and queries with timeouts.
Data-Driven Testing
The project uses an Excel file located at test-data/test-contacts.xlsx as the primary data source. The file should contain two columns:

Name	Phone Number
John Doe	555-0101
Jane Smith	555-0102
...	...
If the file is missing, the TestDataProvider falls back to a set of default contacts.

The DataDrivenTest class uses JUnit's @MethodSource to feed each contact from Excel into a test method, ensuring comprehensive coverage with real-world data.

Performance Testing
Performance tests validate that the application can handle bulk operations efficiently:

Bulk Contact Addition: Measures time to add all contacts from Excel.

Contact Query: Measures time to retrieve each contact by name.

Tests include timeouts (e.g., 5 seconds) and assert that average time per operation stays within acceptable limits.

Configuration
Excel Path: The default Excel file path is test-data/test-contacts.xlsx. You can change it by modifying DEFAULT_EXCEL_PATH in ExcelDataReader.java.

Logging: SLF4J is configured to output simple logs to the console.

Report Output: HTML reports are saved in test-output/html-report/. You can change the output directory in ExtentManager.java.

Contributing
Contributions are welcome! If you find a bug or have a feature request, please open an issue or submit a pull request.

Fork the repository.

Create a feature branch: git checkout -b feature/my-feature

Commit your changes: git commit -am 'Add my feature'

Push to the branch: git push origin feature/my-feature

Open a pull request.

License
This project is licensed under the MIT License – see the LICENSE file for details.

Author: Dragan Stojilković
GitHub: @Gagasr10
Email: dragan.stojilkovic85@gmail.com

For any questions or feedback, feel free to reach out!