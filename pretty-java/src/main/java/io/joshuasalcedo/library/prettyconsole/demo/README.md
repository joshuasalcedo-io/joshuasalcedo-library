# PrettyConsole Demo

This package contains a comprehensive demonstration application for the PrettyConsole library, showing all the different capabilities for enhancing terminal output in Java applications.

## Running the Demo

To run the demo, execute the `PrettyConsoleDemo` class:

```bash
# From the project root
java -cp <project-classpath> io.joshuasalcedo.library.prettyconsole.demo.PrettyConsoleDemo
```

## Demo Features

The demo application showcases the following features:

1. **Basic Colors**
   - Standard foreground colors
   - Bright foreground colors
   - Background colors

2. **Text Styling**
   - Bold, italic, underline, strikethrough text
   - Combined styles (bold + color, etc.)
   - Nested styles

3. **Advanced Colors**
   - RGB colors
   - Hex color conversion
   - 256-color (8-bit) support
   - Color gradients

4. **String Decoration (Fluent API)**
   - Fluent interface for styling text
   - Chained methods for combined styling
   - Real-world use cases

5. **Message Formatting**
   - Different message types (error, warning, info, success, debug)
   - Custom message prefixes and styling
   - Application log simulation

6. **Layout Formatting**
   - Box formatting for content
   - Divider formatting with titles
   - Different border styles and colors

7. **Table Formatting**
   - Headers and rows with custom styling
   - Alternating row colors
   - Product catalog example

8. **Progress Bars**
   - Different styles and colors
   - Customizable characters and lengths
   - Animated progress demonstrations

9. **File Tree Visualization**
   - Directory structure rendering
   - Colored file types (Java, images, etc.)
   - Real-world project structure example

10. **PrettyFile**
    - File metadata formatting
    - Colored file listings
    - File type detection and styling

11. **Terminal Utilities**
    - Terminal capability detection
    - Text utilities (padding, centering, etc.)
    - File size formatting

12. **PrettyPrintStream**
    - Enhanced PrintStream with styles
    - Method chaining for styling
    - Console application simulation

## HTML Output

The demo automatically generates HTML files that preserve the colored output from the terminal. These files are saved in the `pretty-console-examples` directory and can be viewed in any web browser.

## Utility Classes

The demo package also includes utility classes that you can use in your own applications:

- **AnsiToHtmlConverter**: Converts ANSI-colored console output to HTML
- **AnsiToMarkdownConverter**: Converts ANSI-colored console output to Markdown with HTML spans

## Using in Your Projects

You can adapt the examples from the demo to use in your own projects. Here are some common patterns:

### Basic Color

```java
System.out.println(PrettyConsole.apply(ForegroundColor.GREEN, "Success!"));
```

### Combined Styles

```java
StyleFormatter formatter = new StyleFormatter()
    .withStyle(TextStyle.BOLD)
    .withColor(ForegroundColor.RED)
    .withBackground(BackgroundColor.BRIGHT_WHITE);

System.out.println(PrettyConsole.apply(formatter, "Important error message!"));
```

### Fluent API

```java
String warning = PrettyConsole.decorate("WARNING")
    .style(TextStyle.BOLD)
    .color(ForegroundColor.BLACK)
    .bg(BackgroundColor.YELLOW)
    .text();

System.out.println(warning + " This operation is risky!");
```

### Progress Bar

```java
ProgressBarFormat progressBar = FormatFactory.createFormat(
    VisualizationType.PROGRESS_BAR, ProgressBarFormat.class)
    .setBarColor(ForegroundColor.CYAN)
    .setCompletedChar('=')
    .setIncompleteChar(' ')
    .setPrefix("Downloading: ")
    .setLength(30)
    .showPercentage(true);

// Display progress at 75%
System.out.println(progressBar.generate(75, 100));
```

### Table

```java
TableFormat table = FormatFactory.createFormat(
    VisualizationType.TABLE, TableFormat.class)
    .setHeaderBackground(BackgroundColor.BLUE)
    .setHeaderColor(ForegroundColor.WHITE);

String[] headers = {"Name", "Age", "City"};
String[][] data = {
    {"John", "28", "New York"},
    {"Alice", "32", "London"},
    {"Bob", "25", "Tokyo"}
};

System.out.println(table.generate(headers, data));
```

## Notes

- The demo outputs the styled text to the console and also saves it as HTML for reference
- Not all terminal emulators support all ANSI features; results may vary
- Use `PrettyConsole.safeApply()` to automatically disable colors where not supported