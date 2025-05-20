package io.joshuasalcedo.pretty.core.theme;

import io.joshuasalcedo.pretty.core.properties.RGBColor;

/**
 * An enum that defines terminal color styles for different message categories
 * with precise RGB color definitions. These styles can be applied to text output
 * in terminal applications.
 */
public enum TerminalStyle {
    
    //-------------------------------------------------------------------------
    // System Status Styles
    //-------------------------------------------------------------------------
    
    /**
     * Standard error style with bright red text.
     */
    ERROR(RGBColor.of(255, 50, 50).withName("error-red")),
    
    /**
     * Critical error style with white text on red background for maximum visibility.
     */
    CRITICAL_ERROR(RGBColor.of(255, 255, 255).withName("white"), 
                   RGBColor.of(200, 0, 0).withName("critical-red")),
    
    /**
     * Warning style with amber yellow text.
     */
    WARNING(RGBColor.of(255, 180, 0).withName("warning-yellow")),
    
    /**
     * Standard information style with blue text.
     */
    INFO(RGBColor.of(0, 150, 255).withName("info-blue")),
    
    /**
     * Success style with green text.
     */
    SUCCESS(RGBColor.of(0, 200, 0).withName("success-green")),
    
    /**
     * Debug message style with purple text.
     */
    DEBUG(RGBColor.of(180, 120, 200).withName("debug-purple")),
    
    /**
     * Trace message style with gray text.
     */
    TRACE(RGBColor.of(150, 150, 150).withName("trace-gray")),
    
    //-------------------------------------------------------------------------
    // Database Styles
    //-------------------------------------------------------------------------
    
    /**
     * Database error style with deep red text.
     */
    DB_ERROR(RGBColor.of(220, 50, 50).withName("db-error-red")),
    
    /**
     * Database success style with teal green text.
     */
    DB_SUCCESS(RGBColor.of(0, 180, 140).withName("db-success-teal")),
    
    /**
     * Database warning style with dark amber text.
     */
    DB_WARNING(RGBColor.of(230, 150, 0).withName("db-warning-amber")),
    
    /**
     * Database info style with dark blue text.
     */
    DB_INFO(RGBColor.of(0, 140, 220).withName("db-info-blue")),
    
    //-------------------------------------------------------------------------
    // Network Styles
    //-------------------------------------------------------------------------
    
    /**
     * Network error style with crimson text.
     */
    NETWORK_ERROR(RGBColor.of(220, 30, 70).withName("network-error-crimson")),
    
    /**
     * Network success style with seafoam green text.
     */
    NETWORK_SUCCESS(RGBColor.of(0, 200, 120).withName("network-success-seafoam")),
    
    /**
     * Network warning style with gold text.
     */
    NETWORK_WARNING(RGBColor.of(240, 200, 0).withName("network-warning-gold")),
    
    /**
     * Network info style with azure blue text.
     */
    NETWORK_INFO(RGBColor.of(30, 140, 200).withName("network-info-azure")),
    
    //-------------------------------------------------------------------------
    // Security Styles
    //-------------------------------------------------------------------------
    
    /**
     * Security alert style with white text on bright red background.
     */
    SECURITY_ALERT(RGBColor.of(255, 255, 255).withName("white"),
                   RGBColor.of(255, 0, 0).withName("alert-red")),
    
    /**
     * Security warning style with black text on yellow background.
     */
    SECURITY_WARNING(RGBColor.of(0, 0, 0).withName("black"),
                     RGBColor.of(255, 240, 0).withName("security-yellow")),
    
    /**
     * Security info style with navy blue text.
     */
    SECURITY_INFO(RGBColor.of(30, 60, 180).withName("security-info-navy")),
    
    /**
     * Authentication success style with green text.
     */
    AUTHENTICATION_SUCCESS(RGBColor.of(20, 180, 20).withName("auth-success-green")),
    
    /**
     * Authentication failure style with red text.
     */
    AUTHENTICATION_FAILURE(RGBColor.of(220, 0, 0).withName("auth-failure-red")),
    
    //-------------------------------------------------------------------------
    // API Styles
    //-------------------------------------------------------------------------
    
    /**
     * API error style with brick red text.
     */
    API_ERROR(RGBColor.of(200, 60, 60).withName("api-error-red")),
    
    /**
     * API success style with bright green text.
     */
    API_SUCCESS(RGBColor.of(40, 200, 40).withName("api-success-green")),
    
    /**
     * API warning style with mustard text.
     */
    API_WARNING(RGBColor.of(220, 180, 0).withName("api-warning-mustard")),
    
