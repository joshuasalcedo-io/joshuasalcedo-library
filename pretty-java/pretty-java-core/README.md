# Pretty Java Core

A Java library for creating beautiful, interactive console applications with rich styling, colors, and progress indicators.

## Overview

Pretty Java Core provides a set of tools for enhancing command-line interfaces with:

- Rich text styling with RGB color support
- Interactive progress indicators
- Task lists with status tracking
- Animated spinners and text
- File tree visualization
- And more!

This library makes it easy to create professional-looking CLI applications that provide visual feedback to users during long-running operations.

## Quick Start Example

Here's a complete example you can copy and paste to get started immediately:

```java
import io.joshuasalcedo.pretty.core.model.ProgressBarRunner;
import io.joshuasalcedo.pretty.core.model.ProgressRunnerFactory;
import io.joshuasalcedo.pretty.core.model.SpinnerProgressRunner;
import io.joshuasalcedo.pretty.core.model.TaskListProgressRunner;
import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

public class PrettyJavaExample {
    public static void main(String[] args) throws Exception {
        // Create fancy console output
        PrettyPrintStream out = new PrettyPrintStream(System.out);
        
        // Print stylized header
        out.foreground(RGBColor.of(25, 181, 254)).bold(true)
           .println("\n=== Pretty Java Demo ===\n");
           
        // Show a progress bar
        ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
                "Processing files...", 50, RGBColor.of(92, 172, 238));
        
        progressBar.start();
        for (int i = 0; i <= 100; i++) {
            progressBar.withProgress(i / 100.0);
            progressBar.withMessage("Processing files... " + i + "%");
            Thread.sleep(20); // Simulate work
        }
        progressBar.stop();
        
        // Show a spinner
        SpinnerProgressRunner spinner = (SpinnerProgressRunner) ProgressRunnerFactory.createSpinner(
                "Loading data...", SpinnerProgressRunner.SpinnerType.DOTS);
                
        spinner.start();
        Thread.sleep(2000); // Simulate work
        spinner.stop();
        
        // Show task list
        TaskListProgressRunner tasks = (TaskListProgressRunner) ProgressRunnerFactory.createTaskList(
                "Setup steps:",
                "Initialize application",
                "Load configuration",
                "Connect to database",
                "Start services"
        );
        
        tasks.start();
        
        for (int i = 0; i < 4; i++) {
            tasks.markTaskInProgress(i);
            Thread.sleep(500); // Simulate work
            tasks.markTaskComplete(i);
        }
        
        tasks.stop();
        
        // Print stylized result
        out.println();
        out.println(TerminalStyle.SUCCESS, "✓ All operations completed successfully!");
        out.println();
        
        // Create clickable links (supported in modern terminals like iTerm2, Windows Terminal)
        out.println("For more information, visit:");
        out.printlnHyperlink("https://github.com/joshuasalcedo-io/joshuasalcedo-library", "GitHub Repository");
    }
}
```

## What This Example Does

When you run this example, you'll see:

1. A colorful header with bold text
2. A blue progress bar that fills up while showing percentage
3. A loading spinner animation
4. A task list that shows tasks being completed one by one
5. A success message with green checkmark
6. A clickable hyperlink (in terminals that support OSC 8 hyperlinks)

This demonstrates the core capabilities of the library in just a few lines of code!

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.joshuasalcedo.library</groupId>
    <artifactId>pretty-java-core</artifactId>
    <version>0.1.1</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```groovy
implementation 'io.joshuasalcedo.library:pretty-java-core:0.1.1'
```

### Using with the Parent BOM

For easier dependency management, you can use the parent BOM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.joshuasalcedo.library</groupId>
            <artifactId>library-parent</artifactId>
            <version>0.1.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.joshuasalcedo.library</groupId>
        <artifactId>pretty-java-core</artifactId>
        <!-- No version needed when using the parent BOM -->
    </dependency>
</dependencies>
```

### Spring Boot Integration

For Spring Boot applications, add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.joshuasalcedo.library</groupId>
    <artifactId>pretty-java-core</artifactId>
    <version>0.1.1</version>
</dependency>
```

Then create a bean for the PrettyPrintStream:

```java
@Configuration
public class PrettyConsoleConfig {
    
    @Bean
    public PrettyPrintStream prettyPrintStream() {
        return new PrettyPrintStream(System.out);
    }
}
```

And use it in your services:

```java
@Service
public class DataProcessingService {
    
    private final PrettyPrintStream console;
    
    public DataProcessingService(PrettyPrintStream console) {
        this.console = console;
    }
    
    public void processData() throws Exception {
        // Create a progress bar
        ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
                "Processing data...", 50, RGBColor.of(92, 172, 238));
        
        progressBar.start();
        
        // Your processing logic
        
        progressBar.stop();
        console.println(TerminalStyle.SUCCESS, "Data processing complete!");
    }
}

## Key Features

### Progress Indicators

Pretty Java Core offers several types of progress indicators:

- **Progress Bars**: Visual bars showing completion percentage
- **Spinners**: Animated indicators for indeterminate progress
- **Task Lists**: Lists of tasks with status indicators
- **Counters**: Numeric progress indicators
- **Text Animations**: Animated text for visual feedback
- **Dots Animations**: Simple animated dots

### Terminal Styling

Enhance your console output with:

- RGB color support for true color terminals
- Bold, italic, underline text formatting
- Predefined styles for success, error, warning, etc.
- Custom styling options

### File Operations

Visualize file operations with:

- File tree visualization
- Pretty-printed file listings
- Progress indicators for file operations

## Usage Examples

### Basic Progress Bar

```java
// Create a progress bar
ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
        "Downloading file...", 50);

