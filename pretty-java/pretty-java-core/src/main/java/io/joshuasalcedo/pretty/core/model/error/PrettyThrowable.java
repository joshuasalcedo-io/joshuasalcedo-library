package io.joshuasalcedo.pretty.core.model.error;

import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.TextUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * A custom extension of Throwable that provides colored stack traces.
 * This class overrides the printStackTrace methods to provide colorized
 * output for better readability in terminal environments.
 */
public class PrettyThrowable extends Throwable {

    /**
     * The terminal style to use for the main exception message
     */
    private TerminalStyle messageStyle = TerminalStyle.ERROR;
    
    /**
     * The terminal style to use for stack trace elements
     */
    private TerminalStyle stackTraceStyle = TerminalStyle.SECONDARY;

    /**
     * The terminal style to use for "Caused by:" sections
     */
    private TerminalStyle causedByStyle = TerminalStyle.WARNING;
    
    /**
     * The terminal style to use for "Suppressed:" sections
     */
    private TerminalStyle suppressedStyle = TerminalStyle.INFO;
    
    /**
     * The terminal style to use for class names in the stack trace
     */
    private TerminalStyle classNameStyle = TerminalStyle.EMPHASIS;
    
    /**
     * The terminal style to use for method names in the stack trace
     */
    private TerminalStyle methodNameStyle = TerminalStyle.UI_VALUE;
    
    /**
     * The terminal style to use for file information in the stack trace
     */
    private TerminalStyle fileInfoStyle = TerminalStyle.TRACE;
    
    /**
     * The terminal style to use for native method indicators
     */
    private TerminalStyle nativeMethodStyle = TerminalStyle.DEV_NOTE;

    /**
     * Constructs a new throwable with {@code null} as its detail message.
     */
    public PrettyThrowable() {
        super();
    }

    /**
     * Constructs a new throwable with the specified detail message.
     *
     * @param message the detail message
     */
    public PrettyThrowable(String message) {
        super(message);
    }

    /**
     * Constructs a new throwable with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public PrettyThrowable(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new throwable with the specified cause.
     *
     * @param cause the cause
     */
    public PrettyThrowable(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new throwable with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause
     * @param enableSuppression whether suppression is enabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    protected PrettyThrowable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Sets the style for the main exception message.
     *
     * @param style the style to use
     * @return this PrettyThrowable for method chaining
     */
    public PrettyThrowable withMessageStyle(TerminalStyle style) {
        this.messageStyle = style;
        return this;
    }

    /**
     * Sets the style for stack trace elements.
     *
     * @param style the style to use
     * @return this PrettyThrowable for method chaining
     */
    public PrettyThrowable withStackTraceStyle(TerminalStyle style) {
        this.stackTraceStyle = style;
        return this;
    }

    /**
     * Sets the style for "Caused by:" sections.
     *
     * @param style the style to use
     * @return this PrettyThrowable for method chaining
     */
    public PrettyThrowable withCausedByStyle(TerminalStyle style) {
        this.causedByStyle = style;
        return this;
    }

    /**
     * Sets the style for "Suppressed:" sections.
     *
     * @param style the style to use
     * @return this PrettyThrowable for method chaining
     */
    public PrettyThrowable withSuppressedStyle(TerminalStyle style) {
        this.suppressedStyle = style;
        return this;
    }

    /**
     * Creates a new PrettyThrowable that wraps the given throwable.
     *
     * @param throwable the throwable to wrap
     * @return a new PrettyThrowable wrapping the given throwable
     */
    public static PrettyThrowable from(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        if (throwable instanceof PrettyThrowable) {
            return (PrettyThrowable) throwable;
        }
        
        return new PrettyThrowable(throwable.getMessage(), throwable.getCause());
    }

    @Override
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            // Print the exception class name and message
            s.println(messageStyle.apply(this.toString()));
            
            // Print the stack trace elements
            StackTraceElement[] trace = getStackTrace();
            for (StackTraceElement traceElement : trace) {
                s.println(formatStackTraceElement(traceElement, "\tat "));
            }
            
            // Print suppressed exceptions, if any
            Throwable[] suppressed = getSuppressed();
            if (suppressed.length > 0) {
                for (Throwable se : suppressed) {
                    printEnclosedStackTrace(se, s, trace, suppressedStyle.apply("Suppressed: "), "\t");
                }
            }
            
            // Print cause, if any
            Throwable ourCause = getCause();
            if (ourCause != null) {
                printEnclosedStackTrace(ourCause, s, trace, causedByStyle.apply("Caused by: "), "");
            }
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        // For PrintWriter, we'll use the default implementation for now
        // In a real implementation, you might want to duplicate the PrintStream logic here
        super.printStackTrace(s);
    }

