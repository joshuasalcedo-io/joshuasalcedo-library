package io.joshuasalcedo.pretty.core.model.error;

import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.TextUtils;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for formatting stack traces with colors.
 * This formatter can be used to colorize any exception's stack trace.
 */
public class ExceptionFormatter {
    
    /**
     * Pattern to match stack trace elements
     */
    private static final Pattern STACK_TRACE_PATTERN = 
            Pattern.compile("\\s*at\\s+([\\w.$]+)\\.([\\w$]+)\\(([^:]+)(?::([\\d]+))?\\)");
    
    /**
     * Pattern to match "Caused by" lines
     */
    private static final Pattern CAUSED_BY_PATTERN = 
            Pattern.compile("Caused by:\\s+([\\w.$]+)(?::\\s+(.+))?");
    
    /**
     * Pattern to match "Suppressed" lines
     */
    private static final Pattern SUPPRESSED_PATTERN = 
            Pattern.compile("Suppressed:\\s+([\\w.$]+)(?::\\s+(.+))?");
    
    /**
     * Pattern to match the "more frames" line
     */
    private static final Pattern MORE_FRAMES_PATTERN = 
            Pattern.compile("\\s*\\.\\.\\.\\s+(\\d+)\\s+more");
    
    /**
     * Default styles for different parts of the stack trace
     */
    private Map<String, TerminalStyle> styles;
    
    /**
     * Optional package highlighting configuration
     */
    private Map<String, TerminalStyle> packageHighlights;
    
    /**
     * Creates a new formatter with default styles
     */
    public ExceptionFormatter() {
        styles = new HashMap<>();
        packageHighlights = new HashMap<>();
        
        // Set default styles
        styles.put("message", TerminalStyle.ERROR);
        styles.put("stackTrace", TerminalStyle.SECONDARY);
        styles.put("className", TerminalStyle.EMPHASIS);
        styles.put("methodName", TerminalStyle.UI_VALUE);
        styles.put("fileName", TerminalStyle.TRACE);
        styles.put("lineNumber", TerminalStyle.TRACE);
        styles.put("causedBy", TerminalStyle.WARNING);
        styles.put("suppressed", TerminalStyle.INFO);
        styles.put("nativeMethod", TerminalStyle.DEV_NOTE);
        styles.put("moreFrames", TerminalStyle.TERTIARY);
    }
    
    /**
     * Sets a style for a specific part of the stack trace
     *
     * @param part the part to style ("message", "stackTrace", "className", etc.)
     * @param style the style to use
     * @return this formatter instance for method chaining
     */
    public ExceptionFormatter setStyle(String part, TerminalStyle style) {
        styles.put(part, style);
        return this;
    }
    
    /**
     * Adds a package highlighting rule to make specific packages stand out
     *
     * @param packagePrefix the package prefix to highlight (e.g., "com.mycompany")
     * @param style the style to use for this package
     * @return this formatter instance for method chaining
     */
    public ExceptionFormatter highlightPackage(String packagePrefix, TerminalStyle style) {
        packageHighlights.put(packagePrefix, style);
        return this;
    }
    
    /**
     * Formats an exception's stack trace with colors
     *
     * @param throwable the exception to format
     * @return a formatted, colorized string
     */
    public String format(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        // If terminal doesn't support ANSI colors, return the standard stack trace
        if (!TerminalUtils.isAnsiSupported()) {
            return captureStackTrace(throwable);
        }
        
        // Capture the standard stack trace
        String rawStackTrace = captureStackTrace(throwable);
        
        // Process and colorize each line
        StringBuilder result = new StringBuilder();
        String[] lines = rawStackTrace.split("\\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // Process the first line (exception message)
            if (i == 0) {
                result.append(formatExceptionMessage(line)).append("\n");
                continue;
            }
            
            // Check for "Caused by" lines
            Matcher causedByMatcher = CAUSED_BY_PATTERN.matcher(line);
            if (causedByMatcher.find()) {
                result.append(formatCausedByLine(line, causedByMatcher)).append("\n");
                continue;
            }
            
            // Check for "Suppressed" lines
            Matcher suppressedMatcher = SUPPRESSED_PATTERN.matcher(line);
            if (suppressedMatcher.find()) {
                result.append(formatSuppressedLine(line, suppressedMatcher)).append("\n");
                continue;
            }
            
            // Check for stack trace elements
            Matcher stackTraceMatcher = STACK_TRACE_PATTERN.matcher(line);
            if (stackTraceMatcher.find()) {
                result.append(formatStackTraceLine(line, stackTraceMatcher)).append("\n");
                continue;
            }
            
            // Check for "more frames" lines
            Matcher moreFramesMatcher = MORE_FRAMES_PATTERN.matcher(line);
            if (moreFramesMatcher.find()) {
                result.append(formatMoreFramesLine(line, moreFramesMatcher)).append("\n");
                continue;
            }
            
            // If none of the patterns match, add the line as is
            result.append(line).append("\n");
        }
        
        return result.toString();
    }
    
