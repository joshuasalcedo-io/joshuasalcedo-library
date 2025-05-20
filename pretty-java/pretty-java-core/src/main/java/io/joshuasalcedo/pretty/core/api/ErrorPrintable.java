package io.joshuasalcedo.pretty.core.api;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Interface for enhanced error handling and presentation.
 * <p>
 * This interface defines methods for displaying errors with improved formatting,
 * pretty-printing stack traces, and customizing error presentation for better
 * readability in terminal environments.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.0
 */
public interface ErrorPrintable {

    /**
     * Prints a beautifully formatted stack trace for an exception.
     *
     * @param throwable The exception to print
     * @return this object for method chaining
     */
    Object printException(Throwable throwable);
    
    /**
     * Wraps a standard throwable with enhanced formatting capabilities.
     *
     * @param throwable The throwable to enhance
     * @return An enhanced version of the throwable
     */
    Throwable enhanceThrowable(Throwable throwable);
    
    /**
     * Creates and prints an error report with contextual information about an exception.
     *
     * @param throwable The exception to report
     * @param title Optional title for the error report (can be null)
     * @return this object for method chaining
     */
    Object printErrorReport(Throwable throwable, String title);
    
    /**
     * Prints a stack trace with custom formatting to the specified stream.
     *
     * @param throwable The throwable whose stack trace to print
     * @param stream The print stream to write to
     * @return this object for method chaining
     */
    Object printStackTrace(Throwable throwable, PrintStream stream);
    
    /**
     * Prints a stack trace with custom formatting to the specified writer.
     *
     * @param throwable The throwable whose stack trace to print
     * @param writer The print writer to write to
     * @return this object for method chaining
     */
    Object printStackTrace(Throwable throwable, PrintWriter writer);
    
    /**
     * Prints a stack trace with custom formatting to standard error.
     *
     * @param throwable The throwable whose stack trace to print
     * @return this object for method chaining
     */
    Object printStackTrace(Throwable throwable);
    
    /**
     * Formats just the first line of a throwable (the class name and message).
     *
     * @param throwable The throwable to format
     * @return A formatted string representation of the throwable header
     */
    String formatThrowableHeader(Throwable throwable);
    
    /**
     * Formats a single stack trace element with proper styling.
     *
     * @param element The stack trace element to format
     * @return A formatted string representation of the stack trace element
     */
    String formatStackTraceElement(StackTraceElement element);
    
    /**
     * Formats a throwable's message, optionally applying custom styling.
     *
     * @param message The error message to format
     * @param isError Whether this is an error message (as opposed to a warning/info)
     * @return A formatted string representation of the message
     */
    String formatErrorMessage(String message, boolean isError);
    
    /**
     * Provides a compact one-line summary of an exception.
     *
     * @param throwable The throwable to summarize
     * @return A one-line summary of the exception
     */
    String summarizeException(Throwable throwable);
    
    /**
     * Prints suggestions for resolving common exceptions.
     *
     * @param throwable The throwable to provide suggestions for
     * @return this object for method chaining
     */
    Object printExceptionSuggestions(Throwable throwable);
    
    /**
     * Prints a list of common causes for this type of exception.
     *
     * @param throwable The throwable to analyze
     * @return this object for method chaining
     */
    Object printCommonCauses(Throwable throwable);
    
    /**
     * Prints a comparison between the expected value and the actual value that caused an error.
     *
     * @param expected The expected value
     * @param actual The actual value that caused the error
     * @return this object for method chaining
     */
    Object printValueComparison(Object expected, Object actual);
    
    /**
     * Prints code context around the line where an exception occurred, if available.
     *
     * @param throwable The throwable to analyze
     * @param contextLines Number of lines before and after the error line to include
     * @return this object for method chaining
     */
    Object printCodeContext(Throwable throwable, int contextLines);
    
    /**
     * Logs an exception to a file with enhanced formatting.
     *
     * @param throwable The throwable to log
     * @param filePath The path to the log file
     * @return this object for method chaining
     */
    Object logExceptionToFile(Throwable throwable, String filePath);
    
    /**
     * Creates an interactive navigable view of a complex exception chain.
     * This is for terminals that support interactive navigation.
     *
     * @param throwable The root throwable to navigate
     * @return A handle to control the interactive view
     */
    ExceptionNavigator createInteractiveExceptionView(Throwable throwable);
    
    /**
     * Returns the error details as a structured object rather than printing them.
     *
     * @param throwable The throwable to analyze
     * @return A structured representation of the error
     */
    ErrorDetails getStructuredErrorDetails(Throwable throwable);
    
    /**
     * Handle for navigating through a complex exception interactively.
     */
    interface ExceptionNavigator {
        /**
         * Moves to the cause of the current exception.
         * 
         * @return this navigator for method chaining
         */
        ExceptionNavigator navigateToCause();
        
        /**
         * Moves to a specific suppressed exception.
         * 
         * @param index the index of the suppressed exception
         * @return this navigator for method chaining
         */
        ExceptionNavigator navigateToSuppressed(int index);
        
        /**
         * Returns to the parent exception in the chain.
         * 
         * @return this navigator for method chaining
         */
        ExceptionNavigator navigateToParent();
        
        /**
         * Expands the current stack trace to show more frames.
         * 
         * @param additionalFrames number of additional frames to show
         * @return this navigator for method chaining
         */
        ExceptionNavigator expandStackTrace(int additionalFrames);
        
        /**
         * Closes the interactive view.
         */
        void close();
    }
    
    /**
     * Structured representation of error details.
     */
    interface ErrorDetails {
        /**
         * Gets the exception type.
         * 
         * @return the fully qualified class name of the exception
         */
        String getExceptionType();
        
        /**
         * Gets the error message.
         * 
         * @return the error message
         */
        String getMessage();
        
        /**
         * Gets the stack trace elements.
         * 
         * @return an array of stack trace elements
         */
        StackTraceElement[] getStackTrace();
        
        /**
         * Gets the cause details.
         * 
         * @return the cause's error details, or null if no cause
         */
        ErrorDetails getCause();
        
        /**
         * Gets the suppressed exceptions.
         * 
         * @return an array of suppressed exception details
         */
        ErrorDetails[] getSuppressed();
        
        /**
         * Gets the location where the exception occurred.
         * 
         * @return a string representation of the exception location 
         */
        String getLocation();
        
        /**
         * Gets suggested fixes for this error.
         * 
         * @return an array of suggested fixes
         */
        String[] getSuggestions();
    }
}