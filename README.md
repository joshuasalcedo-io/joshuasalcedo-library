
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


## Deployment Commands

```shell
cd $(git rev-parse --show-toplevel)
git add .
#git commit -m "Refactor $(git ls-files --full-name commit-message.txt)"
git commit -m ""
mvn clean deploy -P nexus
mvn clean deploy -P github
git push origin main

```
