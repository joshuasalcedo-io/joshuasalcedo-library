package io.joshuasalcedo.pretty.core.model.error;

import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Demonstrates the enhanced stack trace formatting capabilities.
 */
public class EnhancedStackTraceDemo {

    public static void main(String[] args) {
        showBasicEnhancedStackTrace();
        System.out.println("\n" + "=".repeat(80) + "\n");
        showCompleteExceptionReport();
        System.out.println("\n" + "=".repeat(80) + "\n");
        showWrappedStandardException();
        System.out.println("\n" + "=".repeat(80) + "\n");
        showNestedExceptions();
    }

    /**
     * Demonstrates a basic enhanced stack trace.
     */
    private static void showBasicEnhancedStackTrace() {
        System.out.println(TerminalStyle.UI_HEADER.apply("Basic Enhanced Stack Trace"));
        System.out.println(TerminalStyle.UI_SUBHEADER.apply("Showing a simple exception with colored formatting\n"));

        try {
            // Create and throw an enhanced exception
            throw new EnhancedThrowable("Something went wrong in the application");
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Demonstrates a complete exception report with context and solutions.
     */
    private static void showCompleteExceptionReport() {
        System.out.println(TerminalStyle.UI_HEADER.apply("Complete Exception Report"));
        System.out.println(TerminalStyle.UI_SUBHEADER.apply("Showing a comprehensive error report with context\n"));

        try {
            // Generate a more context-specific exception
            throw new EnhancedThrowable("Configuration file not found: config.properties");
        } catch (EnhancedThrowable e) {
            // Create context map
            Map<String, Object> context = new HashMap<>();
            context.put("User", "admin");
            context.put("Operation", "Loading configuration");
            context.put("Config Path", "/etc/app/config.properties");
            context.put("Last Modified", "Never (file not found)");
            
            // Print comprehensive report
            e.printExceptionReport(System.out, context);
        }
    }

    /**
     * Demonstrates wrapping a standard exception with EnhancedThrowable.
     */
    private static void showWrappedStandardException() {
        System.out.println(TerminalStyle.UI_HEADER.apply("Wrapped Standard Exception"));
        System.out.println(TerminalStyle.UI_SUBHEADER.apply("Showing how to enhance display of standard exceptions\n"));

        try {
            // A regular Java exception
            throw new FileNotFoundException("Critical system file missing: /var/log/app.log");
        } catch (FileNotFoundException e) {
            // Wrap it in an EnhancedThrowable
            EnhancedThrowable enhanced = EnhancedThrowable.from(e);
            enhanced.printStackTrace(System.out);
        }
    }

    /**
     * Demonstrates nested exceptions with cause chain.
     */
    private static void showNestedExceptions() {
        System.out.println(TerminalStyle.UI_HEADER.apply("Nested Exceptions"));
        System.out.println(TerminalStyle.UI_SUBHEADER.apply("Showing a chain of nested exceptions\n"));

        try {
            try {
                try {
                    // The root cause
                    throw new IOException("Network connection interrupted");
                } catch (IOException e) {
                    // Middle of the chain
                    throw new TimeoutException("Database operation timed out").initCause(e);
                }
            } catch (TimeoutException e) {
                // Top-level exception
                throw new EnhancedThrowable("Failed to process customer data", e)
                      .highlightPackage("java.io", TerminalStyle.NETWORK_ERROR)
                      .highlightPackage("java.util.concurrent", TerminalStyle.DB_WARNING);
            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Creates a deeply nested call stack to demonstrate package grouping.
     */
    static class DeepStackGenerator {
        public static void level1() throws EnhancedThrowable {
            level2();
        }
        
        private static void level2() throws EnhancedThrowable {
            level3();
        }
        
        private static void level3() throws EnhancedThrowable {
            try {
                File file = new File("nonexistent.txt");
                if (!file.exists()) {
                    throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                throw new EnhancedThrowable("Error in processing", e);
            }
        }
    }
}