# Spring Boot Auto-Configuration Guide for Pretty Java Console

This guide demonstrates how to integrate Pretty Java Console into a Spring Boot application with auto-configuration, allowing you to override the default `System.out` streams with the enhanced `PrettyPrintStream`.

## 1. Create the Auto-Configuration Classes

First, create the necessary auto-configuration classes in your Spring Boot project. Let's start with the configuration properties.

### Step 1: Create Configuration Properties Class

```java
package io.joshuasalcedo.pretty.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pretty.console")
public class PrettyConsoleProperties {
    
    /**
     * Enable or disable Pretty Console auto-configuration
     */
    private boolean enabled = true;
    
    /**
     * Whether to override System.out and System.err
     */
    private boolean overrideSystemOut = true;
    
    /**
     * Default color for normal output (as RGB hex string)
     */
    private String defaultColor = "#cccccc";
    
    /**
     * Default color for error output (as RGB hex string)
     */
    private String errorColor = "#ff5050";
    
    // Getters and setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isOverrideSystemOut() {
        return overrideSystemOut;
    }
    
    public void setOverrideSystemOut(boolean overrideSystemOut) {
        this.overrideSystemOut = overrideSystemOut;
    }
    
    public String getDefaultColor() {
        return defaultColor;
    }
    
    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }
    
    public String getErrorColor() {
        return errorColor;
    }
    
    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }
}
```

### Step 2: Create Auto-Configuration Class

```java
package io.joshuasalcedo.pretty.spring;

import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;

@Configuration
@ConditionalOnClass(PrettyPrintStream.class)
@EnableConfigurationProperties(PrettyConsoleProperties.class)
@ConditionalOnProperty(prefix = "pretty.console", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PrettyConsoleAutoConfiguration {

    private final PrettyConsoleProperties properties;
    
    public PrettyConsoleAutoConfiguration(PrettyConsoleProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public PrettyPrintStream prettyPrintStream() {
        // Create the main PrettyPrintStream instance
        PrettyPrintStream prettyPrintStream = new PrettyPrintStream(System.out);
        
        // Parse default color from properties
        if (properties.getDefaultColor() != null && !properties.getDefaultColor().isEmpty()) {
            try {
                RGBColor defaultColor = parseHexColor(properties.getDefaultColor());
                prettyPrintStream.foreground(defaultColor);
            } catch (IllegalArgumentException e) {
                // Log warning about invalid color format
                System.err.println("Warning: Invalid default color format in properties: " + properties.getDefaultColor());
            }
        }
        
        // If system override is enabled, replace System.out
        if (properties.isOverrideSystemOut() && TerminalUtils.isAnsiSupported()) {
            System.setOut(prettyPrintStream);
            
            // Also create and set a PrettyPrintStream for System.err
            RGBColor errorColor = parseHexColor(properties.getErrorColor());
            PrettyPrintStream prettyErrorStream = new PrettyPrintStream(System.err);
            prettyErrorStream.foreground(errorColor);
            System.setErr(prettyErrorStream);
        }
        
        return prettyPrintStream;
    }
    
    /**
     * Helper method to parse hex color strings
     */
    private RGBColor parseHexColor(String hexColor) {
        if (hexColor == null || hexColor.isEmpty()) {
            return RGBColor.of(204, 204, 204); // Default to light gray
        }
        
        // Remove # if present
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        
        try {
            int r, g, b;
            if (hexColor.length() == 6) {
                r = Integer.parseInt(hexColor.substring(0, 2), 16);
                g = Integer.parseInt(hexColor.substring(2, 4), 16);
                b = Integer.parseInt(hexColor.substring(4, 6), 16);
                return RGBColor.of(r, g, b);
            } else {
                throw new IllegalArgumentException("Invalid hex color format: " + hexColor);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color format: " + hexColor, e);
        }
    }
    
    /**
     * Create a console service bean that provides formatted output methods
     */
    @Bean
    @ConditionalOnMissingBean
    public PrettyConsoleService prettyConsoleService(PrettyPrintStream prettyPrintStream) {
        return new PrettyConsoleService(prettyPrintStream);
    }
}
```

### Step 3: Create Console Service Class

