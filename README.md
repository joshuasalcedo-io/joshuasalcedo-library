
# Joshua Salcedo Library

A library of reusable components for Java applications.

## Deployment Commands

```shell
cd $(git rev-parse --show-toplevel)
git add .
git commit -m "Fix bug in $(git ls-files --full-name commit-message.txt)"
mvn clean deploy -P nexus
mvn clean deploy -P github

```
mvn clean deploy -P central
## SonarQube Analysis

To run SonarQube analysis, you need to have a SonarQube token. This token should be stored in a `.env` file in the root directory of the project with the following format:

```
SONAR_TOKEN=your_sonar_token_here
```

**Note:** The `.env` file is excluded from version control to protect sensitive information.

### Running SonarQube Analysis

Use the provided batch script to run SonarQube analysis:

```shell
.\run-sonar.bat
```

This script will:
1. Read the SONAR_TOKEN from the .env file
2. Run the Maven SonarQube scanner with the token

Alternatively, you can manually set the SONAR_TOKEN environment variable and run the Maven command:

```shell
set SONAR_TOKEN=your_sonar_token_here
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.token=%SONAR_TOKEN%
```