    /**
     * API info style with sky blue text.
     */
    API_INFO(RGBColor.of(70, 180, 230).withName("api-info-sky")),
    
    //-------------------------------------------------------------------------
    // UI Element Styles
    //-------------------------------------------------------------------------
    
    /**
     * UI header style with bright white bold text.
     */
    UI_HEADER(RGBColor.of(255, 255, 255).withName("header-white")),
    
    /**
     * UI subheader style with light gray text.
     */
    UI_SUBHEADER(RGBColor.of(220, 220, 220).withName("subheader-light-gray")),
    
    /**
     * UI label style with cyan text.
     */
    UI_LABEL(RGBColor.of(0, 200, 200).withName("label-cyan")),
    
    /**
     * UI value style with white text.
     */
    UI_VALUE(RGBColor.of(240, 240, 240).withName("value-white")),
    
    /**
     * UI link style with bright cyan text.
     */
    UI_LINK(RGBColor.of(0, 240, 240).withName("link-cyan")),
    
    /**
     * Clickable hyperlink style with blue text and underline.
     * Uses the OSC 8 hyperlink ANSI sequence for terminal support.
     */
    HYPERLINK(RGBColor.of(0, 120, 255).withName("hyperlink-blue")),
    
    /**
     * UI button style with black text on green background.
     */
    UI_BUTTON(RGBColor.of(0, 0, 0).withName("black"),
              RGBColor.of(100, 220, 100).withName("button-green")),
    
    /**
     * UI menu style with white text on blue background.
     */
    UI_MENU(RGBColor.of(255, 255, 255).withName("white"),
            RGBColor.of(50, 100, 200).withName("menu-blue")),
    
    //-------------------------------------------------------------------------
    // Documentation Styles
    //-------------------------------------------------------------------------
    
    /**
     * Documentation title style with bright white text.
     */
    DOC_TITLE(RGBColor.of(255, 255, 255).withName("doc-title-white")),
    
    /**
     * Documentation section style with light blue text.
     */
    DOC_SECTION(RGBColor.of(150, 200, 255).withName("doc-section-light-blue")),
    
    /**
     * Documentation subsection style with light cyan text.
     */
    DOC_SUBSECTION(RGBColor.of(150, 220, 220).withName("doc-subsection-light-cyan")),
    
    /**
     * Documentation code style with white text on dark gray background.
     */
    DOC_CODE(RGBColor.of(240, 240, 240).withName("code-white"),
             RGBColor.of(40, 40, 40).withName("code-dark-gray")),
    
    /**
     * Documentation note style with blue text.
     */
    DOC_NOTE(RGBColor.of(100, 150, 250).withName("doc-note-blue")),
    
    /**
     * Documentation warning style with yellow text.
     */
    DOC_WARNING(RGBColor.of(240, 200, 0).withName("doc-warning-yellow")),
    
    /**
     * Documentation tip style with green text.
     */
    DOC_TIP(RGBColor.of(100, 200, 100).withName("doc-tip-green")),
    
    //-------------------------------------------------------------------------
    // Development Styles
    //-------------------------------------------------------------------------
    
    /**
     * Development TODO style with yellow text.
     */
    DEV_TODO(RGBColor.of(240, 200, 0).withName("todo-yellow")),
    
    /**
     * Development FIXME style with red text.
     */
    DEV_FIXME(RGBColor.of(255, 60, 60).withName("fixme-red")),
    
    /**
     * Development note style with teal text.
     */
    DEV_NOTE(RGBColor.of(0, 200, 200).withName("dev-note-teal")),
    
    /**
     * Deprecated warning style with black text on yellow background.
     */
    DEV_DEPRECATED(RGBColor.of(0, 0, 0).withName("black"),
                   RGBColor.of(240, 200, 0).withName("deprecated-yellow")),
    
    //-------------------------------------------------------------------------
    // Testing Styles
    //-------------------------------------------------------------------------
    
    /**
     * Test passed style with green text.
     */
    TEST_PASSED(RGBColor.of(40, 200, 40).withName("test-passed-green")),
    
    /**
     * Test failed style with red text.
     */
    TEST_FAILED(RGBColor.of(220, 50, 50).withName("test-failed-red")),
    
    /**
     * Test skipped style with yellow text.
     */
    TEST_SKIPPED(RGBColor.of(220, 180, 0).withName("test-skipped-yellow")),
    
    /**
     * Test info style with blue text.
     */
    TEST_INFO(RGBColor.of(80, 150, 220).withName("test-info-blue")),
    
