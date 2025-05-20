package io.joshuasalcedo.pretty.core.model.error;

import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.TextUtils;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An enhanced throwable implementation that provides beautiful colorized stack traces
 * with improved visual organization, clickable links, and contextual information.
 */
public class EnhancedThrowable extends Throwable {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Default styles - can be customized
    private TerminalStyle headerStyle = TerminalStyle.ERROR;
    private TerminalStyle typeStyle = TerminalStyle.ERROR;
    private TerminalStyle messageStyle = TerminalStyle.ERROR;
    private TerminalStyle sectionStyle = TerminalStyle.UI_HEADER;
    private TerminalStyle framePrefixStyle = TerminalStyle.SECONDARY;
    private TerminalStyle frameNumberStyle = TerminalStyle.EMPHASIS;
    private TerminalStyle packageStyle = TerminalStyle.UI_SUBHEADER;
    private TerminalStyle exceptionClassStyle = TerminalStyle.ERROR;
    private TerminalStyle jdkClassStyle = TerminalStyle.SECONDARY;
    private TerminalStyle libraryClassStyle = TerminalStyle.TRACE;
    private TerminalStyle appClassStyle = TerminalStyle.SUCCESS;
    private TerminalStyle methodStyle = TerminalStyle.UI_VALUE;
    private TerminalStyle fileInfoStyle = TerminalStyle.HYPERLINK;
    private TerminalStyle causeStyle = TerminalStyle.WARNING;
    
    // Package prefixes for highlighting
    private final Map<String, TerminalStyle> packageHighlighting = new HashMap<>();
    
    /**
     * Constructs a new throwable with null as its detail message.
     */
    public EnhancedThrowable() {
        super();
        setupDefaultPackageHighlighting();
    }
    
    /**
     * Constructs a new throwable with the specified detail message.
     *
     * @param message the detail message
     */
    public EnhancedThrowable(String message) {
        super(message);
        setupDefaultPackageHighlighting();
    }
    
    /**
     * Constructs a new throwable with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public EnhancedThrowable(String message, Throwable cause) {
        super(message, cause);
        setupDefaultPackageHighlighting();
    }
    
    /**
     * Constructs a new throwable with the specified cause.
     *
     * @param cause the cause
     */
    public EnhancedThrowable(Throwable cause) {
        super(cause);
        setupDefaultPackageHighlighting();
    }
    
    /**
     * Sets up default package highlighting rules.
     */
    private void setupDefaultPackageHighlighting() {
        // JDK packages
        packageHighlighting.put("java.", jdkClassStyle);
        packageHighlighting.put("javax.", jdkClassStyle);
        packageHighlighting.put("sun.", jdkClassStyle);
        
        // Common third-party libraries
        packageHighlighting.put("org.springframework", libraryClassStyle);
        packageHighlighting.put("org.hibernate", libraryClassStyle);
        packageHighlighting.put("com.google", libraryClassStyle);
        packageHighlighting.put("org.apache", libraryClassStyle);
        
        // Application code (customize this for your app)
        packageHighlighting.put("io.joshuasalcedo", appClassStyle);
    }
    
    /**
     * Adds a package highlighting rule.
     *
     * @param packagePrefix the package prefix to highlight
     * @param style the style to use for this package
     * @return this throwable instance for method chaining
     */
    public EnhancedThrowable highlightPackage(String packagePrefix, TerminalStyle style) {
        packageHighlighting.put(packagePrefix, style);
        return this;
    }
    
    /**
     * Creates a wrapped enhanced throwable from any exception.
     *
     * @param throwable the throwable to wrap
     * @return an EnhancedThrowable wrapping the given throwable
     */
    public static EnhancedThrowable from(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        if (throwable instanceof EnhancedThrowable) {
            return (EnhancedThrowable) throwable;
        }
        
        return new EnhancedThrowable(throwable.getMessage(), throwable.getCause());
    }
    
    @Override
    public void printStackTrace(PrintStream s) {
        // Use the enhanced version
        printEnhancedStackTrace(s);
    }
    
    @Override
    public void printStackTrace(PrintWriter s) {
        // For PrintWriter, we'll use a simpler approach for now
        super.printStackTrace(s);
    }
    
