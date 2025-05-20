
# Joshua Salcedo Library

## 
    Just a simple library to practice on code maintenance and follow good practices.
    A library of reusable components for Java applications.
Not yet complete but can be used

```shell

=== Clean File System Tree (No Hidden or Gitignored Files) ===
📁 library-parent
├── 📄 commit-message.txt
├── 📝 pom.xml
├── 📁 pretty-console-examples
│   └── 🌐 pretty-console-demo.html
├── 📁 pretty-java
│   ├── 📚 CHANGES.md
│   ├── 📝 pom.xml
│   ├── 📁 pretty-java-core
│   │   ├── 📝 pom.xml
│   │   ├── 📚 README.md
│   │   ├── 📁 src
│   │   │   ├── 📁 main
│   │   │   │   ├── 📁 java
│   │   │   │   │   └── 📁 io
│   │   │   │   │       └── 📁 joshuasalcedo
│   │   │   │   │           └── 📁 pretty
│   │   │   │   │               └── 📁 core
│   │   │   │   │                   ├── ☕ AbstractPrettyFormatter.java
│   │   │   │   │                   ├── ☕ ColorProvider.java
│   │   │   │   │                   ├── 📁 demo
│   │   │   │   │                   │   └── ☕ PrettyJavaDemo.java
│   │   │   │   │                   ├── 📁 model
│   │   │   │   │                   │   ├── 📁 file
│   │   │   │   │                   │   │   └── ☕ PrettyFile.java
│   │   │   │   │                   │   ├── ☕ PrettyFileTreeNode.java
│   │   │   │   │                   │   ├── 📁 progress
│   │   │   │   │                   │   │   ├── ☕ AbstractProgressRunner.java
│   │   │   │   │                   │   │   ├── ☕ ColoredProgressBar.java
│   │   │   │   │                   │   │   ├── ☕ CounterProgressRunner.java
│   │   │   │   │                   │   │   ├── ☕ DotsAnimationRunner.java
│   │   │   │   │                   │   │   └── ☕ TextAnimationRunner.java
│   │   │   │   │                   │   ├── ☕ ProgressBarRunner.java
│   │   │   │   │                   │   ├── ☕ ProgressRunnerExamples.java
│   │   │   │   │                   │   ├── ☕ ProgressRunnerFactory.java
│   │   │   │   │                   │   ├── ☕ SpinnerProgressRunner.java
│   │   │   │   │                   │   ├── 📁 stream
│   │   │   │   │                   │   │   └── ☕ PrettyPrintStream.java
│   │   │   │   │                   │   ├── ☕ TaskListProgressRunner.java
│   │   │   │   │                   │   └── ☕ TaskListProgressRunnerTest.java
│   │   │   │   │                   ├── ☕ PrettyFormat.java
│   │   │   │   │                   ├── 📁 properties
│   │   │   │   │                   │   ├── ☕ ColorProperties.java
│   │   │   │   │                   │   ├── ☕ PrettyProperty.java
│   │   │   │   │                   │   ├── ☕ RGBColor.java
│   │   │   │   │                   │   └── ☕ StyleProperties.java
│   │   │   │   │                   ├── ☕ StyleProvider.java
│   │   │   │   │                   ├── 📁 theme
│   │   │   │   │                   │   └── ☕ TerminalStyle.java
│   │   │   │   │                   └── 📁 utils
│   │   │   │   │                       ├── ☕ FileUtils.java
│   │   │   │   │                       ├── ☕ RGBColorCalculator.java
│   │   │   │   │                       ├── ☕ TerminalUtils.java
│   │   │   │   │                       └── ☕ TextUtils.java
│   │   │   │   └── 📁 resources
│   │   │   └── 📁 test
│   │   │       ├── 📁 java
│   │   │       └── 📁 resources
│   │   └── 📁 target
│   │       ├── 📁 classes
│   │       │   └── 📁 io
│   │       │       └── 📁 joshuasalcedo
│   │       │           └── 📁 pretty
│   │       │               └── 📁 core
│   │       │                   ├── 📁 demo
│   │       │                   ├── 📁 model
│   │       │                   │   ├── 📁 file
│   │       │                   │   ├── 📁 progress
│   │       │                   │   └── 📁 stream
│   │       │                   ├── 📁 properties
│   │       │                   ├── 📁 theme
│   │       │                   └── 📁 utils
│   │       ├── 📁 generated-sources
│   │       │   └── 📁 annotations
│   │       ├── 📁 generated-test-sources
│   │       │   └── 📁 test-annotations
│   │       ├── 📁 maven-archiver
│   │       │   └── 🔑 pom.properties
│   │       ├── 📁 maven-status
│   │       │   └── 📁 maven-compiler-plugin
│   │       │       ├── 📁 compile
│   │       │       │   └── 📁 default-compile
│   │       │       │       ├── ❓ createdFiles.lst
│   │       │       │       └── ❓ inputFiles.lst
│   │       │       └── 📁 testCompile
│   │       │           └── 📁 default-testCompile
│   │       │               ├── ❓ createdFiles.lst
│   │       │               └── ❓ inputFiles.lst
│   │       └── 📁 test-classes
│   ├── 🔧 qodana.yaml
│   ├── 📚 REFACTORING.md
│   ├── 📜 script.js
│   ├── 📁 src
│   │   ├── 📁 main
│   │   │   ├── 📁 java
│   │   │   │   └── 📁 io
│   │   │   │       └── 📁 joshuasalcedo
│   │   │   │           └── 📁 library
│   │   │   │               └── 📁 prettyconsole
│   │   │   │                   ├── 📁 api
│   │   │   │                   │   ├── ☕ Format.java
│   │   │   │                   │   ├── ☕ MessageFormat.java
│   │   │   │                   │   ├── ☕ TextFormat.java
│   │   │   │                   │   └── ☕ VisualFormat.java
│   │   │   │                   ├── 📁 core
│   │   │   │                   │   ├── 📁 file
│   │   │   │                   │   │   ├── ☕ PrettyFile.java
│   │   │   │                   │   │   └── ☕ PrettyFileTreeNode.java
│   │   │   │                   │   └── 📁 stream
│   │   │   │                   │       └── ☕ PrettyPrintStream.java
│   │   │   │                   ├── 📁 demo
│   │   │   │                   │   ├── ☕ PrettyConsoleDemo.java
│   │   │   │                   │   └── 📚 README.md
│   │   │   │                   ├── 📁 format
│   │   │   │                   │   ├── ☕ FormatFactory.java
│   │   │   │                   │   ├── 📁 layout
│   │   │   │                   │   │   ├── ☕ BoxFormat.java
│   │   │   │                   │   │   ├── ☕ DividerFormat.java
│   │   │   │                   │   │   ├── ☕ FileTreeFormat.java
│   │   │   │                   │   │   └── ☕ TableFormat.java
│   │   │   │                   │   ├── 📁 progress
│   │   │   │                   │   │   ├── ☕ ProgressBarFormat.java
│   │   │   │                   │   │   └── ☕ ProgressBarRunner.java
│   │   │   │                   │   ├── 📁 text
│   │   │   │                   │   │   ├── ☕ BasicMessageFormat.java
│   │   │   │                   │   │   └── ☕ MessageType.java
│   │   │   │                   │   └── ☕ VisualizationType.java
│   │   │   │                   ├── ☕ PrettyConsole.java
│   │   │   │                   ├── ☕ PrettyConsoleFormatter.java
│   │   │   │                   ├── ☕ PrettyStyle.java
│   │   │   │                   ├── 📁 style
│   │   │   │                   │   ├── ☕ AbstractStyleComponent.java
│   │   │   │                   │   ├── ☕ BackgroundColor.java
│   │   │   │                   │   ├── 📁 color
│   │   │   │                   │   │   ├── ☕ ColorConverter.java
│   │   │   │                   │   │   └── ☕ ColorProvider.java
│   │   │   │                   │   ├── ☕ Color.java
│   │   │   │                   │   ├── ☕ Color256.java
│   │   │   │                   │   ├── ☕ ColorConverter.java
│   │   │   │                   │   ├── ☕ ColorType.java
│   │   │   │                   │   ├── 📁 core
│   │   │   │                   │   │   └── ☕ AnsiConstants.java
│   │   │   │                   │   ├── ☕ ForegroundColor.java
│   │   │   │                   │   ├── ☕ RgbColor.java
│   │   │   │                   │   ├── ☕ StringDecorator.java
│   │   │   │                   │   ├── ☕ Style.java
│   │   │   │                   │   ├── ☕ StyleComponent.java
│   │   │   │                   │   ├── ☕ StyleFormatter.java
│   │   │   │                   │   ├── ☕ StyleManager.java
│   │   │   │                   │   └── ☕ TextStyle.java
│   │   │   │                   └── 📁 utils
│   │   │   │                       ├── ☕ AnsiToHtmlConverter.java
│   │   │   │                       ├── ☕ AnsiToMarkdownConverter.java
│   │   │   │                       ├── ☕ FileUtils.java
│   │   │   │                       ├── ☕ TerminalUtils.java
│   │   │   │                       └── ☕ TextUtils.java
│   │   │   └── 📁 resources
│   │   └── 📁 test
│   │       ├── 📁 java
│   │       │   └── 📁 io
│   │       │       └── 📁 joshuasalcedo
│   │       │           └── 📁 library
│   │       │               └── 📁 prettyconsole
│   │       │                   ├── 📁 core
│   │       │                   │   └── 📁 file
│   │       │                   │       ├── ☕ PrettyFileTest.java
│   │       │                   │       └── ☕ PrettyFileTreeNodeTest.java
│   │       │                   ├── 📁 format
│   │       │                   │   └── 📁 text
│   │       │                   │       └── ☕ BasicMessageFormatTest.java
│   │       │                   ├── ☕ PrettyTest.java
│   │       │                   └── 📁 utils
│   │       │                       ├── ☕ FileUtilsTest.java
│   │       │                       ├── ☕ TerminalUtilsTest.java
│   │       │                       └── ☕ TextUtilsTest.java
│   │       └── 📁 resources
│   ├── 🎨 styles.css
│   └── 📚 todo.md
├── 📚 problem.md
├── 🔧 qodana.yaml
├── 📚 README.md
├── 🖥️ run-sonar.bat
└── 🐍 sonatype-central-publisher.py

```


