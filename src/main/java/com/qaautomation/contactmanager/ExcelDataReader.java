package com.qaautomation.contactmanager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataReader {

    // Default path to the Excel file containing test data
    private static final String DEFAULT_EXCEL_PATH = "test-data/test-contacts.xlsx";

    /**
     * Reads contacts from the default Excel file.
     */
    public static List<Contact> readContactsFromExcel() {
        return readContactsFromExcel(DEFAULT_EXCEL_PATH);
    }

    /**
     * Reads contacts from the Excel file at the specified file path.
     */
    public static List<Contact> readContactsFromExcel(String filePath) {
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

            System.out.println("Loaded " + contacts.size() + " contacts from: " + filePath);

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
                double numericValue = cell.getNumericCellValue();
                // Convert integer-like values (e.g., 123.0) to "123"
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            default:
                return null;
        }
    }
}
