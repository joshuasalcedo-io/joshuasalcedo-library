package io.joshuasalcedo.pretty.core.model.error;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Data Transfer Object (DTO) for java.lang.StackTraceElement.
 * Since StackTraceElement is final and cannot be extended, this class
 * provides a mutable representation with the same properties.
 */
public class StackTraceElementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String classLoaderName;
    private String moduleName;
    private String moduleVersion;
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;

    /**
     * Default constructor
     */
    public StackTraceElementDTO() {
    }

    /**
     * Creates a StackTraceElementDTO with the specified properties
     *
     * @param classLoaderName the class loader name
     * @param moduleName the module name
     * @param moduleVersion the module version
     * @param declaringClass the fully qualified name of the class
     * @param methodName the name of the method
     * @param fileName the name of the source file
     * @param lineNumber the line number in the source file
     */
    public StackTraceElementDTO(String classLoaderName,
                                String moduleName,
                                String moduleVersion,
                                String declaringClass,
                                String methodName,
                                String fileName,
                                int lineNumber) {
        this.classLoaderName = classLoaderName;
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    /**
     * Convenience constructor with only the basic fields
     */
    public StackTraceElementDTO(String declaringClass, String methodName, String fileName, int lineNumber) {
        this(null, null, null, declaringClass, methodName, fileName, lineNumber);
    }

    // Getters and setters
    public String getClassLoaderName() {
        return classLoaderName;
    }

    public void setClassLoaderName(String classLoaderName) {
        this.classLoaderName = classLoaderName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Returns true if the method is a native method
     */
    public boolean isNativeMethod() {
        return lineNumber == -2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StackTraceElementDTO)) {
            return false;
        }
        
        StackTraceElementDTO other = (StackTraceElementDTO) obj;
        return lineNumber == other.lineNumber
                && Objects.equals(declaringClass, other.declaringClass)
                && Objects.equals(classLoaderName, other.classLoaderName)
                && Objects.equals(moduleName, other.moduleName)
                && Objects.equals(moduleVersion, other.moduleVersion)
                && Objects.equals(methodName, other.methodName)
                && Objects.equals(fileName, other.fileName);
    }

    @Override
    public int hashCode() {
        int result = 31 * Objects.hashCode(declaringClass) + Objects.hashCode(methodName);
        result = 31 * result + Objects.hashCode(classLoaderName);
        result = 31 * result + Objects.hashCode(moduleName);
        result = 31 * result + Objects.hashCode(moduleVersion);
        result = 31 * result + Objects.hashCode(fileName);
        result = 31 * result + lineNumber;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (classLoaderName != null && !classLoaderName.isEmpty()) {
            sb.append(classLoaderName).append('/');
        }

        if (moduleName != null && !moduleName.isEmpty()) {
            sb.append(moduleName);
            if (moduleVersion != null && !moduleVersion.isEmpty()) {
                sb.append('@').append(moduleVersion);
            }
        }

        if (sb.length() > 0) {
            sb.append('/');
        }

        sb.append(declaringClass).append('.').append(methodName).append('(');
        if (isNativeMethod()) {
            sb.append("Native Method");
        } else if (fileName == null) {
            sb.append("Unknown Source");
        } else {
            sb.append(fileName);
            if (lineNumber >= 0) {
                sb.append(':').append(lineNumber);
            }
        }
        sb.append(')');

        return sb.toString();
    }
}