```java
package io.joshuasalcedo.pretty.spring;

import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

/**
 * Service for Spring applications to use Pretty Console functionalities
 */
public class PrettyConsoleService {
    
    private final PrettyPrintStream console;
    
    public PrettyConsoleService(PrettyPrintStream console) {
        this.console = console;
    }
    
    /**
     * Print a success message
     */
    public void success(String message) {
        console.println(TerminalStyle.SUCCESS, message);
    }
    
    /**
     * Print an error message
     */
    public void error(String message) {
        console.println(TerminalStyle.ERROR, message);
    }
    
    /**
     * Print a warning message
     */
    public void warning(String message) {
        console.println(TerminalStyle.WARNING, message);
    }
    
    /**
     * Print an info message
     */
    public void info(String message) {
        console.println(TerminalStyle.INFO, message);
    }
    
    /**
     * Print a custom colored message
     */
    public void print(int r, int g, int b, String message) {
        console.println(RGBColor.of(r, g, b), message);
    }
    
    /**
     * Print a hyperlink
     */
    public void link(String url, String text) {
        console.printlnHyperlink(url, text);
    }
    
    /**
     * Get the underlying PrettyPrintStream for advanced usage
     */
    public PrettyPrintStream getConsole() {
        return console;
    }
}
```

### Step 4: Create Spring Factory Config File

Create a file at `src/main/resources/META-INF/spring.factories`:

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
io.joshuasalcedo.pretty.spring.PrettyConsoleAutoConfiguration
```

## 2. Using the Pretty Console in a Spring Boot Application

After setting up the auto-configuration, you can use the Pretty Console in your Spring Boot application as follows:

### Application Properties

Configure Pretty Console in your `application.properties` or `application.yml`:

```properties
# Enable/disable Pretty Console (default is true)
pretty.console.enabled=true

# Override System.out and System.err (default is true)
pretty.console.override-system-out=true

# Default color for standard output (hex RGB)
pretty.console.default-color=#a0e0ff

# Default color for error output (hex RGB)
pretty.console.error-color=#ff5050
```

### Using the PrettyConsoleService

Inject the service into your components:

```java
import io.joshuasalcedo.pretty.spring.PrettyConsoleService;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    
    private final PrettyConsoleService console;
    
    public MyService(PrettyConsoleService console) {
        this.console = console;
    }
    
    public void doSomething() {
        console.info("Starting process...");
        
        // Some business logic
        
        console.success("Process completed successfully!");
        
        // Print a clickable link
        console.link("https://example.com/docs", "View documentation");
    }
}
```

### Using System.out Directly

If you've enabled system output override, you can use the standard `System.out.println()` and it will be automatically enhanced:

```java
@Service
public class LegacyService {
    
    public void oldMethod() {
        // This will use PrettyPrintStream under the hood
        System.out.println("This is automatically pretty!");
        
        // Error messages will use the error color
        System.err.println("This is an error message");
    }
}
```

## 3. Creating a Spring Boot Starter

To make it easy for others to use your Pretty Console with Spring Boot, you can package it as a starter.

1. Create a new Maven project called `pretty-java-spring-boot-starter`
2. Add the following dependencies to the `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>io.joshuasalcedo.library</groupId>
        <artifactId>pretty-java-core</artifactId>
        <version>${pretty-java.version}</version>
    </dependency>
</dependencies>
```

3. Include the auto-configuration classes from steps 1-3 in this project.
4. Create the `spring.factories` file as described in step 4.
5. Build and publish to your repository.

## 4. Integration With Logging Frameworks

### Integrating with Logback

To override Logback (the default logging framework in Spring Boot), create a custom appender:

```java
package io.joshuasalcedo.pretty.spring.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

public class PrettyConsoleAppender extends AppenderBase<ILoggingEvent> {
    
    private final PrettyPrintStream console;
    
    public PrettyConsoleAppender(PrettyPrintStream console) {
        this.console = console;
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        
        // Choose styling based on log level
        switch (event.getLevel().toInt()) {
            case Level.ERROR_INT:
                console.println(TerminalStyle.ERROR, message);
                break;
            case Level.WARN_INT:
                console.println(TerminalStyle.WARNING, message);
                break;
            case Level.INFO_INT:
                console.println(TerminalStyle.INFO, message);
                break;
            case Level.DEBUG_INT:
                console.println(TerminalStyle.DEBUG, message);
                break;
            case Level.TRACE_INT:
                console.println(TerminalStyle.TRACE, message);
                break;
            default:
                console.println(message);
        }
    }
}
```

Then register it programmatically:

```java
@Configuration
@ConditionalOnClass({LoggerContext.class, PrettyPrintStream.class})
public class PrettyLogbackConfiguration {
    
