package io.joshuasalcedo.pretty.core.model.error;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * Extends PrettyException with HTML reporting capabilities.
 * Generates visually appealing HTML representations of exception stack traces.
 */
public class PrettyExceptionHtml {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Generates an HTML representation of the exception.
     * 
     * @param throwable The throwable to generate HTML for
     * @param additionalContext Optional additional context information
     * @return A string containing the HTML representation
     */
    public static String generateHtml(Throwable throwable, Map<String, Object> additionalContext) {
        if (throwable == null) {
            return "<div class='exception-report'>No exception provided</div>";
        }
        
        StringBuilder html = new StringBuilder();
        
        // Current time for the report
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        
        // Start building the HTML with CSS styles
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>Exception Report: ").append(throwable.getClass().getSimpleName()).append("</title>\n")
            .append("    <style>\n")
            .append(getExceptionCss())
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("<div class=\"exception-container\">\n");
        
        // Add header section
        html.append("    <header class=\"exception-header\">\n")
            .append("        <div class=\"exception-title\">Exception Report</div>\n")
            .append("        <div class=\"exception-timestamp\">").append(timestamp).append("</div>\n")
            .append("    </header>\n");
        
        // Add exception type and message
        html.append("    <div class=\"exception-summary\">\n")
            .append("        <div class=\"exception-type\">").append(throwable.getClass().getName()).append("</div>\n")
            .append("        <div class=\"exception-message\">").append(escapeHtml(throwable.getMessage())).append("</div>\n")
            .append("    </div>\n");
        
        // Add environment information
        html.append("    <div class=\"exception-section\">\n")
            .append("        <div class=\"section-header\">Environment Information</div>\n")
            .append("        <div class=\"section-content\">\n")
            .append("            <table class=\"info-table\">\n")
            .append("                <tr><td>Java Version</td><td>").append(System.getProperty("java.version")).append("</td></tr>\n")
            .append("                <tr><td>OS Name</td><td>").append(System.getProperty("os.name")).append("</td></tr>\n")
            .append("                <tr><td>OS Version</td><td>").append(System.getProperty("os.version")).append("</td></tr>\n")
            .append("                <tr><td>OS Architecture</td><td>").append(System.getProperty("os.arch")).append("</td></tr>\n")
            .append("                <tr><td>User Directory</td><td>").append(System.getProperty("user.dir")).append("</td></tr>\n")
            .append("            </table>\n")
            .append("        </div>\n")
            .append("    </div>\n");
        
        // Add additional context if provided
        if (additionalContext != null && !additionalContext.isEmpty()) {
            html.append("    <div class=\"exception-section\">\n")
                .append("        <div class=\"section-header\">Additional Context</div>\n")
                .append("        <div class=\"section-content\">\n")
                .append("            <table class=\"info-table\">\n");
            
            for (Map.Entry<String, Object> entry : additionalContext.entrySet()) {
                html.append("                <tr><td>").append(escapeHtml(entry.getKey())).append("</td><td>")
                    .append(escapeHtml(String.valueOf(entry.getValue()))).append("</td></tr>\n");
            }
            
            html.append("            </table>\n")
                .append("        </div>\n")
                .append("    </div>\n");
        }
        
        // Add stack trace section
        html.append("    <div class=\"exception-section\">\n")
            .append("        <div class=\"section-header\">Stack Trace</div>\n")
            .append("        <div class=\"section-content\">\n")
            .append(formatStackTraceHtml(throwable))
            .append("        </div>\n")
            .append("    </div>\n");
        
        // Add potential solutions section
        html.append("    <div class=\"exception-section\">\n")
            .append("        <div class=\"section-header\">Suggested Actions</div>\n")
            .append("        <div class=\"section-content\">\n")
            .append("            <ul class=\"suggestion-list\">\n");
        
        // Add suggestions based on exception type
        for (String suggestion : getSuggestionsForException(throwable)) {
            html.append("                <li>").append(escapeHtml(suggestion)).append("</li>\n");
        }
        
        html.append("            </ul>\n")
            .append("        </div>\n")
            .append("    </div>\n");
        
        // Close container and document
        html.append("</div>\n</body>\n</html>");
        
        return html.toString();
    }
    
