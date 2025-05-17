package io.joshuasalcedo.library.prettyconsole.utils;

import io.joshuasalcedo.library.prettyconsole.style.core.AnsiConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for converting ANSI-colored console output to Markdown with HTML spans.
 * <p>
 * This class provides methods to convert strings containing ANSI escape sequences
 * to properly formatted Markdown with HTML spans for use in GitHub and other Markdown
 * platforms that support HTML.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Convert an ANSI colored string to Markdown
 * String ansiText = PrettyConsole.apply(ForegroundColor.RED, "Error: Something went wrong!");
 * String mdText = AnsiToMarkdownConverter.convert(ansiText);
 * 
 * // Save console output to a Markdown file
 * String consoleOutput = captureConsoleOutput();
 * AnsiToMarkdownConverter.saveToMarkdown(consoleOutput, "output.md", "Console Output");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class AnsiToMarkdownConverter {
    
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[(\\d+(?:;\\d+)*)m");
    
    // ANSI code to CSS style mapping
    private static final Map<Integer, String> STYLES_MAP = new HashMap<>();
    
    static {
        // Text styles
        STYLES_MAP.put(1, "font-weight: bold;");
        STYLES_MAP.put(3, "font-style: italic;");
        STYLES_MAP.put(4, "text-decoration: underline;");
        STYLES_MAP.put(9, "text-decoration: line-through;");
        
        // Foreground colors (standard)
        STYLES_MAP.put(30, "color: #000000;");
        STYLES_MAP.put(31, "color: #C51E14;");
        STYLES_MAP.put(32, "color: #1DC121;");
        STYLES_MAP.put(33, "color: #C7C329;");
        STYLES_MAP.put(34, "color: #0A2FC4;");
        STYLES_MAP.put(35, "color: #C839C5;");
        STYLES_MAP.put(36, "color: #20C5C6;");
        STYLES_MAP.put(37, "color: #C7C7C7;");
        
        // Foreground colors (bright)
        STYLES_MAP.put(90, "color: #686868;");
        STYLES_MAP.put(91, "color: #FD6F6B;");
        STYLES_MAP.put(92, "color: #67F86F;");
        STYLES_MAP.put(93, "color: #FFFA72;");
        STYLES_MAP.put(94, "color: #6A76FB;");
        STYLES_MAP.put(95, "color: #FD7CFC;");
        STYLES_MAP.put(96, "color: #68FDFE;");
        STYLES_MAP.put(97, "color: #FFFFFF;");
        
        // Background colors (standard)
        STYLES_MAP.put(40, "background-color: #000000;");
        STYLES_MAP.put(41, "background-color: #C51E14;");
        STYLES_MAP.put(42, "background-color: #1DC121;");
        STYLES_MAP.put(43, "background-color: #C7C329;");
        STYLES_MAP.put(44, "background-color: #0A2FC4;");
        STYLES_MAP.put(45, "background-color: #C839C5;");
        STYLES_MAP.put(46, "background-color: #20C5C6;");
        STYLES_MAP.put(47, "background-color: #C7C7C7;");
        
        // Background colors (bright)
        STYLES_MAP.put(100, "background-color: #686868;");
        STYLES_MAP.put(101, "background-color: #FD6F6B;");
        STYLES_MAP.put(102, "background-color: #67F86F;");
        STYLES_MAP.put(103, "background-color: #FFFA72;");
        STYLES_MAP.put(104, "background-color: #6A76FB;");
        STYLES_MAP.put(105, "background-color: #FD7CFC;");
        STYLES_MAP.put(106, "background-color: #68FDFE;");
        STYLES_MAP.put(107, "background-color: #FFFFFF;");
    }
    
    /**
     * Converts ANSI escape sequences to Markdown with HTML spans.
     *
     * @param ansiText The text with ANSI escape sequences
     * @return Markdown formatted text with HTML spans for colored text
     */
    public static String convert(String ansiText) {
        if (ansiText == null || ansiText.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        int lastProcessedIndex = 0;
        
        Matcher matcher = ANSI_PATTERN.matcher(ansiText);
        String activeStyles = "";
        boolean isStyleActive = false;
        
        while (matcher.find()) {
            // Append text before this ANSI sequence
            result.append(escapeHtml(ansiText.substring(lastProcessedIndex, matcher.start())));
            
            // Close any active style
            if (isStyleActive) {
                result.append("</span>");
                isStyleActive = false;
            }
            
            // Extract ANSI codes
            String codes = matcher.group(1);
            
            // Reset if code is 0
            if (codes.equals("0")) {
                activeStyles = "";
            } else {
                // Build the style string
                StringBuilder styleBuilder = new StringBuilder();
                
                for (String code : codes.split(";")) {
                    try {
                        int codeNum = Integer.parseInt(code);
                        String style = STYLES_MAP.get(codeNum);
                        
                        if (style != null) {
                            styleBuilder.append(style).append(" ");
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid codes
                    }
                }
                
                if (styleBuilder.length() > 0) {
                    activeStyles = styleBuilder.toString();
                }
            }
            
            // Start a new span if we have styles
            if (!activeStyles.isEmpty()) {
                result.append("<span style=\"").append(activeStyles).append("\">");
                isStyleActive = true;
            }
            
            lastProcessedIndex = matcher.end();
        }
        
        // Add the remaining text
        result.append(escapeHtml(ansiText.substring(lastProcessedIndex)));
        
        // Close any open span
        if (isStyleActive) {
            result.append("</span>");
        }
        
        return result.toString();
    }
    
    /**
     * Saves ANSI-colored console output to a Markdown file.
     *
     * @param ansiText The text with ANSI escape sequences
     * @param filePath The path where the Markdown file will be saved
     * @param title    The title for the Markdown document
     * @throws IOException If there is an error writing the file
     */
    public static void saveToMarkdown(String ansiText, String filePath, String title) throws IOException {
        StringBuilder md = new StringBuilder();
        
        // Add title
        md.append("# ").append(title).append("\n\n");
        
        // Add info about the document
        md.append("> This document contains terminal output with colors preserved using HTML spans.\n");
        md.append("> It was generated using the PrettyConsole library's AnsiToMarkdownConverter.\n\n");
        
        // Add the terminal output in a code block
        md.append("```html\n");
        md.append(convert(ansiText)).append("\n");
        md.append("```\n\n");
        
        // Add the terminal output directly (for GitHub rendering)
        md.append("## Rendered Output\n\n");
        md.append("<pre style=\"background-color: #0C0C0C; color: #CCCCCC; padding: 10px; border-radius: 5px;\">\n");
        md.append(convert(ansiText)).append("\n");
        md.append("</pre>\n");
        
        // Write to file
        Path path = Paths.get(filePath);
        Files.writeString(path, md.toString());
    }
    
    /**
     * Escapes HTML special characters.
     *
     * @param text The text to escape
     * @return The escaped text
     */
    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    
    /**
     * Removes all ANSI escape sequences from a string.
     *
     * @param ansiText The text with ANSI codes
     * @return The text without any ANSI codes
     */
    public static String stripAnsi(String ansiText) {
        if (ansiText == null) {
            return null;
        }
        return ANSI_PATTERN.matcher(ansiText).replaceAll("");
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private AnsiToMarkdownConverter() {
        // Utility class should not be instantiated
    }
}