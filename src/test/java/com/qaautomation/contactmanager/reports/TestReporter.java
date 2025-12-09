package com.qaautomation.contactmanager.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TestReporter {

    private static final Map<String, Long> testTimers = new HashMap<>();
    private static final Map<String, List<String>> testDataStore = new HashMap<>();

    public static void logInfo(String message) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info(message);
        } else {
            System.out.println("[INFO] " + message);
        }
    }

    public static void logPass(String message) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.pass(message);
        } else {
            System.out.println("[PASS] " + message);
        }
    }

    public static void logFail(String message) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.fail(message);
        } else {
            System.out.println("[FAIL] " + message);
        }
    }

    public static void logWarning(String message) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.warning(message);
        } else {
            System.out.println("[WARN] " + message);
        }
    }

    public static void startTest(String testName) {
        logInfo("Starting Test: " + testName);
        testTimers.put(testName, System.currentTimeMillis());
    }

    public static void endTest(String testName) {
        Long startTime = testTimers.get(testName);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            logInfo("Test '" + testName + "' completed in " + executionTime + "ms");
            testTimers.remove(testName);
        }
    }

    public static void logStep(String stepDescription) {
        ExtentTest test = ExtentManager.getTest();
        String step = "STEP: " + stepDescription;

        if (test != null) {
            test.info(step);
        } else {
            System.out.println(step);
        }
    }

    public static void logTestData(String dataType, Object data) {
        ExtentTest test = ExtentManager.getTest();

        if (test != null) {
            String formattedData;

            if (data instanceof List) {
                formattedData = formatListAsTable((List<?>) data);
                test.info(MarkupHelper.createCodeBlock(dataType + ":\n" + formattedData));
            } else if (data instanceof Map) {
                formattedData = formatMapAsTable((Map<?, ?>) data);
                test.info(MarkupHelper.createCodeBlock(dataType + ":\n" + formattedData));
            } else {
                formattedData = dataType + ": " + data.toString();
                test.info(formattedData);
            }
        } else {
            System.out.println(dataType + ": " + data.toString());
        }
    }

    public static void logDataComparison(String expected, String actual, boolean success) {
        ExtentTest test = ExtentManager.getTest();
        String comparison = String.format(
                "DATA COMPARISON:\n" +
                        "   Expected: %s\n" +
                        "   Actual:   %s\n" +
                        "   Result:   %s",
                expected, actual, success ? "MATCH" : "MISMATCH"
        );

        if (test != null) {
            if (success) {
                test.pass(MarkupHelper.createCodeBlock(comparison));
            } else {
                test.fail(MarkupHelper.createCodeBlock(comparison));
            }
        } else {
            System.out.println(comparison);
        }
    }

    public static void logPerformanceMetric(String operation, long timeMs) {
        String performanceLevel;
        if (timeMs < 100) {
            performanceLevel = "EXCELLENT";
        } else if (timeMs < 500) {
            performanceLevel = "GOOD";
        } else if (timeMs < 1000) {
            performanceLevel = "ACCEPTABLE";
        } else {
            performanceLevel = "SLOW";
        }

        String metric = String.format(
                "PERFORMANCE METRIC: %s\n" +
                        "   Time: %d ms\n" +
                        "   Rating: %s",
                operation, timeMs, performanceLevel
        );

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info(MarkupHelper.createCodeBlock(metric));
        } else {
            System.out.println(metric);
        }
    }

    public static void logValidationResult(String validation, boolean success, String details) {
        String result = String.format(
                "VALIDATION: %s\n" +
                        "   Result: %s\n" +
                        "   Details: %s",
                validation,
                success ? "PASS" : "FAIL",
                details
        );

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            if (success) {
                test.pass(MarkupHelper.createCodeBlock(result));
            } else {
                test.fail(MarkupHelper.createCodeBlock(result));
            }
        } else {
            System.out.println(result);
        }
    }

    public static void logScenarioSetup(String scenario) {
        String setup = "SCENARIO SETUP: " + scenario;
        ExtentTest test = ExtentManager.getTest();

        if (test != null) {
            test.info(MarkupHelper.createLabel(setup, com.aventstack.extentreports.markuputils.ExtentColor.BLUE));
        } else {
            System.out.println("\n" + setup + "\n" + "=".repeat(50));
        }
    }

    public static void logDataIteration(int iteration, String data) {
        String iterationLog = String.format("ITERATION %d: %s", iteration, data);
        logInfo(iterationLog);
    }

    public static void storeTestData(String key, String... data) {
        testDataStore.put(key, Arrays.asList(data));
    }

    public static List<String> getTestData(String key) {
        return testDataStore.get(key);
    }

    public static void logScreenshot(String screenshotName) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info("Screenshot captured: " + screenshotName);
        }
    }

    public static void logEnvironmentDetails() {
        Map<String, String> envDetails = new HashMap<>();
        envDetails.put("Java Version", System.getProperty("java.version"));
        envDetails.put("OS", System.getProperty("os.name"));
        envDetails.put("OS Version", System.getProperty("os.version"));
        envDetails.put("User", System.getProperty("user.name"));

        logTestData("Environment Details", envDetails);
    }

    public static void logTestSummary(int totalTests, int passed, int failed, int skipped) {
        String summary = String.format(
                "\nTEST SUMMARY\n" +
                        "   Total Tests: %d\n" +
                        "   Passed: %d\n" +
                        "   Failed: %d\n" +
                        "   Skipped: %d\n" +
                        "   Success Rate: %.1f%%",
                totalTests, passed, failed, skipped,
                (totalTests > 0 ? (passed * 100.0 / totalTests) : 0)
        );

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info(MarkupHelper.createCodeBlock(summary));
        } else {
            System.out.println(summary);
        }
    }

    private static String formatListAsTable(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "Empty List";
        }

        StringBuilder table = new StringBuilder();
        table.append("Index | Value\n");
        table.append("------|------\n");

        for (int i = 0; i < Math.min(10, list.size()); i++) {
            table.append(String.format("%5d | %s\n", i, list.get(i)));
        }

        if (list.size() > 10) {
            table.append(String.format("... and %d more items\n", list.size() - 10));
        }

        return table.toString();
    }

    private static String formatMapAsTable(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return "Empty Map";
        }

        StringBuilder table = new StringBuilder();
        table.append("Key | Value\n");
        table.append("----|------\n");

        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (count++ >= 10) break;
            table.append(String.format("%s | %s\n", entry.getKey(), entry.getValue()));
        }

        if (map.size() > 10) {
            table.append(String.format("... and %d more entries\n", map.size() - 10));
        }

        return table.toString();
    }
}