    /**
     * Formats the stack trace of a throwable as HTML.
     * 
     * @param throwable The throwable to format
     * @return The HTML representation of the stack trace
     */
    private static String formatStackTraceHtml(Throwable throwable) {
        StringBuilder stackTraceHtml = new StringBuilder();
        
        // Get the stack trace elements
        StackTraceElement[] stackElements = throwable.getStackTrace();
        List<StackTraceElementDTO> stackTraceElements = StackTraceElementMapper.toDtoList(stackElements);
        
        // Group frames by package
        Map<String, StringBuilder> packageGroups = new HashMap<>();
        
        for (int i = 0; i < stackTraceElements.size(); i++) {
            StackTraceElementDTO element = stackTraceElements.get(i);
            String className = element.getDeclaringClass();
            String packageName = className.contains(".") ?
                    className.substring(0, className.lastIndexOf('.')) : "";
            
            // Create a package group if it doesn't exist
            if (!packageGroups.containsKey(packageName)) {
                packageGroups.put(packageName, new StringBuilder());
            }
            
            // Add this frame to the package group
            StringBuilder group = packageGroups.get(packageName);
            
            // Format the stack trace element
            String shortClassName = className.substring(className.lastIndexOf('.') + 1);
            
            group.append("<div class=\"stack-frame")
                 .append(i < 5 ? " important-frame" : "")  // Highlight the first 5 frames
                 .append(isAppPackage(packageName) ? " app-frame" : "")  // Highlight app code
                 .append("\">\n")
                 .append("    <span class=\"frame-number\">").append(i).append("</span>\n")
                 .append("    <span class=\"frame-class\">").append(shortClassName).append("</span>.\n")
                 .append("    <span class=\"frame-method\">").append(element.getMethodName()).append("</span>\n");
            
            // Add source file information if available
            if (element.getFileName() != null) {
                group.append("    <span class=\"frame-location\">")
                     .append("(").append(element.getFileName());
                
                if (element.getLineNumber() >= 0) {
                    group.append(":").append(element.getLineNumber());
                }
                
                group.append(")</span>\n");
            } else if (element.isNativeMethod()) {
                group.append("    <span class=\"frame-location\">(Native Method)</span>\n");
            } else {
                group.append("    <span class=\"frame-location\">(Unknown Source)</span>\n");
            }
            
            group.append("</div>\n");
        }
        
        // Add all package groups to the stack trace HTML
        for (Map.Entry<String, StringBuilder> entry : packageGroups.entrySet()) {
            String packageName = entry.getKey();
            
            stackTraceHtml.append("<div class=\"package-group\">\n")
                          .append("    <div class=\"package-name\">").append(packageName).append("</div>\n")
                          .append("    <div class=\"package-frames\">\n")
                          .append(entry.getValue())
                          .append("    </div>\n")
                          .append("</div>\n");
        }
        
        // Add caused-by exceptions
        Throwable cause = throwable.getCause();
        if (cause != null) {
            stackTraceHtml.append("<div class=\"caused-by\">\n")
                          .append("    <div class=\"caused-by-header\">Caused by: ")
                          .append(cause.getClass().getName())
                          .append(cause.getMessage() != null ? ": " + escapeHtml(cause.getMessage()) : "")
                          .append("</div>\n")
                          .append(formatStackTraceHtml(cause))
                          .append("</div>\n");
        }
        
        return stackTraceHtml.toString();
    }
    
