package io.joshuasalcedo.pretty.core.api;

/**
 * The {@code PrettySystem} class provides a custom implementation of core system functionality,
 * serving as an alternative to Java's standard {@code java.lang.System} class.
 * This class cannot be instantiated and offers a collection of utility methods and
 * standard I/O operations.
 *
 * <p>PrettySystem aims to provide enhanced functionality while maintaining compatibility
 * with the standard System class operations. It includes improved implementations for:
 * <ul>
 *   <li>Standard input, output, and error streams with formatting capabilities</li>
 *   <li>Enhanced property management for configuration</li>
 *   <li>Optimized array operations and memory management</li>
 *   <li>Platform-specific resource access and environmental variable handling</li>
 *   <li>Extended utility methods for system interaction</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>
 *     PrettySystem.out.println("Hello with formatting!");
 *     String javaVersion = PrettySystem.getProperty("java.version");
 *     PrettySystem.arraycopy(src, srcPos, dest, destPos, length);
 * </pre>
 *
 * <p>This implementation focuses on improved performance and additional features
 * while maintaining backward compatibility with the standard System class where possible.
 *
 * <p><strong>Note:</strong> This is a custom implementation and not part of the Java
 * standard library. Some operations may work differently than their standard counterparts.
 *
 * @author Joshua Salcedo
 * @version 1.0
 * @since 1.0
 * @see java.lang.System
 */
public final class PrettySystem {


}