## Using the Library

### Option 1: Import All Modules (Recommended)

To include all modules automatically in your project, add the library-parent dependency to your project's dependencyManagement section:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.joshuasalcedo.library</groupId>
            <artifactId>library-parent</artifactId>
            <version>${library.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Then you can use any module without specifying versions:

```xml
<dependencies>
    <dependency>
        <groupId>io.joshuasalcedo.library</groupId>
        <artifactId>pretty-java-core</artifactId>
    </dependency>
</dependencies>
```

### Option 2: Import Specific Modules

Alternatively, you can import specific modules with explicit versions:

```xml
<!-- For pretty-java core module only -->
<dependency>
    <groupId>io.joshuasalcedo.library</groupId>
    <artifactId>pretty-java-core</artifactId>
    <version>${library.version}</version>
</dependency>
```

## Deployment Commands

```shell
# Navigate to root directory
cd $(git rev-parse --show-toplevel)

# Add changes
git add .

# Commit changes
git commit -m "feature: Added PrettyTimeStamp"

# Deploy to Nexus repository
mvn clean deploy -DaltDeploymentRepository=joshuasalcedo-nexus::default::https://repo.joshuasalcedo.io/repository/maven-releases/

# Deploy to GitHub packages
#mvn clean deploy -DaltDeploymentRepository=github::default::https://maven.pkg.github.com/joshuasalcedo-io/joshuasalcedo-library

# Push changes to GitHub
git push origin main
```

## Module Structure

The library is organized into the following modules:

1. `library-parent` - Parent POM that includes all modules
2. `pretty-java` - Module containing Pretty Java Console components
   - `pretty-java-core` - Core functionality for Pretty Java

When you depend on `library-parent`, all these modules are automatically included in your project.

### How to Add New Modules

To add a new module to the library:

1. Create the module directory and POM file with appropriate parent reference
2. Add the module to the parent's `<modules>` section
3. Add the module to the parent's `<dependencyManagement>` section

Example:

```xml
<!-- In library-parent/pom.xml -->
<modules>
    <module>pretty-java</module>
    <module>your-new-module</module>
</modules>

<dependencyManagement>
    <dependencies>
        <!-- Add your new module -->
        <dependency>
            <groupId>io.joshuasalcedo.library</groupId>
            <artifactId>your-new-module</artifactId>
            <version>${revision}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Deployment Implementation Details

The library deployment has been automated with the following features:

1. **Automatic Module Discovery**: All modules are automatically included when depending on the `library-parent` POM.

2. **Version Management**: The `${revision}` property is used for consistent versioning across all modules. The flatten-maven-plugin resolves these placeholders during the build.

3. **Deployment Optimization**: The `maven-deploy-plugin` is configured with `deployAtEnd=true` to optimize the deployment process.

4. **Bill of Materials (BOM) Pattern**: The parent POM serves as a BOM, allowing consumers to import all dependencies with consistent versions.

5. **Multiple Repository Support**: The project can be deployed to:
   - Nexus repository: `mvn clean deploy -P nexus`
   - GitHub packages: `mvn clean deploy -P github`
   - Maven Central: `mvn clean deploy -P central`