    /**
     * Prints a beautifully formatted stack trace with improved visual organization.
     *
     * @param out the output stream to print to
     */
    public void printEnhancedStackTrace(PrintStream out) {
        if (out == null) return;
        
        // Print the main exception header
        out.println(headerStyle.apply(" EXCEPTION "));
        out.println(typeStyle.apply("╭─ Type: ") + getClass().getName());
        out.println(messageStyle.apply("╰─ Message: ") + getMessage());
        
        // Print stack trace
        out.println(sectionStyle.apply("\n┌─ STACK TRACE ") + "───────────────────────────────────────────────");
        
        StackTraceElement[] stackTrace = getStackTrace();
        printStackElements(stackTrace, getClass().getName(), out);
        
        // Handle nested exceptions (causes)
        int causeDepth = 1;
        Throwable cause = getCause();
        while (cause != null && causeDepth < 5) { // Limit depth to avoid excessive output
            out.println(causeStyle.apply("\n├─ CAUSED BY: ") + cause.getClass().getName());
            out.println(causeStyle.apply("│  ") + cause.getMessage());
            out.println(causeStyle.apply("│"));
            
            // Print cause stack trace
            printStackElements(cause.getStackTrace(), cause.getClass().getName(), out);
            
            // Move to next cause
            cause = cause.getCause();
            causeDepth++;
        }
        
        // If we hit the depth limit but there's still more causes
        if (cause != null) {
            out.println(causeStyle.apply("\n├─ Additional nested causes omitted..."));
        }
        
        out.println(sectionStyle.apply("└───────────────────────────────────────────────────────────────────"));
    }
    
    /**
     * Helper method to print stack trace elements with proper formatting and highlighting.
     *
     * @param stackTrace The stack trace elements to print
     * @param exceptionClassName The name of the exception class
     * @param out The output stream to print to
     */
    private void printStackElements(StackTraceElement[] stackTrace, String exceptionClassName, PrintStream out) {
        if (stackTrace == null || stackTrace.length == 0) {
            out.println(framePrefixStyle.apply("│  No stack trace available"));
            return;
        }
        
        // Group frames by package for better organization
        String currentPackage = "";
        boolean isFirstFrame = true;
        
        int maxFramesToShow = Math.min(stackTrace.length, 20); // Limit to 20 frames by default
        
        for (int i = 0; i < maxFramesToShow; i++) {
            StackTraceElement element = stackTrace[i];
            String className = element.getClassName();
            String packageName = className.contains(".") ?
                    className.substring(0, className.lastIndexOf('.')) : "";
            
            // Add package separator if we're entering a new package
            if (!packageName.equals(currentPackage)) {
                if (!isFirstFrame) {
                    out.println(packageStyle.apply("│"));
                }
                out.println(packageStyle.apply("│  package " + packageName));
                out.println(packageStyle.apply("│"));
                currentPackage = packageName;
            }
            
            // Format the frame
            String methodName = element.getMethodName();
            String fileName = element.getFileName();
            int lineNumber = element.getLineNumber();
            
            // Determine style based on package
            TerminalStyle classStyle = getStyleForClass(className, exceptionClassName);
            
            // Print the frame with line prefix
            StringBuilder frameBuilder = new StringBuilder();
            frameBuilder.append(framePrefixStyle.apply("│  "));
            
            // Add frame number
            frameBuilder.append(frameNumberStyle.apply(String.format("%2d", i))).append(": ");
            
            // Build the class and method part
            String shortClassName = className.substring(className.lastIndexOf('.') + 1);
            frameBuilder.append(classStyle.apply(shortClassName))
                    .append(".")
                    .append(methodStyle.apply(methodName));
            
            // Add source location
            if (fileName != null && lineNumber >= 0) {
                String locationText = fileName + ":" + lineNumber;
                frameBuilder.append(" (")
                        .append(fileInfoStyle.apply(locationText))
                        .append(")");
            } else {
                // No source file available
                frameBuilder.append(" (Unknown Source)");
            }
            
            out.println(frameBuilder.toString());
            
            isFirstFrame = false;
        }
        
        // If we have a long stack trace, add a note about trimmed frames
        if (stackTrace.length > maxFramesToShow) {
            out.println(framePrefixStyle.apply("│"));
            out.println(framePrefixStyle.apply("│  ... " + (stackTrace.length - maxFramesToShow) +
                    " more frames (showing first " + maxFramesToShow + ")"));
        }
    }
    