    /**
     * Formats the exception message line
     */
    private String formatExceptionMessage(String line) {
        // Split the line at the colon if there's a message
        int colonIndex = line.indexOf(": ");
        if (colonIndex > 0) {
            String exceptionClass = line.substring(0, colonIndex);
            String message = line.substring(colonIndex);
            return styles.get("className").apply(exceptionClass) + 
                   styles.get("message").apply(message);
        } else {
            // No message, just the exception class
            return styles.get("className").apply(line);
        }
    }
    
    /**
     * Formats a "Caused by" line
     */
    private String formatCausedByLine(String line, Matcher matcher) {
        StringBuilder sb = new StringBuilder();
        sb.append(styles.get("causedBy").apply("Caused by: "));
        
        String exceptionClass = matcher.group(1);
        sb.append(styles.get("className").apply(exceptionClass));
        
        String message = matcher.group(2);
        if (message != null) {
            sb.append(styles.get("message").apply(": " + message));
        }
        
        return sb.toString();
    }
    
    /**
     * Formats a "Suppressed" line
     */
    private String formatSuppressedLine(String line, Matcher matcher) {
        StringBuilder sb = new StringBuilder();
        sb.append(styles.get("suppressed").apply("Suppressed: "));
        
        String exceptionClass = matcher.group(1);
        sb.append(styles.get("className").apply(exceptionClass));
        
        String message = matcher.group(2);
        if (message != null) {
            sb.append(styles.get("message").apply(": " + message));
        }
        
        return sb.toString();
    }
    
    /**
     * Formats a stack trace element line
     */
    private String formatStackTraceLine(String line, Matcher matcher) {
        StringBuilder sb = new StringBuilder();
        
        // Format the "at " prefix
        String indent = line.substring(0, matcher.start());
        sb.append(styles.get("stackTrace").apply(indent + "at "));
        
        // Format the class name with possible package highlighting
        String className = matcher.group(1);
        TerminalStyle classStyle = findPackageStyle(className);
        if (classStyle == null) {
            classStyle = styles.get("className");
        }
        sb.append(classStyle.apply(className));
        
        // Add the dot separator
        sb.append(styles.get("stackTrace").apply("."));
        
        // Format the method name
        String methodName = matcher.group(2);
        sb.append(styles.get("methodName").apply(methodName));
        
        // Format the file info
        sb.append(styles.get("stackTrace").apply("("));
        
        String fileInfo = matcher.group(3);
        if ("Native Method".equals(fileInfo)) {
            sb.append(styles.get("nativeMethod").apply(fileInfo));
        } else {
            sb.append(styles.get("fileName").apply(fileInfo));
            
            // Format the line number if present
            String lineNum = matcher.group(4);
            if (lineNum != null) {
                sb.append(styles.get("stackTrace").apply(":"));
                sb.append(styles.get("lineNumber").apply(lineNum));
            }
        }
        
        sb.append(styles.get("stackTrace").apply(")"));
        
        return sb.toString();
    }
    
    /**
     * Formats a "more frames" line
     */
    private String formatMoreFramesLine(String line, Matcher matcher) {
        String indent = line.substring(0, matcher.start());
        String count = matcher.group(1);
        
        return styles.get("moreFrames").apply(indent + "... " + count + " more");
    }
    
    /**
     * Finds the appropriate style for a package or class name based on package highlighting rules
     */
    private TerminalStyle findPackageStyle(String className) {
        for (Map.Entry<String, TerminalStyle> entry : packageHighlights.entrySet()) {
            if (className.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    /**
     * Captures a throwable's stack trace as a string
     */
    private String captureStackTrace(Throwable throwable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
    
    /**
     * Creates a new builder for fluent configuration
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder class for fluent configuration of the formatter
     */
    public static class Builder {
        private final ExceptionFormatter formatter;
        
        private Builder() {
            formatter = new ExceptionFormatter();
        }
        
        public Builder messageStyle(TerminalStyle style) {
            formatter.setStyle("message", style);
            return this;
        }
        
        public Builder stackTraceStyle(TerminalStyle style) {
            formatter.setStyle("stackTrace", style);
            return this;
        }
        
        public Builder classNameStyle(TerminalStyle style) {
            formatter.setStyle("className", style);
            return this;
        }
        
        public Builder methodNameStyle(TerminalStyle style) {
            formatter.setStyle("methodName", style);
            return this;
        }
        
        public Builder fileNameStyle(TerminalStyle style) {
            formatter.setStyle("fileName", style);
            return this;
        }
        
        public Builder lineNumberStyle(TerminalStyle style) {
            formatter.setStyle("lineNumber", style);
            return this;
        }
        
        public Builder causedByStyle(TerminalStyle style) {
            formatter.setStyle("causedBy", style);
            return this;
        }
        
        public Builder suppressedStyle(TerminalStyle style) {
            formatter.setStyle("suppressed", style);
            return this;
        }
        
        public Builder nativeMethodStyle(TerminalStyle style) {
            formatter.setStyle("nativeMethod", style);
            return this;
        }
        
        public Builder moreFramesStyle(TerminalStyle style) {
            formatter.setStyle("moreFrames", style);
            return this;
        }
        
        public Builder highlightPackage(String packagePrefix, TerminalStyle style) {
            formatter.highlightPackage(packagePrefix, style);
            return this;
        }
        
        public ExceptionFormatter build() {
            return formatter;
        }
    }
}