    @Bean
    @ConditionalOnBean(PrettyPrintStream.class)
    public LoggerContextListener prettyLogbackContextListener(PrettyPrintStream prettyPrintStream) {
        return new LoggerContextListener() {
            @Override
            public void onStart(LoggerContext context) {
                // Create and register our custom appender
                PrettyConsoleAppender appender = new PrettyConsoleAppender(prettyPrintStream);
                appender.setContext(context);
                appender.start();
                
                // Add to root logger
                Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
                rootLogger.addAppender(appender);
            }
            
            // Implement other required methods (empty implementations)
            // ...
        };
    }
}
```

## 5. Example Project

Here's a complete example of a Spring Boot application using Pretty Java Console:

```java
@SpringBootApplication
public class PrettyConsoleApplication implements CommandLineRunner {
    
    private final PrettyConsoleService console;
    
    public PrettyConsoleApplication(PrettyConsoleService console) {
        this.console = console;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(PrettyConsoleApplication.class, args);
    }
    
    @Override
    public void run(String... args) {
        // Display a header
        PrettyPrintStream out = console.getConsole();
        out.bold(true).foreground(RGBColor.of(25, 181, 254))
           .println("\n=== Pretty Java Spring Boot Demo ===\n");
        out.reset();
        
        // Use predefined styles
        console.info("Application started successfully");
        console.warning("Low disk space detected (15% remaining)");
        
        // Direct system output is also enhanced
        System.out.println("This uses the overridden System.out");
        System.err.println("This uses the overridden System.err");
        
        // Display hyperlinks
        console.info("For more information:");
        console.link("https://github.com/joshuasalcedo-io/joshuasalcedo-library", "View on GitHub");
        
        // Exit the application
        console.success("Demo completed successfully!");
    }
}
```

## 6. Configuration Reference

| Property | Description | Default |
|----------|-------------|---------|
| `pretty.console.enabled` | Enable/disable the auto-configuration | `true` |
| `pretty.console.override-system-out` | Override System.out and System.err | `true` |
| `pretty.console.default-color` | Default color for normal output (hex RGB) | `#cccccc` |
| `pretty.console.error-color` | Default color for error output (hex RGB) | `#ff5050` |

## 7. Advanced Usage

### Custom Terminal Styles

You can define custom terminal styles for your application:

```java
@Configuration
public class CustomPrettyConsoleConfig {
    
    @Bean
    public Map<String, TerminalStyle> customStyles() {
        Map<String, TerminalStyle> styles = new HashMap<>();
        
        // Define custom styles for your application
        styles.put("APP_HEADER", new TerminalStyle(
            RGBColor.of(255, 255, 255).withName("header-white"),
            RGBColor.of(0, 100, 200).withName("header-blue"),
            "HEADER"
        ));
        
        styles.put("APP_SUCCESS", new TerminalStyle(
            RGBColor.of(0, 220, 120).withName("app-success-green")
        ));
        
        return styles;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public EnhancedPrettyConsoleService enhancedConsoleService(
            PrettyPrintStream prettyPrintStream,
            Map<String, TerminalStyle> customStyles) {
        return new EnhancedPrettyConsoleService(prettyPrintStream, customStyles);
    }
}

// Enhanced service that supports custom styles
public class EnhancedPrettyConsoleService extends PrettyConsoleService {
    
    private final Map<String, TerminalStyle> customStyles;
    
    public EnhancedPrettyConsoleService(PrettyPrintStream console, Map<String, TerminalStyle> customStyles) {
        super(console);
        this.customStyles = customStyles;
    }
    
    public void printWithStyle(String styleName, String message) {
        TerminalStyle style = customStyles.get(styleName);
        if (style != null) {
            getConsole().println(style, message);
        } else {
            getConsole().println(message);
        }
    }
}
```

## 8. Troubleshooting

### ANSI Colors Not Showing

If colors aren't displaying correctly:

1. Check if your terminal supports ANSI codes
2. In Spring Boot, ensure `spring.output.ansi.enabled` is set to `always` or `detect`
3. For Windows Command Prompt, consider using Windows Terminal or enabling ANSI processing

### System.out Override Not Working

If System.out/err override isn't working:

1. Ensure `pretty.console.override-system-out=true` in application properties
2. Check if any other components are overriding System.out after your auto-configuration runs
3. If integrating with logging, check if the logging framework is intercepting output

### Custom Colors Not Applied

If custom colors aren't applied:

1. Verify the hex color format in your properties (should be `#RRGGBB`)
2. Check if terminal supports 24-bit (true color) ANSI codes
3. If not, consider using the RGBColor's toAnsi method, which will automatically downsample colors

## 9. Conclusion

You've now set up a Spring Boot auto-configuration for Pretty Java Console that:

1. Provides a `PrettyConsoleService` bean for use in your application
2. Optionally overrides System.out and System.err for enhanced output
3. Allows customization through application properties
4. Can be integrated with logging frameworks

This integration makes it easy to add beautiful, colorful console output to your Spring Boot applications with minimal configuration.