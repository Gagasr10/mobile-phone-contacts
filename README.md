Mobile Phone Contacts Manager

Enterprise-ready Java console application for managing contacts with comprehensive test automation, Excel data integration, and professional HTML reporting.

Project Overview

Mobile Phone Contacts Manager is a Java console application that allows users to manage contacts through full CRUD operations.

The application:

Reads contact data from Excel (test-data/test-contacts.xlsx)

Falls back to default contacts if the file is missing

Includes enterprise-level automated testing

Generates professional HTML reports using Extent Reports

This project demonstrates:

Clean OOP design

Data validation layer

Data-driven testing (Excel)

Parameterized and performance testing

Professional reporting with Extent Reports

Maven-based build system

Table of Contents

Features

Technologies

Project Structure

Getting Started

Usage

Testing

Data-Driven Testing

Performance Testing

Configuration

Contributing

License

Author

Features
Contact Management

Create contact

Update contact

Delete contact

Query contact

Bulk import contacts

Data Validation

Name cannot be empty

Phone number cannot be empty

Length validation

Duplicate prevention

Excel Integration

Reads .xlsx files using Apache POI

Fallback to predefined default data

Automated Testing

Unit tests (JUnit 5)

Parameterized tests

Data-driven tests

Performance tests

Professional HTML reporting

Reporting

Extent Reports 5

Timestamped HTML reports

Pass/Fail metrics

Performance metrics

Data tables

Technologies
Technology	Purpose
Java 11	Core language
Maven	Build and dependency management
JUnit 5	Testing framework
Extent Reports 5	HTML reporting
Apache POI	Excel reading
Jackson	CSV/JSON support (optional)
Commons IO	File utilities
SLF4J	Logging

# Mobile Phone Contacts Manager

Enterprise-ready Java console application for managing contacts with comprehensive test automation, Excel data integration, and professional HTML reporting.

## Project Overview

Mobile Phone Contacts Manager is a Java console application that allows users to manage contacts through full CRUD operations.

The application:
- Reads contact data from Excel (`test-data/test-contacts.xlsx`)
- Falls back to default contacts if the Excel file is missing
- Includes enterprise-level automated testing
- Generates professional HTML reports using Extent Reports

This project demonstrates:
- Clean OOP design
- Data validation layer
- Data-driven testing with Excel
- Parameterized and performance testing
- Professional reporting with Extent Reports
- Maven-based build system

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing](#testing)
- [Data-Driven Testing](#data-driven-testing)
- [Performance Testing](#performance-testing)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

## Project Structure

```bash
contactManager/
├── pom.xml
├── .gitignore
├── test-data/
│   └── test-contacts.xlsx
├── src/
│   ├── main/java/com/qaautomation/contactmanager/
│   │   ├── Contact.java
│   │   ├── MobilePhone.java
│   │   ├── Main.java
│   │   ├── data/
│   │   │   ├── DataValidator.java
│   │   │   ├── ExcelDataReader.java
│   │   │   └── TestDataProvider.java
│   │   └── reports/
│   │       ├── ExtentManager.java
│   │       └── TestReporter.java
│   └── test/java/com/qaautomation/contactmanager/
│       ├── DataDrivenTest.java
│       ├── MobilePhoneTest.java
│       └── PerformanceTest.java
└── test-output/
    └── html-report/

Getting Started
Prerequisites

Java 11 or higher

Maven 3.6+

Git (optional)

Installation
git clone https://github.com/Gagasr10/contactManager.git
cd contactManager
mvn clean compile
Running the Application
mvn exec:java -Dexec.mainClass="com.qaautomation.contactmanager.Main"

Or run Main.java directly from your IDE.

The application automatically loads contacts from test-data/test-contacts.xlsx (if available).

Usage
Public API – MobilePhone
boolean addNewContact(Contact contact);
boolean updateContact(Contact oldContact, Contact newContact);
boolean removeContact(Contact contact);
Contact queryContact(String contactName);
void printContacts();
int getContactCount();
int addAllContacts(List<Contact> contacts);
boolean contactExists(String contactName);
Testing
Run All Tests
mvn clean test

Reports are generated in:

test-output/html-report/
Test Reports

Extent Reports generate timestamped HTML files such as:

extent-report-YYYY-MM-DD-HH-MM-SS.html

Each report contains:

Suite summary

Pass/Fail/Skip statistics

Detailed logs

Performance metrics

Input data tables

Test Suites
Test Class	Description
MobilePhoneTest	CRUD operations and validation tests
DataDrivenTest	Excel-driven parameterized tests
PerformanceTest	Bulk operation and timing validation
Data-Driven Testing

Excel file location:

test-data/test-contacts.xlsx

Expected structure:

Name	Phone Number
John Doe	555-0101
Jane Smith	555-0102

If the file is missing, TestDataProvider falls back to default contacts.

DataDrivenTest uses JUnit @MethodSource to feed Excel data into parameterized tests.

Performance Testing

Validates:

Bulk contact addition speed

Contact query speed

Timeout limits (e.g., 5 seconds)

Average execution time per operation

Configuration
Configuration	Location
Excel Path	ExcelDataReader.java (DEFAULT_EXCEL_PATH)
Logging	SLF4J Console
Report Output	ExtentManager.java
Contributing
git checkout -b feature/my-feature
git commit -m "Add my feature"
git push origin feature/my-feature

Open a Pull Request.

License

This project is licensed under the MIT License – see the LICENSE file for details.

Author

Dragan Stojilković

GitHub: https://github.com/Gagasr10

LinkedIn: https://www.linkedin.com/in/dragan-stojilkovic-35aa8426a

Email: dragan.stojilkovic85@gmail.com
