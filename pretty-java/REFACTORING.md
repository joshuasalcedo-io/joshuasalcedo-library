# Pretty-Java Refactoring Plan

This document outlines a comprehensive refactoring strategy to address code duplication and inefficiencies in the Pretty-Java library.

## 1. Identified Issues

1. **Duplicate Implementations**: Several utility methods are duplicated across `PrettyConsole`, `Style`, and `PrettyStyle` classes:
   - `rgbToHex()`, `hexToRgb()`, `rgb()`, `safeApply()`, and others.
   - Some are even explicitly marked as duplicated with comments.

2. **Duplicate Constants**: 
   - The constants `RESET` and `CSI` appear in multiple classes.

3. **Inefficient Code**:
   - The `format()` method in `PrettyPrintStream` creates a new `StyleFormatter` with each call.
   - Method name `isABoolean()` is confusing and does not reflect its purpose.

## 2. Refactoring Strategy

### Phase 1: Foundation Classes

1. **Create Core Constants**
   - Create `AnsiConstants` class for centralized ANSI-related constants.
   - Replace all references to these constants with `AnsiConstants.RESET`, etc.

2. **Consolidate Utility Methods**
   - Create `StyleManager` for common styling operations.
   - Create `ColorConverter` for color-related conversions.
   - Create `ColorProvider` interface for a unified color system.

### Phase 2: Class Updates

1. **Update `AbstractStyleComponent`**
   - Replace hardcoded constants with references to `AnsiConstants`.

2. **Optimize `PrettyPrintStream.format()`**
   - Replace inefficient formatter creation with StringBuilder approach.
   - Rename confusing method and mark as deprecated.

3. **Create New Versions of Facade Classes**
   - Update `PrettyConsole` and `Style` to delegate to utility classes.
   - Keep API compatibility while removing duplication.

### Phase 3: Final Implementation

1. **Apply Changes**
   - Replace original classes with refactored versions.
   - Update imports across the codebase.

2. **Update Documentation**
   - Update JavaDocs to reflect new architecture.
   - Update example code if needed.

3. **Test**
   - Ensure all functionality works as expected.
   - Run performance tests to confirm improvements.

## 3. Migration Notes

### For Library Maintainer

1. Replace original files with the `.new` versions:
   - `PrettyConsole.java.new` → `PrettyConsole.java`
   - `Style.java.new` → `Style.java`

2. Review and update imports in all other classes to use the new utility classes.

3. Consider gradually deprecating redundant methods in `PrettyStyle` in favor of the new utility classes.

4. Update unit tests to ensure compatibility with the new implementation.

### For Library Users

This refactoring maintains API compatibility, so existing code using the library should continue to function without changes. The improvements provide:

- Better performance with optimized string formatting
- More maintainable codebase with reduced duplication
- Clear separation of concerns with utility classes

## 4. Future Considerations

1. **Further Consolidation**
   - Consider if `PrettyStyle` and `Style` can be merged or better separated.
   - Evaluate if a more comprehensive color system would be beneficial.

2. **Enhanced Testing**
   - Create specific tests for the new utility classes.
   - Consider adding performance tests to verify optimizations.

3. **Additional Optimizations**
   - Consider using a String pool for common ANSI sequences.
   - Implement caching for frequently used styles.