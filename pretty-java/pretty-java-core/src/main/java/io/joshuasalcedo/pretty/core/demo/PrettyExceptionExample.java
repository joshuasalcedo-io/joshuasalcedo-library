package io.joshuasalcedo.pretty.core.demo;

import io.joshuasalcedo.pretty.core.model.error.PrettyException;
import io.joshuasalcedo.pretty.core.model.error.PrettyExceptionHtml;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PrettyExceptionExample {

    public static void main(String[] args) {
        try {
            // Simulate a deep exception chain
            try {
                // Inner exception
                try {
                    // Innermost exception
                    throw new IOException("Failed to read config file: config.json");
                } catch (IOException e) {
                    throw new IllegalStateException("Application initialization failed", e);
                }
            } catch (IllegalStateException e) {
                throw new PrettyException("Application startup failed", e);
            }
        } catch (Exception e) {
            // Print the standard pretty stack trace
            System.out.println("\n=== Standard Pretty Stack Trace ===\n");
            
            if (e instanceof PrettyException) {
                ((PrettyException) e).printStackTrace(System.out);
            } else {
                PrettyException prettyEx = PrettyException.from(e);
                prettyEx.printStackTrace(System.out);
            }
            
            // Generate HTML report
            try {
                Map<String, Object> context = new HashMap<>();
                context.put("Application Name", "MyAwesomeApp");
                context.put("Startup Phase", "Configuration Loading");
                context.put("Config Path", "/etc/myapp/config.json");
                context.put("User", "admin");
                
                // Generate HTML report and save to file
                Path reportPath = PrettyExceptionHtml.saveHtmlReport(e, context);
                
                System.out.println("\n=== HTML Exception Report ===");
                System.out.println("Report generated at: " + reportPath.toAbsolutePath());
                System.out.println("Open this file in your browser to view the report.");
                
                // Optionally try to open the file in the browser
                openInBrowser(reportPath);
            } catch (IOException ex) {
                System.err.println("Error saving HTML report: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Attempts to open a file in the default browser.
     * 
     * @param path The path to the file to open
     */
    private static void openInBrowser(Path path) {
        String osName = System.getProperty("os.name").toLowerCase();
        
        try {
            if (osName.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path.toAbsolutePath());
            } else if (osName.contains("mac")) {
                // macOS
                Runtime.getRuntime().exec("open " + path.toAbsolutePath());
            } else if (osName.contains("linux") || osName.contains("unix")) {
                // Linux/Unix
                Runtime.getRuntime().exec("xdg-open " + path.toAbsolutePath());
            } else {
                System.out.println("Could not detect OS for browser opening.");
            }
        } catch (IOException e) {
            System.err.println("Error opening browser: " + e.getMessage());
        }
    }
}