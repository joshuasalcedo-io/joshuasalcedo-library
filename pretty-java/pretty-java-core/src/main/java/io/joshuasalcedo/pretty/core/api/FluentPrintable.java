package io.joshuasalcedo.pretty.core.api;

import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

/**
 * An interface that extends the functionality of PrintStream by returning the instance
 * instead of void, allowing for method chaining and more functional programming patterns.
 * <p>
 * This interface mirrors all PrintStream methods but changes return types from void to Object,
 * enabling fluent API design while maintaining compatibility with standard PrintStream operations.
 * </p>
 *
 * @author Joshua Salcedo
 * @version 1.0
 */
public interface FluentPrintable {

    /**
     * Flushes the stream.
     *
     * @return this object for method chaining
     */
    Object flush();

    /**
     * Closes the stream.
     *
     * @return this object for method chaining
     */
    Object close();

    /**
     * Write the specified byte to this stream.
     *
     * @param b The byte to be written
     * @return this object for method chaining
     */
    Object write(int b);

    /**
     * Write the specified byte array to this stream.
     *
     * @param buf The byte array to be written
     * @return this object for method chaining
     */
    Object write(byte[] buf);

    /**
     * Write a portion of the specified byte array to this stream.
     *
     * @param buf The byte array to be written
     * @param off The offset from which to start writing
     * @param len The number of bytes to write
     * @return this object for method chaining
     */
    Object write(byte[] buf, int off, int len);

    /**
     * Prints a boolean value.
     *
     * @param b The boolean to be printed
     * @return this object for method chaining
     */
    Object print(boolean b);

    /**
     * Prints a character.
     *
     * @param c The character to be printed
     * @return this object for method chaining
     */
    Object print(char c);

    /**
     * Prints an integer.
     *
     * @param i The integer to be printed
     * @return this object for method chaining
     */
    Object print(int i);

    /**
     * Prints a long.
     *
     * @param l The long to be printed
     * @return this object for method chaining
     */
    Object print(long l);

    /**
     * Prints a float.
     *
     * @param f The float to be printed
     * @return this object for method chaining
     */
    Object print(float f);

    /**
     * Prints a double.
     *
     * @param d The double to be printed
     * @return this object for method chaining
     */
    Object print(double d);

    /**
     * Prints a character array.
     *
     * @param s The character array to be printed
     * @return this object for method chaining
     */
    Object print(char[] s);

    /**
     * Prints a string.
     *
     * @param s The string to be printed
     * @return this object for method chaining
     */
    Object print(String s);

    /**
     * Prints an object.
     *
     * @param obj The object to be printed
     * @return this object for method chaining
     */
    Object print(Object obj);

    /**
     * Terminates the current line by writing the line separator string.
     *
     * @return this object for method chaining
     */
    Object println();

    /**
     * Prints a boolean and then terminates the line.
     *
     * @param x The boolean to be printed
     * @return this object for method chaining
     */
    Object println(boolean x);

    /**
     * Prints a character and then terminates the line.
     *
     * @param x The character to be printed
     * @return this object for method chaining
     */
    Object println(char x);

    /**
     * Prints an integer and then terminates the line.
     *
     * @param x The integer to be printed
     * @return this object for method chaining
     */
    Object println(int x);

    /**
     * Prints a long and then terminates the line.
     *
     * @param x The long to be printed
     * @return this object for method chaining
     */
    Object println(long x);

    /**
     * Prints a float and then terminates the line.
     *
     * @param x The float to be printed
     * @return this object for method chaining
     */
    Object println(float x);

    /**
     * Prints a double and then terminates the line.
     *
     * @param x The double to be printed
     * @return this object for method chaining
     */
    Object println(double x);

    /**
     * Prints a character array and then terminates the line.
     *
     * @param x The character array to be printed
     * @return this object for method chaining
     */
    Object println(char[] x);

    /**
     * Prints a string and then terminates the line.
     *
     * @param x The string to be printed
     * @return this object for method chaining
     */
    Object println(String x);

    /**
     * Prints an object and then terminates the line.
     *
     * @param x The object to be printed
     * @return this object for method chaining
     */
    Object println(Object x);

    /**
     * A convenience method to write a formatted string to this output stream using
     * the specified format string and arguments.
     *
     * @param format A format string
     * @param args Arguments referenced by the format specifiers in the format string
     * @return this object for method chaining
     */
    Object printf(String format, Object... args);

    /**
     * A convenience method to write a formatted string to this output stream using
     * the specified format string and arguments.
     *
     * @param l The locale to apply during formatting
     * @param format A format string
     * @param args Arguments referenced by the format specifiers in the format string
     * @return this object for method chaining
     */
    Object printf(Locale l, String format, Object... args);

    /**
     * Writes a formatted string to this output stream using the specified format
     * string and arguments.
     *
     * @param format A format string
     * @param args Arguments referenced by the format specifiers in the format string
     * @return this object for method chaining
     */
    Object format(String format, Object... args);

    /**
     * Writes a formatted string to this output stream using the specified format
     * string and arguments.
     *
     * @param l The locale to apply during formatting
     * @param format A format string
     * @param args Arguments referenced by the format specifiers in the format string
     * @return this object for method chaining
     */
    Object format(Locale l, String format, Object... args);

    /**
     * Appends the specified character to this output stream.
     *
     * @param c The character to append
     * @return this object for method chaining
     */
    Object append(char c);

    /**
     * Appends the specified character sequence to this output stream.
     *
     * @param csq The character sequence to append
     * @return this object for method chaining
     */
    Object append(CharSequence csq);

    /**
     * Appends a subsequence of the specified character sequence to this output stream.
     *
     * @param csq The character sequence to append
     * @param start The index of the first character in the subsequence
     * @param end The index of the character following the last character in the subsequence
     * @return this object for method chaining
     */
    Object append(CharSequence csq, int start, int end);

    /**
     * Checks if the stream has encountered an error.
     *
     * @return true if the stream has encountered an error
     */
    boolean checkError();

    /**
     * Sets the error state of the stream.
     *
     * @param b the error state to set
     * @return this object for method chaining
     */
    Object setError(boolean b);
}