    //-------------------------------------------------------------------------
    // Performance Styles
    //-------------------------------------------------------------------------
    
    /**
     * Performance critical style with white text on red background.
     */
    PERF_CRITICAL(RGBColor.of(255, 255, 255).withName("white"),
                  RGBColor.of(220, 0, 0).withName("perf-critical-red")),
    
    /**
     * Performance warning style with orange text.
     */
    PERF_WARNING(RGBColor.of(255, 150, 0).withName("perf-warning-orange")),
    
    /**
     * Performance info style with blue text.
     */
    PERF_INFO(RGBColor.of(50, 160, 220).withName("perf-info-blue")),
    
    //-------------------------------------------------------------------------
    // Validation Styles
    //-------------------------------------------------------------------------
    
    /**
     * Validation error style with red text.
     */
    VALIDATION_ERROR(RGBColor.of(220, 50, 50).withName("validation-error-red")),
    
    /**
     * Validation warning style with orange text.
     */
    VALIDATION_WARNING(RGBColor.of(255, 150, 0).withName("validation-warning-orange")),
    
    /**
     * Validation success style with green text.
     */
    VALIDATION_SUCCESS(RGBColor.of(40, 200, 40).withName("validation-success-green")),
    
    //-------------------------------------------------------------------------
    // Configuration Styles
    //-------------------------------------------------------------------------
    
    /**
     * Configuration error style with red text.
     */
    CONFIG_ERROR(RGBColor.of(220, 50, 50).withName("config-error-red")),
    
    /**
     * Configuration warning style with orange text.
     */
    CONFIG_WARNING(RGBColor.of(255, 150, 0).withName("config-warning-orange")),
    
    /**
     * Configuration info style with blue text.
     */
    CONFIG_INFO(RGBColor.of(50, 160, 220).withName("config-info-blue")),
    
    //-------------------------------------------------------------------------
    // Custom Emphasis Styles
    //-------------------------------------------------------------------------
    
    /**
     * Highlight style with black text on yellow background.
     */
    HIGHLIGHT(RGBColor.of(0, 0, 0).withName("black"),
              RGBColor.of(255, 255, 0).withName("highlight-yellow")),
    
    /**
     * Emphasis style with light blue text.
     */
    EMPHASIS(RGBColor.of(100, 200, 255).withName("emphasis-light-blue")),
    
    /**
     * Important text style with bold red text.
     */
    IMPORTANT(RGBColor.of(255, 80, 80).withName("important-red")),
    
    /**
     * Secondary importance text style with light gray text.
     */
    SECONDARY(RGBColor.of(200, 200, 200).withName("secondary-light-gray")),
    
    /**
     * Tertiary importance text style with darker gray text.
     */
    TERTIARY(RGBColor.of(150, 150, 150).withName("tertiary-gray")),
    
    /**
     * Plain text style without any special formatting.
     * Default terminal text color.
     */
    PLAIN(RGBColor.of(220, 220, 220).withName("plain-white"));
    
    private final RGBColor foreground;
    private final RGBColor background;
    private final String prefix;
    
    /**
     * Constructor for styles with foreground color only.
     *
     * @param foreground The foreground (text) color
     */
    TerminalStyle(RGBColor foreground) {
        this(foreground, null, null);
    }
    
    /**
     * Constructor for styles with foreground and background colors.
     *
     * @param foreground The foreground (text) color
     * @param background The background color
     */
    TerminalStyle(RGBColor foreground, RGBColor background) {
        this(foreground, background, null);
    }
    
    /**
     * Constructor for styles with foreground, background, and prefix.
     *
     * @param foreground The foreground (text) color
     * @param background The background color
     * @param prefix The prefix to add before the text (can be null)
     */
    TerminalStyle(RGBColor foreground, RGBColor background, String prefix) {
        this.foreground = foreground;
        this.background = background;
        this.prefix = prefix;
    }
    
    /**
     * Returns the foreground (text) color for this style.
     *
     * @return The foreground RGBColor
     */
    public RGBColor getForeground() {
        return foreground;
    }
    
    /**
     * Returns the background color for this style.
     *
     * @return The background RGBColor or null if no background is set
     */
    public RGBColor getBackground() {
        return background;
    }
    