    /**
     * Generates CSS styles for the exception report.
     * 
     * @return A string containing CSS styles
     */
    private static String getExceptionCss() {
        return "body {\n" +
               "    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
               "    line-height: 1.6;\n" +
               "    color: #333;\n" +
               "    background-color: #f5f5f5;\n" +
               "    margin: 0;\n" +
               "    padding: 20px;\n" +
               "}\n" +
               ".exception-container {\n" +
               "    max-width: 1200px;\n" +
               "    margin: 0 auto;\n" +
               "    background-color: #fff;\n" +
               "    border-radius: 8px;\n" +
               "    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\n" +
               "    overflow: hidden;\n" +
               "}\n" +
               ".exception-header {\n" +
               "    background-color: #e53935;\n" +
               "    color: white;\n" +
               "    padding: 20px;\n" +
               "    display: flex;\n" +
               "    justify-content: space-between;\n" +
               "    align-items: center;\n" +
               "}\n" +
               ".exception-title {\n" +
               "    font-size: 24px;\n" +
               "    font-weight: bold;\n" +
               "}\n" +
               ".exception-timestamp {\n" +
               "    font-size: 14px;\n" +
               "    opacity: 0.9;\n" +
               "}\n" +
               ".exception-summary {\n" +
               "    padding: 20px;\n" +
               "    background-color: #ffebee;\n" +
               "    border-bottom: 1px solid #ffcdd2;\n" +
               "}\n" +
               ".exception-type {\n" +
               "    font-family: 'Fira Code', 'Source Code Pro', monospace;\n" +
               "    font-weight: bold;\n" +
               "    font-size: 18px;\n" +
               "    color: #b71c1c;\n" +
               "    margin-bottom: 5px;\n" +
               "}\n" +
               ".exception-message {\n" +
               "    font-size: 16px;\n" +
               "    word-break: break-word;\n" +
               "}\n" +
               ".exception-section {\n" +
               "    margin: 20px;\n" +
               "    border: 1px solid #e0e0e0;\n" +
               "    border-radius: 4px;\n" +
               "    overflow: hidden;\n" +
               "}\n" +
               ".section-header {\n" +
               "    background-color: #f5f5f5;\n" +
               "    padding: 10px 15px;\n" +
               "    font-weight: bold;\n" +
               "    border-bottom: 1px solid #e0e0e0;\n" +
               "}\n" +
               ".section-content {\n" +
               "    padding: 15px;\n" +
               "}\n" +
               ".info-table {\n" +
               "    width: 100%;\n" +
               "    border-collapse: collapse;\n" +
               "}\n" +
               ".info-table td {\n" +
               "    padding: 8px 12px;\n" +
               "    border-bottom: 1px solid #eee;\n" +
               "}\n" +
               ".info-table td:first-child {\n" +
               "    font-weight: bold;\n" +
               "    width: 180px;\n" +
               "}\n" +
               ".package-group {\n" +
               "    margin-bottom: 20px;\n" +
               "    border-left: 3px solid #2196F3;\n" +
               "    background-color: #f9f9f9;\n" +
               "    border-radius: 0 4px 4px 0;\n" +
               "}\n" +
               ".package-name {\n" +
               "    padding: 8px 15px;\n" +
               "    background-color: #e3f2fd;\n" +
               "    font-family: 'Fira Code', 'Source Code Pro', monospace;\n" +
               "    font-size: 14px;\n" +
               "    color: #0d47a1;\n" +
               "}\n" +
               ".package-frames {\n" +
               "    padding: 10px 15px;\n" +
               "}\n" +
               ".stack-frame {\n" +
               "    font-family: 'Fira Code', 'Source Code Pro', monospace;\n" +
               "    font-size: 14px;\n" +
               "    padding: 6px 10px;\n" +
               "    margin-bottom: 4px;\n" +
               "    border-radius: 3px;\n" +
               "    background-color: #f5f5f5;\n" +
               "}\n" +
               ".important-frame {\n" +
               "    background-color: #fff8e1;\n" +
               "    border-left: 3px solid #ffc107;\n" +
               "}\n" +
               ".app-frame {\n" +
               "    background-color: #e8f5e9;\n" +
               "    border-left: 3px solid #4caf50;\n" +
               "}\n" +
               ".frame-number {\n" +
               "    display: inline-block;\n" +
               "    min-width: 24px;\n" +
               "    text-align: right;\n" +
               "    margin-right: 10px;\n" +
               "    color: #757575;\n" +
               "}\n" +
               ".frame-class {\n" +
               "    font-weight: bold;\n" +
               "    color: #0288d1;\n" +
               "}\n" +
               ".frame-method {\n" +
               "    color: #d81b60;\n" +
               "}\n" +
               ".frame-location {\n" +
               "    color: #616161;\n" +
               "    margin-left: 10px;\n" +
               "}\n" +
               ".caused-by {\n" +
               "    margin-top: 15px;\n" +
               "    border-left: 3px solid #ff9800;\n" +
               "    padding-left: 15px;\n" +
               "}\n" +
               ".caused-by-header {\n" +
               "    padding: 8px 10px;\n" +
               "    background-color: #fff3e0;\n" +
               "    font-weight: bold;\n" +
               "    color: #e65100;\n" +
               "    border-radius: 3px;\n" +
               "    margin-bottom: 10px;\n" +
               "}\n" +
               ".suggestion-list {\n" +
               "    margin: 0;\n" +
               "    padding-left: 20px;\n" +
               "}\n" +
               ".suggestion-list li {\n" +
               "    margin-bottom: 8px;\n" +
               "}\n";
    }
    
