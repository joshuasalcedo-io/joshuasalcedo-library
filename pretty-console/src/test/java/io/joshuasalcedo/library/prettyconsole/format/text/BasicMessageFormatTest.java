package io.joshuasalcedo.library.prettyconsole.format.text;

import io.joshuasalcedo.library.prettyconsole.PrettyStyle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicMessageFormatTest {

    @Test
    public void testDefaultFormatting() {
        // Test formatting with default settings for various message types
        BasicMessageFormat errorFormat = new BasicMessageFormat(MessageType.ERROR);
        String formattedError = errorFormat.format("Error message");

        // We can't easily test the actual ANSI codes, but we can check that formatting was applied
        // and the prefix is present
        assertTrue(formattedError.contains("[ERROR]"));
        assertTrue(formattedError.contains("Error message"));
    }

    @Test
    public void testCustomFormatting() {
        // Test custom formatting options
        BasicMessageFormat customFormat = new BasicMessageFormat(
                MessageType.INFO,
                PrettyStyle.Color.GREEN,
                PrettyStyle.Style.BOLD,
                PrettyStyle.Background.BLUE,
                "Custom: ",
                true
        );

        String formatted = customFormat.format("Test message");
        assertTrue(formatted.contains("Custom: "));
        assertTrue(formatted.contains("Test message"));
    }

    @Test
    public void testPrefixToggle() {
        // Test enabling/disabling prefix
        BasicMessageFormat formatWithPrefix = new BasicMessageFormat(MessageType.WARNING);
        assertTrue(formatWithPrefix.hasPrefixEnabled());
        String withPrefix = formatWithPrefix.format("Warning message");
        assertTrue(withPrefix.contains("[WARNING]"));

        // Disable prefix
        BasicMessageFormat formatWithoutPrefix = (BasicMessageFormat) formatWithPrefix.withPrefix(false);
        assertFalse(formatWithoutPrefix.hasPrefixEnabled());
        String withoutPrefix = formatWithoutPrefix.format("Warning message");
        assertFalse(withoutPrefix.contains("[WARNING]"));
    }

    @Test
    public void testMessageTypeGetterAndDefaults() {
        // Test getMessageType and default style mappings
        BasicMessageFormat successFormat = new BasicMessageFormat(MessageType.SUCCESS);
        assertEquals(MessageType.SUCCESS, successFormat.getMessageType());

        // Check that formatting is applied (we can't easily verify the exact ANSI codes)
        String formatted = successFormat.format("Success message");
        assertTrue(formatted.contains("[SUCCESS]"));
    }

    @Test
    public void testSpecialMessageTypes() {
        // Test special message types like test results
        BasicMessageFormat testPassedFormat = new BasicMessageFormat(MessageType.TEST_PASSED);
        String passedMessage = testPassedFormat.format("Test passed");
        assertTrue(passedMessage.contains("✓"));

        BasicMessageFormat testFailedFormat = new BasicMessageFormat(MessageType.TEST_FAILED);
        String failedMessage = testFailedFormat.format("Test failed");
        assertTrue(failedMessage.contains("✗"));
    }

    @Test
    public void testEmptyMessage() {
        // Test formatting empty message
        BasicMessageFormat infoFormat = new BasicMessageFormat(MessageType.INFO);
        String formatted = infoFormat.format("");
        assertTrue(formatted.contains("[INFO]"));

        // Test with prefix disabled
        BasicMessageFormat noPrefix = (BasicMessageFormat) infoFormat.withPrefix(false);
        String formattedNoPrefix = noPrefix.format("");
        assertEquals("", formattedNoPrefix.trim());
    }

    @Test
    public void testNullHandling() {
        // Test handling null message (this depends on how PrettyStyle handles nulls)
        BasicMessageFormat debugFormat = new BasicMessageFormat(MessageType.DEBUG);
        String formatted = debugFormat.format(null);

        // The behavior depends on PrettyStyle implementation, but it should not throw exceptions
        assertNotNull(formatted);
    }

    @Test
    public void testCriticalErrorFormatting() {
        // Test critical error which should have both color and background
        BasicMessageFormat criticalFormat = new BasicMessageFormat(MessageType.CRITICAL_ERROR);
        String formatted = criticalFormat.format("System failure");

        assertTrue(formatted.contains("[CRITICAL]"));
        assertTrue(formatted.contains("System failure"));
    }

    @Test
    public void testPlainMessageType() {
        // Test PLAIN message type which should have minimal formatting
        BasicMessageFormat plainFormat = new BasicMessageFormat(MessageType.PLAIN);
        String formatted = plainFormat.format("Plain text");

        // PLAIN should not have a prefix
        assertEquals("Plain text", formatted.trim());
    }

    @Test
    public void testMultilineFormatting() {
        // Test formatting multiline messages
        BasicMessageFormat infoFormat = new BasicMessageFormat(MessageType.INFO);
        String multiline = "Line 1\nLine 2\nLine 3";
        String formatted = infoFormat.format(multiline);

        // Prefix should only appear once
        assertTrue(formatted.contains("[INFO]"));
        assertTrue(formatted.contains("Line 1"));
        assertTrue(formatted.contains("Line 2"));
        assertTrue(formatted.contains("Line 3"));
    }
}