// Start the progress bar
progressBar.start();

// Update progress as your operation proceeds
for (int i = 0; i <= 100; i++) {
    progressBar.withProgress(i / 100.0);
    progressBar.withMessage("Downloading file... " + i + "%");
    Thread.sleep(50);
}

// Stop the progress bar when done
progressBar.stop();
```

### Task List

```java
// Create a task list
TaskListProgressRunner taskList = (TaskListProgressRunner) ProgressRunnerFactory.createTaskList(
        "Deployment steps:",
        "Build application",
        "Run tests",
        "Deploy to staging",
        "Verify deployment"
);

// Start the task list
taskList.start();

// Update task status as operations complete
taskList.markTaskInProgress(0);
// ... perform build ...
taskList.markTaskComplete(0);

taskList.markTaskInProgress(1);
// ... run tests ...
taskList.markTaskComplete(1);

// ... and so on ...

// Stop the task list when all tasks are complete
taskList.stop();
```

### Styled Text Output

```java
// Create a PrettyPrintStream
PrettyPrintStream out = new PrettyPrintStream(System.out);

// Print styled text
out.foreground(RGBColor.of(50, 205, 50)).bold(true)
   .println("Operation successful!");

out.foreground(RGBColor.of(255, 69, 0))
   .println("Operation failed!");

// Use predefined styles
out.println(TerminalStyle.SUCCESS, "Task completed successfully");
out.println(TerminalStyle.ERROR, "An error occurred");
out.println(TerminalStyle.WARNING, "Warning: disk space low");

// Clickable hyperlinks (works in terminals that support OSC 8)
out.printlnHyperlink("https://github.com/joshuasalcedo-io", "Visit GitHub");
```

### Clickable Terminal Hyperlinks

Pretty Java Core supports creating clickable hyperlinks in terminals that support the OSC 8 hyperlink protocol (such as iTerm2, Windows Terminal, and many modern terminal emulators):

```java
PrettyPrintStream console = new PrettyPrintStream(System.out);

// Basic hyperlink with custom text
console.printlnHyperlink("https://github.com/joshuasalcedo-io", "Visit Joshua's GitHub");

// Hyperlink using the URL as display text
console.printlnHyperlink("https://joshuasalcedo.io", null);

// Hyperlinks in sections of text
console.println("For more information, please ");
console.printHyperlink("https://example.com/docs", "view our documentation");
console.println(".");
```

Key features:
- Links are automatically styled with a recognizable blue color and underline
- Graceful fallback for terminals that don't support OSC 8 (showing URL in parentheses)
- Can use either custom text or the URL itself as display text
- Available in both print and println variants

## Advanced Features

### Custom Spinners

```java
// Create a spinner with custom type
SpinnerProgressRunner spinner = (SpinnerProgressRunner) ProgressRunnerFactory.createSpinner(
        "Processing data...", 
        SpinnerProgressRunner.SpinnerType.DOTS);

// Start the spinner
spinner.start();

// Update message as operation proceeds
spinner.withMessage("Connecting to server...");
// ... perform operation ...
spinner.withMessage("Downloading data...");
// ... perform operation ...

// Stop the spinner when done
spinner.stop();
```

### Colored Progress Bars

```java
// Create a progress bar with custom colors
ProgressBarRunner progressBar = (ProgressBarRunner) ProgressRunnerFactory.createProgressBar(
        "Uploading file...", 
        50,
        RGBColor.of(0, 191, 255),  // Progress color (blue)
        RGBColor.of(40, 40, 40)     // Remaining color (dark gray)
);

// Use the progress bar as before
progressBar.start();
// ...
progressBar.stop();
```

## API Documentation

### Core Classes

- `AbstractProgressRunner`: Base class for all progress indicators
- `ProgressRunnerFactory`: Factory for creating different types of progress indicators
- `PrettyPrintStream`: Enhanced PrintStream with styling capabilities
- `RGBColor`: Class for working with RGB colors
- `TerminalStyle`: Predefined terminal styles

### Progress Indicators

- `ProgressBarRunner`: Horizontal progress bar
- `SpinnerProgressRunner`: Animated spinner
- `TaskListProgressRunner`: List of tasks with status
- `CounterProgressRunner`: Numeric counter
- `TextAnimationRunner`: Animated text
- `DotsAnimationRunner`: Animated dots

## Interactive Demo

The library includes an interactive demo that showcases all its features. This demo allows you to explore the various capabilities of Pretty Java Core in a hands-on way.

### Running the Demo

```java
// Import the demo class
import io.joshuasalcedo.pretty.core.demo.PrettyJavaDemo;

// Run the demo
public class Main {
    public static void main(String[] args) {
        PrettyJavaDemo.main(args);
    }
}
```

Alternatively, you can run the demo class directly:

```bash
java -cp pretty-java-core-${version}.jar io.joshuasalcedo.pretty.core.demo.PrettyJavaDemo
```

### Demo Features

The interactive demo includes:

1. **Progress Bar Demo**: Shows a progress bar for a file download operation
2. **Spinner Demo**: Demonstrates a spinner for an API request operation
3. **Task List Demo**: Displays a task list for a deployment process
4. **Counter Demo**: Shows a counter for a database migration operation
5. **Text Animation Demo**: Demonstrates text animation for a search operation
6. **Styled Text Demo**: Showcases various text styling options
7. **Complete Workflow Demo**: Shows all features working together in a real-world scenario

The demo is fully interactive, allowing you to select which features to explore and see them in action.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

Joshua Salcedo - [GitHub Profile](https://github.com/joshuasalcedo)

Project Link: [https://github.com/joshuasalcedo/pretty-java](https://github.com/joshuasalcedo/pretty-java)
