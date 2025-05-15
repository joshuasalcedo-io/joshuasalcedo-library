# Pretty Console Package Structure

## Overview
This document describes the package structure of the Pretty Console library after the reorganization.

## Package Hierarchy

```
io.joshuasalcedo.library.prettyconsole
├── api                     # API interfaces for formatters
│   ├── Format.java         # Base interface for all formatters
│   ├── MessageFormat.java  # Interface for message formatters
│   ├── TextFormat.java     # Interface for text formatters
│   └── VisualFormat.java   # Interface for visual formatters
├── core                    # Core components
│   ├── file                # File-related components
│   │   ├── PrettyFile.java
│   │   └── PrettyFileTreeNode.java
│   └── stream              # Stream-related components
│       └── PrettyPrintStream.java
├── format                  # Format implementations
│   ├── layout              # Layout formatters
│   │   ├── BoxFormat.java
│   │   ├── DividerFormat.java
│   │   └── TableFormat.java
│   ├── progress            # Progress formatters
│   │   ├── ProgressBarFormat.java
│   │   └── ProgressBarRunner.java
│   ├── text                # Text formatters
│   │   ├── BasicMessageFormat.java
│   │   └── MessageType.java
│   ├── FormatFactory.java  # Factory for creating formatters
│   └── VisualizationType.java # Enum for visualization types
├── style                   # Styling components
│   ├── AbstractStyleComponent.java
│   ├── BackgroundColor.java
│   ├── Color256.java
│   ├── ForegroundColor.java
│   ├── RgbColor.java
│   ├── StringDecorator.java
│   ├── Style.java          # Utility class for styling
│   ├── StyleComponent.java # Interface for style components
│   ├── StyleFormatter.java # Formatter for combining styles
│   └── TextStyle.java      # Enum for text styles
├── utils                   # Utility classes
│   ├── FileUtils.java
│   ├── TerminalUtils.java
│   └── TextUtils.java
├── Pretty.java             # Main entry point for formatters
└── PrettyConsole.java      # Main entry point for styling
```

## Package Responsibilities

### api
Contains the interfaces that define the contract for formatters. These interfaces are implemented by the classes in the format package.

### core
Contains core components that provide additional functionality beyond basic formatting, such as file handling and custom print streams.

### format
Contains implementations of the formatter interfaces defined in the api package. Organized into subpackages based on the type of formatting they provide.

### style
Contains components for styling text, such as colors, backgrounds, and text styles. These components are used by the formatters to apply styling to text.

### utils
Contains utility classes that provide helper methods for file operations, terminal detection, and text manipulation.

## Main Entry Points

### Pretty
The main entry point for creating formatters. Provides static methods for creating various types of formatters.

### PrettyConsole
The main entry point for styling text. Provides static methods for applying styles to text.