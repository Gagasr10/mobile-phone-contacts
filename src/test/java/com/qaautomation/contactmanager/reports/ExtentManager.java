package com.qaautomation.contactmanager.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extent Reports manager for profesional test reporting
 */

public class ExtentManager {

    private static ExtentReports extent ;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if(extent == null){
            createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance () {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String reportPath = "test-output/html-report/extent-report-" + timeStamp + ".html";

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);

        //Profesional report configuration
        htmlReporter.config().setDocumentTitle("Mobile Phone Contacts - Test Report");
        htmlReporter.config().setReportName("QA Automation Test Suite");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        //System information
        extent.setSystemInfo("Organization", "QA Automation Portfolio");
        extent.setSystemInfo("Automation Engineer", "Dragan Stojilkovic");
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Application", "Mobile Phone Contacts Management");

        return extent;
    }

    public static ExtentTest createTest(String testName, String description){
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTestRunner(ExtentTest testRunner) {
        test.set(testRunner);
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }


}