    /**
     * Determines the appropriate style for a class based on its package.
     *
     * @param className The fully qualified class name
     * @param exceptionClassName The name of the exception class
     * @return The appropriate TerminalStyle
     */
    private TerminalStyle getStyleForClass(String className, String exceptionClassName) {
        // If it's the exception class itself, use the exception style
        if (className.equals(exceptionClassName)) {
            return exceptionClassStyle;
        }
        
        // Check package prefixes
        for (Map.Entry<String, TerminalStyle> entry : packageHighlighting.entrySet()) {
            if (className.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // Default to app class style if no match
        return appClassStyle;
    }
    
    /**
     * Prints a comprehensive exception report with runtime context and suggested solutions.
     * 
     * @param out The output stream to print to
     * @param additionalContext Optional additional context information
     */
    public void printExceptionReport(PrintStream out, Map<String, Object> additionalContext) {
        if (out == null) return;
        
        // Get current time for the report header
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        
        // Print report header
        out.println(sectionStyle.apply(" EXCEPTION REPORT " + timestamp + " "));
        out.println();
        
        // Print basic exception info
        out.println(typeStyle.apply("Exception Type: ") + getClass().getName());
        out.println(messageStyle.apply("Message: ") + getMessage());
        out.println();
        
        // Print runtime environment
        printSubsection(out, "Environment Information");
        printKeyValue(out, "Java Version", System.getProperty("java.version"));
        printKeyValue(out, "OS Name", System.getProperty("os.name"));
        printKeyValue(out, "OS Version", System.getProperty("os.version"));
        printKeyValue(out, "OS Architecture", System.getProperty("os.arch"));
        printKeyValue(out, "User Directory", System.getProperty("user.dir"));
        out.println();
        
        // Print additional context if provided
        if (additionalContext != null && !additionalContext.isEmpty()) {
            printSubsection(out, "Additional Context");
            for (Map.Entry<String, Object> entry : additionalContext.entrySet()) {
                printKeyValue(out, entry.getKey(), String.valueOf(entry.getValue()));
            }
            out.println();
        }
        
        // Print the structured stack trace
        printSubsection(out, "Stack Trace");
        printEnhancedStackTrace(out);
        
        // Print potential solutions or actions based on exception type
        printPotentialSolutions(out);
    }
    
    /**
     * Prints a subsection header in the report.
     */
    private void printSubsection(PrintStream out, String title) {
        out.println(sectionStyle.apply("┌─ " + title + " ") + "─".repeat(50 - title.length()));
    }
    
    /**
     * Prints a key-value pair in the report.
     */
    private void printKeyValue(PrintStream out, String key, String value) {
        out.println(framePrefixStyle.apply("│  ") + 
                    TerminalStyle.UI_LABEL.apply(key + ": ") + 
                    TerminalStyle.UI_VALUE.apply(value));
    }
    
    /**
     * Prints a bullet list in the report.
     */
    private void printBulletList(PrintStream out, List<String> items) {
        for (String item : items) {
            out.println(framePrefixStyle.apply("│  • ") + 
                         TerminalStyle.UI_VALUE.apply(item));
        }
    }
    
    /**
     * Provides potential solutions or next steps based on exception type.
     */
    private void printPotentialSolutions(PrintStream out) {
        printSubsection(out, "Suggested Actions");
        
        // Custom suggestions based on common exception types
        String exceptionName = getClass().getName();
        String message = getMessage() != null ? getMessage().toLowerCase() : "";
        
        if (exceptionName.contains("NullPointerException")) {
            printBulletList(out, Arrays.asList(
                    "Check if objects are properly initialized before use",
                    "Add null checks for method parameters",
                    "Verify if external service responses are properly validated"
            ));
        } else if (exceptionName.contains("ClassCastException")) {
            printBulletList(out, Arrays.asList(
                    "Verify object types before casting",
                    "Use instanceof operator to check types",
                    "Review generic type parameters"
            ));
        } else if (exceptionName.contains("IndexOutOfBoundsException")) {
            printBulletList(out, Arrays.asList(
                    "Validate array or list indices before access",
                    "Check collection size before iteration",
                    "Ensure loop conditions are correct"
            ));
        } else if (exceptionName.contains("FileNotFoundException")) {
            printBulletList(out, Arrays.asList(
                    "Verify file path is correct",
                    "Check file permissions",
                    "Ensure the file exists"
            ));
        } else if (exceptionName.contains("IOException")) {
            printBulletList(out, Arrays.asList(
                    "Check network connectivity",
                    "Verify file system permissions",
                    "Ensure resources are properly closed"
            ));
        } else if (exceptionName.contains("SQLException")) {
            printBulletList(out, Arrays.asList(
                    "Verify database connection settings",
                    "Check SQL syntax",
                    "Ensure database schema is compatible",
                    "Validate transaction handling"
            ));
        } else if (exceptionName.contains("IllegalArgumentException")) {
            printBulletList(out, Arrays.asList(
                    "Validate method parameters",
                    "Check parameter constraints",
                    "Review API documentation for correct usage"
            ));
        } else if (message.contains("connection") || message.contains("timeout")) {
            printBulletList(out, Arrays.asList(
                    "Check network connectivity",
                    "Verify service endpoint is available",
                    "Increase timeout settings if appropriate",
                    "Implement retry logic with exponential backoff"
            ));
        } else {
            // Generic suggestions
            printBulletList(out, Arrays.asList(
                    "Check the application logs for more details",
                    "Review code around the exception source",
                    "Verify environment configuration",
                    "Add diagnostic logging around the problematic area"
            ));
        }
    }
    
    /**
     * Builder class for creating EnhancedThrowable instances with fluent API.
     */
    public static class Builder {
        private final EnhancedThrowable throwable;
        
        public Builder() {
            this.throwable = new EnhancedThrowable();
        }
        
        public Builder message(String message) {
            this.throwable.setMessage(message);
            return this;
        }
        
        public Builder cause(Throwable cause) {
            try {
                this.throwable.initCause(cause);
            } catch (IllegalStateException e) {
                // Ignore - cause may already be set
            }
            return this;
        }
        
        public Builder headerStyle(TerminalStyle style) {
            this.throwable.headerStyle = style;
            return this;
        }
        
        public Builder typeStyle(TerminalStyle style) {
            this.throwable.typeStyle = style;
            return this;
        }
        
        public Builder messageStyle(TerminalStyle style) {
            this.throwable.messageStyle = style;
            return this;
        }
        
        public Builder sectionStyle(TerminalStyle style) {
            this.throwable.sectionStyle = style;
            return this;
        }
        
        public Builder appClassStyle(TerminalStyle style) {
            this.throwable.appClassStyle = style;
            return this;
        }
        
        public Builder jdkClassStyle(TerminalStyle style) {
            this.throwable.jdkClassStyle = style;
            return this;
        }
        
        public Builder libraryClassStyle(TerminalStyle style) {
            this.throwable.libraryClassStyle = style;
            return this;
        }
        
        public Builder methodStyle(TerminalStyle style) {
            this.throwable.methodStyle = style;
            return this;
        }
        
        public Builder fileInfoStyle(TerminalStyle style) {
            this.throwable.fileInfoStyle = style;
            return this;
        }
        
        public Builder highlightPackage(String packagePrefix, TerminalStyle style) {
            this.throwable.highlightPackage(packagePrefix, style);
            return this;
        }
        
        public EnhancedThrowable build() {
            return throwable;
        }
    }
    
    // Helper method to set message through reflection (for builder)
    private void setMessage(String message) {
        try {
            java.lang.reflect.Field detailMessageField = Throwable.class.getDeclaredField("detailMessage");
            detailMessageField.setAccessible(true);
            detailMessageField.set(this, message);
        } catch (Exception e) {
            // Fallback if reflection fails
            // We can't directly set the message without reflection
        }
    }
}