    /**
     * Returns the prefix for this style.
     *
     * @return The prefix string or null if no prefix is set
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * Applies this style to the given text.
     *
     * @param text The text to style
     * @return The styled text with ANSI color codes
     */
    public String apply(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        String result = text;
        
        // Add prefix if specified
        if (prefix != null && !prefix.isEmpty()) {
            result = prefix + " " + result;
        }
        
        // Apply foreground color
        result = foreground.apply(result);
        
        // Apply background color if specified
        if (background != null) {
            // We need to remove the reset at the end of the foreground application,
            // apply the background, and then add the reset back
            result = result.substring(0, result.length() - 4);  // Remove reset sequence "\u001B[0m"
            result = background.asBackground().apply(result);
        }
        
        return result;
    }
    
    /**
     * Helper method to get a TerminalStyle by its name.
     *
     * @param name The name of the style to get (case insensitive)
     * @return The matching TerminalStyle or PLAIN if not found
     */
    public static TerminalStyle getByName(String name) {
        if (name == null || name.isEmpty()) {
            return PLAIN;
        }
        
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PLAIN;
        }
    }
    
    /**
     * Demonstrates all terminal styles.
     */
    public static void main(String[] args) {
        System.out.println("Terminal Style Demonstration\n");
        
        // Group styles by category
        TerminalStyle[][] categories = {
            // System Status
            {ERROR, CRITICAL_ERROR, WARNING, INFO, SUCCESS, DEBUG, TRACE},
            // Database
            {DB_ERROR, DB_SUCCESS, DB_WARNING, DB_INFO},
            // Network
            {NETWORK_ERROR, NETWORK_SUCCESS, NETWORK_WARNING, NETWORK_INFO},
            // Security
            {SECURITY_ALERT, SECURITY_WARNING, SECURITY_INFO, AUTHENTICATION_SUCCESS, AUTHENTICATION_FAILURE},
            // API
            {API_ERROR, API_SUCCESS, API_WARNING, API_INFO},
            // UI Elements
            {UI_HEADER, UI_SUBHEADER, UI_LABEL, UI_VALUE, UI_LINK, UI_BUTTON, UI_MENU},
            // Documentation
            {DOC_TITLE, DOC_SECTION, DOC_SUBSECTION, DOC_CODE, DOC_NOTE, DOC_WARNING, DOC_TIP},
            // Development
            {DEV_TODO, DEV_FIXME, DEV_NOTE, DEV_DEPRECATED},
            // Testing
            {TEST_PASSED, TEST_FAILED, TEST_SKIPPED, TEST_INFO},
            // Performance
            {PERF_CRITICAL, PERF_WARNING, PERF_INFO},
            // Validation
            {VALIDATION_ERROR, VALIDATION_WARNING, VALIDATION_SUCCESS},
            // Configuration
            {CONFIG_ERROR, CONFIG_WARNING, CONFIG_INFO},
            // Custom Emphasis
            {HIGHLIGHT, EMPHASIS, IMPORTANT, SECONDARY, TERTIARY, PLAIN}
        };
        
        String[] categoryNames = {
            "System Status", "Database", "Network", "Security", "API", 
            "UI Elements", "Documentation", "Development", "Testing", 
            "Performance", "Validation", "Configuration", "Custom Emphasis"
        };
        
        // Print each category
        for (int i = 0; i < categories.length; i++) {
            System.out.println(UI_HEADER.apply(categoryNames[i] + " Styles:"));
            for (TerminalStyle style : categories[i]) {
                String colorInfo = String.format("RGB(%d,%d,%d)", 
                    style.getForeground().getRed(),
                    style.getForeground().getGreen(), 
                    style.getForeground().getBlue());
                    
                System.out.println(style.apply(String.format("%-20s", style.name())) + 
                                   " - " + colorInfo + 
                                   (style.getBackground() != null ? " (with background)" : ""));
            }
            System.out.println();
        }
        
        // Show an example using multiple styles
        System.out.println(UI_HEADER.apply("Example Application Output:"));
        System.out.println(INFO.apply("[INFO] System initializing..."));
        System.out.println(DB_INFO.apply("[DB] Connecting to database server..."));
        System.out.println(DB_SUCCESS.apply("[DB] Connected successfully."));
        System.out.println(WARNING.apply("[WARN] Low disk space detected (15% remaining)."));
        System.out.println(NETWORK_INFO.apply("[NET] Listening on port 8080"));
        System.out.println(ERROR.apply("[ERROR] Failed to process request: Invalid input parameter"));
        System.out.println(SUCCESS.apply("[OK] Service started successfully"));
        System.out.println(SECURITY_ALERT.apply("[SECURITY] Unauthorized access attempt detected"));
        System.out.println(UI_BUTTON.apply("[ Continue ]") + " " + UI_BUTTON.apply("[ Cancel ]"));
        System.out.println(UI_LINK.apply("Click here for more information"));
    }
}