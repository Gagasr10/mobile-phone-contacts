package com.qaautomation.contactmanager.data;

import com.qaautomation.contactmanager.Contact;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataReader {

    // Default path to the Excel file containing test data
    private static final String DEFAULT_EXCEL_PATH = "test-data/test-contacts.xlsx";
    private static final String BACKUP_EXCEL_PATH = "test-data/backup-contcats.xlsx";
    public static List<Contact> readContactsFromExcel() {
        return readContactsFromExcel(DEFAULT_EXCEL_PATH, BACKUP_EXCEL_PATH);
    }

    public static List<Contact> tryReadContactsFromExcel() {
        return tryReadContactsFromExcel(DEFAULT_EXCEL_PATH);
    }


    /**
     * Reads contacts from the default Excel file.
     */
    public static List<Contact> readConctctsFromExcel() {
        return readContactsFromExcel(DEFAULT_EXCEL_PATH, BACKUP_EXCEL_PATH);
    }

    /**
     * Reads contacts from the Excel file at the specified file path.
     */
    public static List<Contact> readContactsFromExcel (String primaryPath, String fallbackPath){
        List<Contact> contacts = tryReadContactsFromExcel(primaryPath);
        if(contacts.isEmpty() && fallbackPath != null){
            System.out.println("Primary Excel file is not available, trying fallback...");
            contacts = tryReadContactsFromExcel(fallbackPath);
        }
        if(contacts.isEmpty()){
            System.out.println("No Excel data is available, will use default test data");
        }
        return  contacts;
    }

    public static List<Contact> tryReadContactsFromExcel(String filePath) {
        List<Contact> contacts = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row (assumed to be the first row)
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            // Process all remaining rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Contact contact = createContactFromRow(row);
                if (contact != null) {
                    contacts.add(contact);
                }
            }

            System.out.println("Successfully loaded " + contacts.size() + " contacts from: " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading Excel file '" + filePath + "': " + e.getMessage());
        }

        return contacts;
    }

    /**
     * Creates a Contact object from a single Excel row.
     * Expected columns:
     *   column 0 -> name
     *   column 1 -> phone number
     */
    private static Contact createContactFromRow(Row row) {
        String name = getCellValueAsString(row.getCell(0));
        String phoneNumber = getCellValueAsString(row.getCell(1));

        // Basic validation: ignore empty or invalid rows
        if (name == null || name.trim().isEmpty()
                || phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }

        return Contact.createContact(name.trim(), phoneNumber.trim());
    }

    //// Checks whether a row can be considered a valid header row
    private static boolean isHeaderRow(Row row){
        if(row.getPhysicalNumberOfCells() < 2){
            return false;
        }
        Cell firstCell = row.getCell(0);
        Cell secondCell = row.getCell(1);

        if(firstCell == null || secondCell == null){
            return false;
        }
        String firstValue = getCellValueAsString(firstCell);
        String secondValue = getCellValueAsString(secondCell);

        return "name".equalsIgnoreCase(firstValue) &&
                "phone".equalsIgnoreCase(secondValue) ||
                "phone number".equalsIgnoreCase(secondValue) ||
                "phonenumber".equalsIgnoreCase(secondValue);
    }

    private static boolean isValidContactData(String name, String phoneNumber) {
        if (name == null || phoneNumber == null) {
            return false;
        }

        String trimmedName = name.trim();
        String trimmedPhone = phoneNumber.trim();

        return !trimmedName.isEmpty()
                && !trimmedPhone.isEmpty()
                && trimmedName.length() >= 1
                && trimmedName.length() <= 100
                && trimmedPhone.length() >= 1
                && trimmedPhone.length() <= 30;
    }


    /**
     * Converts an Excel cell value to a String, handling common cell types.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            default:
                return null;
        }

    }

    public static boolean isTestDataAvailable() {
        try {
            List<Contact> contacts = readContactsFromExcel();
            return !contacts.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTestDataInfo() {
        try {
            List<Contact> contacts = readContactsFromExcel();
            if (contacts.isEmpty()) {
                return "Excel Test Data: No valid contacts found";
            }

            StringBuilder info = new StringBuilder();
            info.append("Excel Test Data: ").append(contacts.size()).append(" contacts available\n");
            info.append("Sample contacts: ");
            for (int i = 0; i < Math.min(3, contacts.size()); i++) {
                info.append(contacts.get(i).getName());
                if (i < Math.min(3, contacts.size()) - 1) {
                    info.append(", ");
                }
            }
            return info.toString();
        } catch (Exception e) {
            return "Excel Test Data: Not available - " + e.getMessage();
        }
    }

    public static List<Contact> getContactsForPerformanceTest(int count) {
        List<Contact> allContacts = readContactsFromExcel();
        List<Contact> performanceContacts = new ArrayList<>();

        if (allContacts.isEmpty()) {
            for (int i = 0; i < count; i++) {
                performanceContacts.add(Contact.createContact("PerfUser" + i, "555-" + String.format("%04d", i)));
            }
        } else {
            for (int i = 0; i < count; i++) {
                Contact original = allContacts.get(i % allContacts.size());
                performanceContacts.add(Contact.createContact(
                        original.getName() + "_" + (i / allContacts.size() + 1),
                        original.getPhoneNumber()
                ));
            }
        }

        return performanceContacts;
    }


}

