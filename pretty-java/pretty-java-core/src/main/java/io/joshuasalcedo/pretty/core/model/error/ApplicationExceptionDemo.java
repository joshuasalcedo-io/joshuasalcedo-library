package io.joshuasalcedo.pretty.core.model.error;

import io.joshuasalcedo.pretty.core.theme.TerminalStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A more complete demonstration showing how to use exception formatting
 * in a real application scenario.
 */
public class ApplicationExceptionDemo {

    private static final ExceptionFormatter FORMATTER = ExceptionFormatter.builder()
            .messageStyle(TerminalStyle.ERROR)
            .classNameStyle(TerminalStyle.EMPHASIS)
            .methodNameStyle(TerminalStyle.UI_VALUE)
            .fileNameStyle(TerminalStyle.TRACE)
            .lineNumberStyle(TerminalStyle.TRACE)
            .causedByStyle(TerminalStyle.WARNING)
            .suppressedStyle(TerminalStyle.INFO)
            // Highlight our application packages
            .highlightPackage("io.joshuasalcedo.pretty.core", TerminalStyle.IMPORTANT)
            // Highlight Java standard library
            .highlightPackage("java.io", TerminalStyle.DB_INFO)
            .highlightPackage("java.sql", TerminalStyle.DB_ERROR)
            .highlightPackage("java.nio", TerminalStyle.CONFIG_INFO)
            .highlightPackage("java.util", TerminalStyle.API_INFO)
            .build();

    public static void main(String[] args) {
        System.out.println(TerminalStyle.UI_HEADER.apply("Application Exception Handling Demo"));
        System.out.println(TerminalStyle.UI_SUBHEADER.apply("A more realistic example\n"));

        try {
            // Simulate application startup
            System.out.println(TerminalStyle.INFO.apply("[INFO] Starting application..."));
            ApplicationService service = new ApplicationService();
            System.out.println(TerminalStyle.INFO.apply("[INFO] Initializing services..."));
            service.initialize();
            System.out.println(TerminalStyle.SUCCESS.apply("[SUCCESS] Services initialized"));
            
            // Process some data
            System.out.println(TerminalStyle.INFO.apply("[INFO] Processing data..."));
            service.processData();
            
        } catch (Exception e) {
            System.out.println(TerminalStyle.ERROR.apply("[ERROR] Application encountered an error:"));
            System.out.println(FORMATTER.format(e));
            
            // In a real application, we might also log the error to a file
            System.out.println(TerminalStyle.INFO.apply("[INFO] See logs for details"));
        } finally {
            System.out.println(TerminalStyle.INFO.apply("[INFO] Application shutdown"));
        }
    }

    /**
     * Mock application service to simulate real-world scenarios
     */
    static class ApplicationService {
        private DatabaseManager dbManager;
        private ConfigurationManager configManager;
        
        public void initialize() throws Exception {
            try {
                // Initialize components
                System.out.println(TerminalStyle.TRACE.apply("  Loading configuration..."));
                configManager = new ConfigurationManager();
                configManager.loadConfiguration();
                
                System.out.println(TerminalStyle.TRACE.apply("  Connecting to database..."));
                dbManager = new DatabaseManager(configManager);
                dbManager.connect();
                
            } catch (Exception e) {
                throw new ServiceInitializationException("Failed to initialize application service", e);
            }
        }
        
        public void processData() throws Exception {
            try {
                // Let's cause an error for demonstration
                System.out.println(TerminalStyle.TRACE.apply("  Reading data files..."));
                DataProcessor processor = new DataProcessor(configManager);
                List<String> results = processor.processFiles();
                
                System.out.println(TerminalStyle.TRACE.apply("  Storing results in database..."));
                dbManager.storeResults(results);
                
            } catch (Exception e) {
                throw new DataProcessingException("Error processing application data", e);
            }
        }
    }
    
    /**
     * Mock configuration manager
     */
    static class ConfigurationManager {
        private Properties config = new Properties();
        
        public void loadConfiguration() throws ConfigurationException {
            try {
                // Simulate loading configuration from a file
                // We'll purposely fail here for the demo
                String nonExistentFile = "config/app-config.properties";
                if (!new File(nonExistentFile).exists()) {
                    throw new IOException("Configuration file not found: " + nonExistentFile);
                }
                
                try (FileReader reader = new FileReader(nonExistentFile)) {
                    config.load(reader);
                }
            } catch (IOException e) {
                throw new ConfigurationException("Failed to load configuration", e);
            }
        }
        
        public String getProperty(String key) {
            return config.getProperty(key);
        }
    }
    
    /**
     * Mock database manager
     */
    static class DatabaseManager {
        private ConfigurationManager configManager;
        private Connection connection;
        
        public DatabaseManager(ConfigurationManager configManager) {
            this.configManager = configManager;
        }
        
        public void connect() throws DatabaseException {
            try {
                // Simulate connecting to a database
                String dbUrl = configManager.getProperty("db.url");
                String username = configManager.getProperty("db.username");
                String password = configManager.getProperty("db.password");
                
                // This will fail because our config loading failed
                connection = DriverManager.getConnection(dbUrl, username, password);
                
            } catch (SQLException e) {
                throw new DatabaseException("Failed to connect to database", e);
            }
        }
        
        public void storeResults(List<String> results) throws DatabaseException {
            try {
                // Simulate storing results in the database
                if (connection == null || connection.isClosed()) {
                    throw new SQLException("Database connection is not available");
                }
                
                // Code to store results...
                
            } catch (SQLException e) {
                throw new DatabaseException("Failed to store results in database", e);
            }
        }
    }
    
    /**
     * Mock data processor
     */
    static class DataProcessor {
        private ConfigurationManager configManager;
        
        public DataProcessor(ConfigurationManager configManager) {
            this.configManager = configManager;
        }
        
        public List<String> processFiles() throws DataProcessingException {
            try {
                // Simulate processing data files
                String dataDir = configManager.getProperty("data.directory");
                
                // This will fail because our config loading failed
                if (dataDir == null) {
                    throw new IllegalStateException("Data directory not configured");
                }
                
                List<String> results = new ArrayList<>();
                File dir = new File(dataDir);
                
                if (!dir.exists() || !dir.isDirectory()) {
                    throw new IOException("Data directory does not exist: " + dataDir);
                }
                
                for (File file : dir.listFiles()) {
                    results.addAll(processFile(file));
                }
                
                return results;
                
            } catch (Exception e) {
                throw new DataProcessingException("Error processing data files", e);
            }
        }
        
        private List<String> processFile(File file) throws IOException {
            List<String> results = new ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    results.add(processLine(line));
                }
            }
            
            return results;
        }
        
        private String processLine(String line) {
            // Simulate processing a line of data
            return line.toUpperCase();
        }
    }
    
    // Custom exception classes
    static class ServiceInitializationException extends Exception {
        public ServiceInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class ConfigurationException extends Exception {
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class DatabaseException extends Exception {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class DataProcessingException extends Exception {
        public DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}