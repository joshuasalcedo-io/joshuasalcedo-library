package io.joshuasalcedo.library.prettyconsole.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

import static io.joshuasalcedo.library.prettyconsole.utils.TextUtils.ANSI_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(TextUtilsTest.class);

    @BeforeEach
    void setUp() {
        logger.info("Starting ANSI pattern test");
    }

    @Test
    void testBasicAnsiPatternMatching() {
        logger.info("Starting basic ANSI pattern matching test");

        // Test cases with ANSI codes
        String redText = "\u001B[31mThis is red text\u001B[0m";
        String boldText = "\u001B[1mThis is bold text\u001B[0m";
        String complexFormatting = "\u001B[1;4;31mBold, underlined, and red\u001B[0m";
        String noAnsi = "This has no ANSI codes";

        System.out.println("-----------------------------------------");
        System.out.println("Testing pattern matching for ANSI escape sequences");
        System.out.println("Red text before: " + redText);
        System.out.println("Bold text before: " + boldText);
        System.out.println("Complex formatting before: " + complexFormatting);
        System.out.println("No ANSI before: " + noAnsi);

        // Test if pattern finds ANSI codes
        assertTrue(ANSI_PATTERN.matcher(redText).find(), "Pattern should match red text ANSI code");
        assertTrue(ANSI_PATTERN.matcher(boldText).find(), "Pattern should match bold text ANSI code");
        assertTrue(ANSI_PATTERN.matcher(complexFormatting).find(), "Pattern should match complex formatting ANSI codes");
        assertFalse(ANSI_PATTERN.matcher(noAnsi).find(), "Pattern should not match string without ANSI codes");

        // Removal of ANSI codes
        String cleanedRedText = ANSI_PATTERN.matcher(redText).replaceAll("");
        String cleanedBoldText = ANSI_PATTERN.matcher(boldText).replaceAll("");
        String cleanedComplexText = ANSI_PATTERN.matcher(complexFormatting).replaceAll("");
        String cleanedNoAnsi = ANSI_PATTERN.matcher(noAnsi).replaceAll("");

        System.out.println("Red text after: " + cleanedRedText);
        System.out.println("Bold text after: " + cleanedBoldText);
        System.out.println("Complex formatting after: " + cleanedComplexText);
        System.out.println("No ANSI after: " + cleanedNoAnsi);

        assertEquals("This is red text", cleanedRedText, "ANSI codes should be stripped from red text");
        assertEquals("This is bold text", cleanedBoldText, "ANSI codes should be stripped from bold text");
        assertEquals("Bold, underlined, and red", cleanedComplexText, "ANSI codes should be stripped from complex formatting");
        assertEquals("This has no ANSI codes", cleanedNoAnsi, "String without ANSI codes should remain unchanged");

        logger.info("Completed basic ANSI pattern matching test");
    }

    @Test
    void testMultipleAnsiCodesInString() {
        logger.info("Starting multiple ANSI codes test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing multiple ANSI codes in a single string");

        String multipleCodesText = "\u001B[34mBlue\u001B[31mRed\u001B[0mNormal";
        System.out.println("Multiple codes text before: " + multipleCodesText);

        // Count matches
        Matcher matcher = ANSI_PATTERN.matcher(multipleCodesText);
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        assertEquals(3, count, "Should find exactly 3 ANSI codes in the string");

        // Check removal
        String cleaned = ANSI_PATTERN.matcher(multipleCodesText).replaceAll("");
        System.out.println("Multiple codes text after: " + cleaned);

        assertEquals("BlueRedNormal", cleaned, "All ANSI codes should be stripped properly");

        logger.info("Completed multiple ANSI codes test");
    }

    @Test
    void testEdgeCases() {
        logger.info("Starting edge cases test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing edge cases for ANSI pattern");

        // Incomplete ANSI code
        String incompleteCode = "\u001B[";
        System.out.println("Incomplete ANSI code before: " + incompleteCode);
        boolean matches = ANSI_PATTERN.matcher(incompleteCode).find();
        assertFalse(matches, "Incomplete ANSI code should not match");

        // Empty string
        String emptyString = "";
        System.out.println("Empty string before: " + emptyString);
        matches = ANSI_PATTERN.matcher(emptyString).find();
        assertFalse(matches, "Empty string should not match ANSI pattern");

        // ANSI at beginning and end
        String surroundingCodes = "\u001B[32mText with surrounding codes\u001B[0m";
        System.out.println("Surrounding codes before: " + surroundingCodes);
        String cleaned = ANSI_PATTERN.matcher(surroundingCodes).replaceAll("");
        System.out.println("Surrounding codes after: " + cleaned);

        assertEquals("Text with surrounding codes", cleaned, "ANSI codes at beginning and end should be removed");

        logger.info("Completed edge cases test");
    }

    @Test
    void testPerformance() {
        logger.info("Starting performance test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing ANSI pattern matching performance");

        // Create a large string with ANSI codes
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("\u001B[").append(i % 10).append("m")
                    .append("Text segment ").append(i)
                    .append("\u001B[0m");
        }
        String largeText = sb.toString();
        System.out.println("Large text sample before (first 100 chars): " + largeText.substring(0, Math.min(100, largeText.length())));

        // Measure time to process
        long startTime = System.nanoTime();
        String result = ANSI_PATTERN.matcher(largeText).replaceAll("");
        long endTime = System.nanoTime();

        // Log performance metrics
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Processing time: " + duration + " ms");
        System.out.println("Large text sample after (first 100 chars): " + result.substring(0, Math.min(100, result.length())));

        // Verify result doesn't contain ANSI codes
        assertFalse(ANSI_PATTERN.matcher(result).find(),
                "Result should not contain any ANSI codes after processing");

        logger.info("Completed performance test");
    }

    @Test
    void testVisibleLength() {
        logger.info("Starting visibleLength test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing visibleLength method");

        // Test with null
        System.out.println("Null string length: " + TextUtils.visibleLength(null));
        assertEquals(0, TextUtils.visibleLength(null), "Null string should have length 0");

        // Test with empty string
        System.out.println("Empty string length: " + TextUtils.visibleLength(""));
        assertEquals(0, TextUtils.visibleLength(""), "Empty string should have length 0");

        // Test with normal string
        String normal = "Hello World";
        System.out.println("Normal string before: " + normal + ", length: " + TextUtils.visibleLength(normal));
        assertEquals(11, TextUtils.visibleLength(normal), "Normal string should have correct length");

        // Test with ANSI codes
        String withAnsi = "\u001B[31mRed\u001B[0m \u001B[32mGreen\u001B[0m";
        System.out.println("String with ANSI before: " + withAnsi + ", length: " + TextUtils.visibleLength(withAnsi));
        assertEquals(9, TextUtils.visibleLength(withAnsi), "String with ANSI codes should count only visible characters");

        logger.info("Completed visibleLength test");
    }

    @Test
    void testTruncate() {
        logger.info("Starting truncate test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing truncate method");

        // Test with null
        System.out.println("Null string truncated to 5: " + TextUtils.truncate(null, 5));
        assertEquals("", TextUtils.truncate(null, 5), "Null string should be truncated to empty string");

        // Test with empty string
        System.out.println("Empty string truncated to 5: " + TextUtils.truncate("", 5));
        assertEquals("", TextUtils.truncate("", 5), "Empty string should remain empty when truncated");

        // Test with string shorter than max length
        String short1 = "Hello";
        System.out.println("Short string before: " + short1);
        System.out.println("Short string truncated to 10: " + TextUtils.truncate(short1, 10));
        assertEquals("Hello", TextUtils.truncate(short1, 10), "String shorter than max length should not be truncated");

        // Test with string equal to max length
        System.out.println("Equal length string truncated to 5: " + TextUtils.truncate(short1, 5));
        assertEquals("Hello", TextUtils.truncate(short1, 5), "String equal to max length should not be truncated");

        // Test with string longer than max length
        String long1 = "Hello World";
        System.out.println("Long string before: " + long1);
        System.out.println("Long string truncated to 5: " + TextUtils.truncate(long1, 5));
        assertEquals("Hello", TextUtils.truncate(long1, 5), "String longer than max length should be truncated");

        // Test with ANSI codes
        String withAnsi = "\u001B[31mRed\u001B[0m \u001B[32mGreen\u001B[0m";
        System.out.println("String with ANSI before: " + withAnsi);
        System.out.println("String with ANSI truncated to 5: " + TextUtils.truncate(withAnsi, 5));
        assertEquals("\u001B[31mRed\u001B[0m \u001B[32mG", TextUtils.truncate(withAnsi, 5),
                "String with ANSI codes should preserve codes and truncate visible characters");

        logger.info("Completed truncate test");
    }

    @Test
    void testRepeat() {
        logger.info("Starting repeat test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing repeat method");

        // Test with count <= 0
        System.out.println("Repeat 'a' 0 times: " + TextUtils.repeat('a', 0));
        assertEquals("", TextUtils.repeat('a', 0), "Repeat with count 0 should return empty string");

        System.out.println("Repeat 'a' -1 times: " + TextUtils.repeat('a', -1));
        assertEquals("", TextUtils.repeat('a', -1), "Repeat with negative count should return empty string");

        // Test with normal cases
        System.out.println("Repeat 'a' 1 time: " + TextUtils.repeat('a', 1));
        assertEquals("a", TextUtils.repeat('a', 1), "Repeat with count 1 should return single character");

        System.out.println("Repeat 'a' 5 times: " + TextUtils.repeat('a', 5));
        assertEquals("aaaaa", TextUtils.repeat('a', 5), "Repeat with count 5 should return 5 characters");

        System.out.println("Repeat space 5 times: '" + TextUtils.repeat(' ', 5) + "'");
        assertEquals("     ", TextUtils.repeat(' ', 5), "Repeat with space should work correctly");

        logger.info("Completed repeat test");
    }

    @Test
    void testCenter() {
        logger.info("Starting center test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing center method");

        // Test with null
        System.out.println("Null string centered to width 2: '" + TextUtils.center(null, 2, ' ') + "'");
        assertEquals("  ", TextUtils.center(null, 2, ' '), "Null string should be treated as empty");

        // Test with empty string
        System.out.println("Empty string centered to width 5: '" + TextUtils.center("", 5, ' ') + "'");
        assertEquals("     ", TextUtils.center("", 5, ' '), "Empty string should be centered with spaces");

        // Test with string longer than width
        String longStr = "Hello";
        System.out.println("String '" + longStr + "' centered to width 3: '" + TextUtils.center(longStr, 3, ' ') + "'");
        assertEquals("Hello", TextUtils.center(longStr, 3, ' '), "String longer than width should not be padded");

        // Test with normal cases
        System.out.println("String '" + longStr + "' centered to width 7: '" + TextUtils.center(longStr, 7, ' ') + "'");
        assertEquals(" Hello ", TextUtils.center(longStr, 7, ' '), "String should be centered with spaces");

        String text = "Text";
        System.out.println("String '" + text + "' centered to width 8 with '-': '" + TextUtils.center(text, 8, '-') + "'");
        assertEquals("--Text--", TextUtils.center(text, 8, '-'), "String should be centered with specified character");

        // Test with odd width and even string length
        System.out.println("String '" + text + "' centered to width 6: '" + TextUtils.center(text, 6, ' ') + "'");
        assertEquals(" Text ", TextUtils.center(text, 6, ' '), "String should be centered with left padding = right padding");

        // Test with even width and odd string length
        String abc = "ABC";
        System.out.println("String '" + abc + "' centered to width 6: '" + TextUtils.center(abc, 6, ' ') + "'");
        assertEquals(" ABC  ", TextUtils.center(abc, 6, ' '), "String should be centered with left padding <= right padding");

        // Test with ANSI codes
        String withAnsi = "\u001B[31mRed\u001B[0m";
        System.out.println("String with ANSI before: " + withAnsi);
        System.out.println("String with ANSI centered to width 5: '" + TextUtils.center(withAnsi, 5, ' ') + "'");
        assertEquals(" \u001B[31mRed\u001B[0m ", TextUtils.center(withAnsi, 5, ' '),
                "String with ANSI codes should be centered based on visible length");

        logger.info("Completed center test");
    }

    @Test
    void testPadLeft() {
        logger.info("Starting padLeft test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing padLeft method");

        // Test with null
        System.out.println("Null string padLeft to width 2: '" + TextUtils.padLeft(null, 2, ' ') + "'");
        assertEquals("  ", TextUtils.padLeft(null, 2, ' '), "Null string should be treated as empty");

        // Test with empty string
        System.out.println("Empty string padLeft to width 5: '" + TextUtils.padLeft("", 5, ' ') + "'");
        assertEquals("     ", TextUtils.padLeft("", 5, ' '), "Empty string should be padded with spaces");

        // Test with string longer than width
        String hello = "Hello";
        System.out.println("String '" + hello + "' padLeft to width 3: '" + TextUtils.padLeft(hello, 3, ' ') + "'");
        assertEquals("Hello", TextUtils.padLeft(hello, 3, ' '), "String longer than width should not be padded");

        // Test with normal cases
        System.out.println("String '" + hello + "' padLeft to width 7: '" + TextUtils.padLeft(hello, 7, ' ') + "'");
        assertEquals("  Hello", TextUtils.padLeft(hello, 7, ' '), "String should be padded on left with spaces");

        String text = "Text";
        System.out.println("String '" + text + "' padLeft to width 7 with '-': '" + TextUtils.padLeft(text, 7, '-') + "'");
        assertEquals("---Text", TextUtils.padLeft(text, 7, '-'), "String should be padded on left with specified character");

        // Test with ANSI codes
        String withAnsi = "\u001B[31mRed\u001B[0m";
        System.out.println("String with ANSI before: " + withAnsi);
        System.out.println("String with ANSI padLeft to width 5: '" + TextUtils.padLeft(withAnsi, 5, ' ') + "'");
        assertEquals("  \u001B[31mRed\u001B[0m", TextUtils.padLeft(withAnsi, 5, ' '),
                "String with ANSI codes should be padded based on visible length");

        logger.info("Completed padLeft test");
    }

    @Test
    void testPadRight() {
        logger.info("Starting padRight test");

        System.out.println("-----------------------------------------");
        System.out.println("Testing padRight method");

        // Test with null
        System.out.println("Null string padRight to width 2: '" + TextUtils.padRight(null, 2, ' ') + "'");
        assertEquals("  ", TextUtils.padRight(null, 2, ' '), "Null string should be treated as empty");

        // Test with empty string
        System.out.println("Empty string padRight to width 5: '" + TextUtils.padRight("", 5, ' ') + "'");
        assertEquals("     ", TextUtils.padRight("", 5, ' '), "Empty string should be padded with spaces");

        // Test with string longer than width
        String hello = "Hello";
        System.out.println("String '" + hello + "' padRight to width 3: '" + TextUtils.padRight(hello, 3, ' ') + "'");
        assertEquals("Hello", TextUtils.padRight(hello, 3, ' '), "String longer than width should not be padded");

        // Test with normal cases
        System.out.println("String '" + hello + "' padRight to width 7: '" + TextUtils.padRight(hello, 7, ' ') + "'");
        assertEquals("Hello  ", TextUtils.padRight(hello, 7, ' '), "String should be padded on right with spaces");

        String text = "Text";
        System.out.println("String '" + text + "' padRight to width 7 with '-': '" + TextUtils.padRight(text, 7, '-') + "'");
        assertEquals("Text---", TextUtils.padRight(text, 7, '-'), "String should be padded on right with specified character");

        // Test with ANSI codes
        String withAnsi = "\u001B[31mRed\u001B[0m";
        System.out.println("String with ANSI before: " + withAnsi);
        System.out.println("String with ANSI padRight to width 5: '" + TextUtils.padRight(withAnsi, 5, ' ') + "'");
        assertEquals("\u001B[31mRed\u001B[0m  ", TextUtils.padRight(withAnsi, 5, ' '),
                "String with ANSI codes should be padded based on visible length");

        logger.info("Completed padRight test");
    }
}