    /**
     * Checks if a package name looks like an application package.
     * 
     * @param packageName The package name to check
     * @return true if the package is likely an application package
     */
    private static boolean isAppPackage(String packageName) {
        // Exclude common JDK and library packages
        if (packageName.startsWith("java.") ||
            packageName.startsWith("javax.") ||
            packageName.startsWith("sun.") ||
            packageName.startsWith("com.sun.") ||
            packageName.startsWith("org.w3c.") ||
            packageName.startsWith("org.xml.")) {
            return false;
        }
        
        // Include specific app packages (customize this for your app)
        if (packageName.startsWith("io.joshuasalcedo") || 
            packageName.startsWith("com.mycompany")) {
            return true;
        }
        
        // Heuristic: if it's not a common third-party library, assume it's app code
        if (packageName.startsWith("org.springframework") ||
            packageName.startsWith("org.hibernate") ||
            packageName.startsWith("com.google") ||
            packageName.startsWith("org.apache")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets suggested actions based on exception type.
     * 
     * @param throwable The throwable to get suggestions for
     * @return A list of suggestion strings
     */
    private static List<String> getSuggestionsForException(Throwable throwable) {
        String exceptionName = throwable.getClass().getName();
        String message = throwable.getMessage() != null ? throwable.getMessage().toLowerCase() : "";
        
        if (exceptionName.contains("NullPointerException")) {
            return List.of(
                "Check if objects are properly initialized before use",
                "Add null checks for method parameters",
                "Verify if external service responses are properly validated"
            );
        } else if (exceptionName.contains("ClassCastException")) {
            return List.of(
                "Verify object types before casting",
                "Use instanceof operator to check types",
                "Review generic type parameters"
            );
        } else if (exceptionName.contains("IndexOutOfBoundsException")) {
            return List.of(
                "Validate array or list indices before access",
                "Check collection size before iteration",
                "Ensure loop conditions are correct"
            );
        } else if (exceptionName.contains("FileNotFoundException")) {
            return List.of(
                "Verify file path is correct",
                "Check file permissions",
                "Ensure the file exists"
            );
        } else if (exceptionName.contains("IOException")) {
            return List.of(
                "Check network connectivity",
                "Verify file system permissions",
                "Ensure resources are properly closed"
            );
        } else if (exceptionName.contains("SQLException")) {
            return List.of(
                "Verify database connection settings",
                "Check SQL syntax",
                "Ensure database schema is compatible",
                "Validate transaction handling"
            );
        } else if (message.contains("connection") || message.contains("timeout")) {
            return List.of(
                "Check network connectivity",
                "Verify service endpoint is available",
                "Increase timeout settings if appropriate",
                "Implement retry logic with exponential backoff"
            );
        } else {
            // Generic suggestions
            return List.of(
                "Check the application logs for more details",
                "Review code around the exception source",
                "Verify environment configuration",
                "Add diagnostic logging around the problematic area"
            );
        }
    }
    
    /**
     * Escapes HTML special characters.
     * 
     * @param input The input string to escape
     * @return The escaped string
     */
    private static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
    
    /**
     * Generates an HTML report for the given exception and saves it to a file.
     * 
     * @param throwable The throwable to generate a report for
     * @param additionalContext Additional context to include in the report
     * @return The path to the created HTML file
     * @throws IOException If an I/O error occurs
     */
    public static Path saveHtmlReport(Throwable throwable, Map<String, Object> additionalContext) throws IOException {
        String html = generateHtml(throwable, additionalContext);
        
        // Create a unique filename
        String fileName = "exception-report-" + 
                          throwable.getClass().getSimpleName() + "-" +
                          UUID.randomUUID().toString().substring(0, 8) + 
                          ".html";
        
        // Save to a reports directory in the current working directory
        Path reportsDir = Paths.get(System.getProperty("user.dir"), "exception-reports");
        if (!Files.exists(reportsDir)) {
            Files.createDirectories(reportsDir);
        }
        
        Path filePath = reportsDir.resolve(fileName);
        Files.writeString(filePath, html);
        
        return filePath;
    }
}