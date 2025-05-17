package io.joshuasalcedo.library.prettyconsole.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for converting ANSI-colored console output to HTML.
 * <p>
 * This class provides methods to convert strings containing ANSI escape sequences
 * to properly formatted HTML with equivalent CSS styling.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Convert an ANSI colored string to HTML
 * String ansiText = PrettyConsole.apply(ForegroundColor.RED, "Error: Something went wrong!");
 * String htmlText = AnsiToHtmlConverter.convert(ansiText);
 * 
 * // Save console output to an HTML file
 * String consoleOutput = captureConsoleOutput();
 * AnsiToHtmlConverter.saveToHtml(consoleOutput, "output.html", "Console Output");
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 2.0.0
 */
public class AnsiToHtmlConverter {
    
    /**
     * Converts ANSI escape sequences to HTML spans with CSS classes.
     *
     * @param ansiText The text with ANSI escape sequences
     * @return HTML formatted text with CSS classes
     */
    public static String convert(String ansiText) {
        if (ansiText == null) {
            return "";
        }
        
        StringBuilder html = new StringBuilder();
        StringBuilder currentSpan = new StringBuilder();
        
        boolean inSpan = false;
        boolean bold = false;
        boolean italic = false;
        boolean underline = false;
        boolean strikethrough = false;
        String foregroundColor = null;
        String backgroundColor = null;
        
        // Iterate through the text
        int i = 0;
        while (i < ansiText.length()) {
            // Look for ANSI escape sequence
            if (ansiText.charAt(i) == '\u001B' && i + 1 < ansiText.length() && ansiText.charAt(i + 1) == '[') {
                // End current span if there is one
                if (inSpan) {
                    html.append("</span>");
                    inSpan = false;
                }
                
                // Find the end of the ANSI sequence
                int end = ansiText.indexOf('m', i);
                if (end > i) {
                    // Extract the ANSI code
                    String code = ansiText.substring(i + 2, end);
                    String[] codes = code.split(";");
                    
                    // Reset if code is 0
                    if (code.equals("0")) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        foregroundColor = null;
                        backgroundColor = null;
                    } else {
                        // Process each code
                        for (String c : codes) {
                            int codeNum = 0;
                            try {
                                codeNum = Integer.parseInt(c);
                            } catch (NumberFormatException e) {
                                // Ignore invalid codes
                                continue;
                            }
                            
                            // Style codes
                            if (codeNum == 1) bold = true;
                            else if (codeNum == 3) italic = true;
                            else if (codeNum == 4) underline = true;
                            else if (codeNum == 9) strikethrough = true;
                            
                            // Basic foreground colors
                            else if (codeNum == 30) foregroundColor = "ansi-black";
                            else if (codeNum == 31) foregroundColor = "ansi-red";
                            else if (codeNum == 32) foregroundColor = "ansi-green";
                            else if (codeNum == 33) foregroundColor = "ansi-yellow";
                            else if (codeNum == 34) foregroundColor = "ansi-blue";
                            else if (codeNum == 35) foregroundColor = "ansi-magenta";
                            else if (codeNum == 36) foregroundColor = "ansi-cyan";
                            else if (codeNum == 37) foregroundColor = "ansi-white";
                            
                            // Bright foreground colors
                            else if (codeNum == 90) foregroundColor = "ansi-bright-black";
                            else if (codeNum == 91) foregroundColor = "ansi-bright-red";
                            else if (codeNum == 92) foregroundColor = "ansi-bright-green";
                            else if (codeNum == 93) foregroundColor = "ansi-bright-yellow";
                            else if (codeNum == 94) foregroundColor = "ansi-bright-blue";
                            else if (codeNum == 95) foregroundColor = "ansi-bright-magenta";
                            else if (codeNum == 96) foregroundColor = "ansi-bright-cyan";
                            else if (codeNum == 97) foregroundColor = "ansi-bright-white";
                            
                            // Basic background colors
                            else if (codeNum == 40) backgroundColor = "ansi-bg-black";
                            else if (codeNum == 41) backgroundColor = "ansi-bg-red";
                            else if (codeNum == 42) backgroundColor = "ansi-bg-green";
                            else if (codeNum == 43) backgroundColor = "ansi-bg-yellow";
                            else if (codeNum == 44) backgroundColor = "ansi-bg-blue";
                            else if (codeNum == 45) backgroundColor = "ansi-bg-magenta";
                            else if (codeNum == 46) backgroundColor = "ansi-bg-cyan";
                            else if (codeNum == 47) backgroundColor = "ansi-bg-white";
                            
                            // Bright background colors
                            else if (codeNum == 100) backgroundColor = "ansi-bg-bright-black";
                            else if (codeNum == 101) backgroundColor = "ansi-bg-bright-red";
                            else if (codeNum == 102) backgroundColor = "ansi-bg-bright-green";
                            else if (codeNum == 103) backgroundColor = "ansi-bg-bright-yellow";
                            else if (codeNum == 104) backgroundColor = "ansi-bg-bright-blue";
                            else if (codeNum == 105) backgroundColor = "ansi-bg-bright-magenta";
                            else if (codeNum == 106) backgroundColor = "ansi-bg-bright-cyan";
                            else if (codeNum == 107) backgroundColor = "ansi-bg-bright-white";
                        }
                    }
                    
                    // Skip the processed ANSI sequence
                    i = end + 1;
                    continue;
                }
            }
            
            // Build the span with current styles
            if (!inSpan && (bold || italic || underline || strikethrough || foregroundColor != null || backgroundColor != null)) {
                currentSpan.setLength(0);
                currentSpan.append("<span class=\"");
                
                if (bold) currentSpan.append("ansi-bold ");
                if (italic) currentSpan.append("ansi-italic ");
                if (underline) currentSpan.append("ansi-underline ");
                if (strikethrough) currentSpan.append("ansi-strikethrough ");
                if (foregroundColor != null) currentSpan.append(foregroundColor).append(" ");
                if (backgroundColor != null) currentSpan.append(backgroundColor).append(" ");
                
                // Remove trailing space
                if (currentSpan.length() > 0) {
                    currentSpan.setLength(currentSpan.length() - 1);
                }
                
                currentSpan.append("\">");
                html.append(currentSpan);
                inSpan = true;
            }
            
            // Regular text handling
            char c = ansiText.charAt(i);
            
            // Handle special HTML characters
            if (c == '<') html.append("&lt;");
            else if (c == '>') html.append("&gt;");
            else if (c == '&') html.append("&amp;");
            else html.append(c);
            
            i++;
        }
        