    /**
     * Formats a stack trace element with the appropriate styling.
     *
     * @param element the stack trace element to format
     * @param prefix the prefix to add before the element
     * @return the formatted stack trace element
     */
    private String formatStackTraceElement(StackTraceElement element, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(stackTraceStyle.apply(prefix));
        
        // Format class and method name
        String declaringClass = element.getClassName();
        String methodName = element.getMethodName();
        
        sb.append(classNameStyle.apply(declaringClass));
        sb.append(stackTraceStyle.apply("."));
        sb.append(methodNameStyle.apply(methodName));
        
        // Format file info
        sb.append(stackTraceStyle.apply("("));
        if (element.isNativeMethod()) {
            sb.append(nativeMethodStyle.apply("Native Method"));
        } else if (element.getFileName() == null) {
            sb.append(fileInfoStyle.apply("Unknown Source"));
        } else {
            sb.append(fileInfoStyle.apply(element.getFileName()));
            if (element.getLineNumber() >= 0) {
                sb.append(stackTraceStyle.apply(":"));
                sb.append(fileInfoStyle.apply(String.valueOf(element.getLineNumber())));
            }
        }
        sb.append(stackTraceStyle.apply(")"));
        
        return sb.toString();
    }

    /**
     * Print an enclosed stack trace (for causes and suppressed exceptions).
     */
    private void printEnclosedStackTrace(Throwable throwable, PrintStream s, StackTraceElement[] enclosingTrace, 
                                        String caption, String prefix) {
        // Print the caption and throwable
        s.println(prefix + caption + messageStyle.apply(throwable.toString()));
        
        // Print the stack trace elements
        StackTraceElement[] trace = throwable.getStackTrace();
        int m = trace.length - 1;
        int n = enclosingTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
            m--; n--;
        }
        int framesInCommon = trace.length - 1 - m;
        
        for (int i = 0; i <= m; i++) {
            s.println(formatStackTraceElement(trace[i], prefix + "\tat "));
        }
        
        if (framesInCommon != 0) {
            s.println(stackTraceStyle.apply(prefix + "\t... " + framesInCommon + " more"));
        }
        
        // Print suppressed exceptions, if any
        Throwable[] suppressed = throwable.getSuppressed();
        if (suppressed.length > 0) {
            for (Throwable se : suppressed) {
                printEnclosedStackTrace(se, s, trace, suppressedStyle.apply("Suppressed: "), prefix + "\t");
            }
        }
        
        // Print cause, if any
        Throwable cause = throwable.getCause();
        if (cause != null) {
            printEnclosedStackTrace(cause, s, trace, causedByStyle.apply("Caused by: "), prefix);
        }
    }

    /**
     * Builder class for fluent creation of PrettyThrowable instances.
     */
    public static class Builder {
        private String message;
        private Throwable cause;
        private TerminalStyle messageStyle = TerminalStyle.ERROR;
        private TerminalStyle stackTraceStyle = TerminalStyle.SECONDARY;
        private TerminalStyle causedByStyle = TerminalStyle.WARNING;
        private TerminalStyle suppressedStyle = TerminalStyle.INFO;
        private TerminalStyle classNameStyle = TerminalStyle.EMPHASIS;
        private TerminalStyle methodNameStyle = TerminalStyle.UI_VALUE;
        private TerminalStyle fileInfoStyle = TerminalStyle.TRACE;
        private TerminalStyle nativeMethodStyle = TerminalStyle.DEV_NOTE;
        
        public Builder() {
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }
        
        public Builder messageStyle(TerminalStyle style) {
            this.messageStyle = style;
            return this;
        }
        
        public Builder stackTraceStyle(TerminalStyle style) {
            this.stackTraceStyle = style;
            return this;
        }
        
        public Builder causedByStyle(TerminalStyle style) {
            this.causedByStyle = style;
            return this;
        }
        
        public Builder suppressedStyle(TerminalStyle style) {
            this.suppressedStyle = style;
            return this;
        }
        
        public Builder classNameStyle(TerminalStyle style) {
            this.classNameStyle = style;
            return this;
        }
        
        public Builder methodNameStyle(TerminalStyle style) {
            this.methodNameStyle = style;
            return this;
        }
        
        public Builder fileInfoStyle(TerminalStyle style) {
            this.fileInfoStyle = style;
            return this;
        }
        
        public Builder nativeMethodStyle(TerminalStyle style) {
            this.nativeMethodStyle = style;
            return this;
        }
        
        public PrettyThrowable build() {
            PrettyThrowable throwable;
            if (cause != null) {
                throwable = new PrettyThrowable(message, cause);
            } else {
                throwable = new PrettyThrowable(message);
            }
            
            return throwable
                .withMessageStyle(messageStyle)
                .withStackTraceStyle(stackTraceStyle)
                .withCausedByStyle(causedByStyle)
                .withSuppressedStyle(suppressedStyle);
        }
    }
}