        // Close any open span
        if (inSpan) {
            html.append("</span>");
        }
        
        return html.toString();
    }
    
    /**
     * Saves ANSI-colored console output to an HTML file with default styling.
     *
     * @param ansiText  The text with ANSI escape sequences
     * @param filePath  The path where the HTML file will be saved
     * @param title     The title for the HTML page
     * @throws IOException If there is an error writing the file
     */
    public static void saveToHtml(String ansiText, String filePath, String title) throws IOException {
        saveToHtml(ansiText, filePath, title, getDefaultStylesheet());
    }
    
    /**
     * Saves ANSI-colored console output to an HTML file with custom CSS.
     *
     * @param ansiText   The text with ANSI escape sequences
     * @param filePath   The path where the HTML file will be saved
     * @param title      The title for the HTML page
     * @param stylesheet The CSS stylesheet content
     * @throws IOException If there is an error writing the file
     */
    public static void saveToHtml(String ansiText, String filePath, String title, String stylesheet) throws IOException {
        StringBuilder html = new StringBuilder();
        
        // Create HTML structure
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>").append(escapeHtml(title)).append("</title>\n");
        html.append("    <style>\n");
        html.append(stylesheet);
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>").append(escapeHtml(title)).append("</h1>\n");
        html.append("    <div class=\"terminal\">\n");
        html.append("        <pre>\n");
        
        // Convert ANSI to HTML
        html.append(convert(ansiText));
        
        html.append("        </pre>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        // Write to file
        Path path = Paths.get(filePath);
        Files.writeString(path, html.toString());
    }
    
    /**
     * Gets the default CSS stylesheet for ANSI-to-HTML conversion.
     *
     * @return The default CSS stylesheet
     */
    public static String getDefaultStylesheet() {
        StringBuilder css = new StringBuilder();
        
        // Basic styling
        css.append("body { background-color: #1E1E1E; color: #CCCCCC; font-family: monospace; padding: 20px; }\n");
        css.append(".terminal { background-color: #0C0C0C; border-radius: 5px; padding: 20px; margin: 20px 0; }\n");
        css.append("pre { margin: 0; white-space: pre-wrap; }\n");
        
        // ANSI color to CSS mappings
        css.append(".ansi-black { color: #000000; }\n");
        css.append(".ansi-red { color: #C51E14; }\n");
        css.append(".ansi-green { color: #1DC121; }\n");
        css.append(".ansi-yellow { color: #C7C329; }\n");
        css.append(".ansi-blue { color: #0A2FC4; }\n");
        css.append(".ansi-magenta { color: #C839C5; }\n");
        css.append(".ansi-cyan { color: #20C5C6; }\n");
        css.append(".ansi-white { color: #C7C7C7; }\n");
        css.append(".ansi-bright-black { color: #686868; }\n");
        css.append(".ansi-bright-red { color: #FD6F6B; }\n");
        css.append(".ansi-bright-green { color: #67F86F; }\n");
        css.append(".ansi-bright-yellow { color: #FFFA72; }\n");
        css.append(".ansi-bright-blue { color: #6A76FB; }\n");
        css.append(".ansi-bright-magenta { color: #FD7CFC; }\n");
        css.append(".ansi-bright-cyan { color: #68FDFE; }\n");
        css.append(".ansi-bright-white { color: #FFFFFF; }\n");
        
        // Background colors
        css.append(".ansi-bg-black { background-color: #000000; }\n");
        css.append(".ansi-bg-red { background-color: #C51E14; }\n");
        css.append(".ansi-bg-green { background-color: #1DC121; }\n");
        css.append(".ansi-bg-yellow { background-color: #C7C329; }\n");
        css.append(".ansi-bg-blue { background-color: #0A2FC4; }\n");
        css.append(".ansi-bg-magenta { background-color: #C839C5; }\n");
        css.append(".ansi-bg-cyan { background-color: #20C5C6; }\n");
        css.append(".ansi-bg-white { background-color: #C7C7C7; }\n");
        css.append(".ansi-bg-bright-black { background-color: #686868; }\n");
        css.append(".ansi-bg-bright-red { background-color: #FD6F6B; }\n");
        css.append(".ansi-bg-bright-green { background-color: #67F86F; }\n");
        css.append(".ansi-bg-bright-yellow { background-color: #FFFA72; }\n");
        css.append(".ansi-bg-bright-blue { background-color: #6A76FB; }\n");
        css.append(".ansi-bg-bright-magenta { background-color: #FD7CFC; }\n");
        css.append(".ansi-bg-bright-cyan { background-color: #68FDFE; }\n");
        css.append(".ansi-bg-bright-white { background-color: #FFFFFF; }\n");
        
        // Text styles
        css.append(".ansi-bold { font-weight: bold; }\n");
        css.append(".ansi-italic { font-style: italic; }\n");
        css.append(".ansi-underline { text-decoration: underline; }\n");
        css.append(".ansi-strikethrough { text-decoration: line-through; }\n");
        
        return css.toString();
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
     * Private constructor to prevent instantiation.
     */
    private AnsiToHtmlConverter() {
        // Utility class should not be instantiated
    }
}