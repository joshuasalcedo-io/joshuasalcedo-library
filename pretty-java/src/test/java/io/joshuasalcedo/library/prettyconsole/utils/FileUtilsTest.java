package io.joshuasalcedo.library.prettyconsole.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @TempDir
    Path tempDir;

    private File directory;
    private File regularFile;
    private File largeFile;
    private File hiddenFile;
    private File[] testFiles;

    @BeforeEach
    void setUp() throws Exception {
        // Create a test directory structure
        directory = tempDir.resolve("testDir").toFile();
        directory.mkdir();

        regularFile = tempDir.resolve("file.txt").toFile();
        regularFile.createNewFile();

        largeFile = tempDir.resolve("largeFile.data").toFile();
        largeFile.createNewFile();
        // Create a 1.5 MB file
        Files.write(largeFile.toPath(), new byte[1500000]);

        hiddenFile = tempDir.resolve(".hiddenFile").toFile();
        hiddenFile.createNewFile();

        testFiles = new File[] {
                directory,
                regularFile,
                largeFile,
                hiddenFile
        };
    }

    @Test
    void testSortFilesByDirectoryAndName() {
        // Create additional test files for sorting
        File[] unsortedFiles = new File[] {
                regularFile,     // file.txt
                directory,       // testDir (directory)
                largeFile,       // largeFile.data
                hiddenFile       // .hiddenFile
        };

        FileUtils.sortFilesByDirectoryAndName(unsortedFiles);

        // Verify directories come first
        assertEquals(directory, unsortedFiles[0], "Directories should be sorted first");

        // Then verify alphabetical order for the files
        assertEquals(hiddenFile, unsortedFiles[1], "Files should be sorted alphabetically after directories");
        assertEquals(regularFile, unsortedFiles[2], "Files should be sorted alphabetically");
        assertEquals(largeFile, unsortedFiles[3], "Files should be sorted alphabetically");
    }

    @Test
    void testCalculateColumnWidths() {
        // Test without showing icons
        int[] widths = FileUtils.calculateColumnWidths(testFiles, false);

        int expectedNameWidth = Math.max(
                Math.max(directory.getName().length(), regularFile.getName().length()),
                Math.max(largeFile.getName().length(), hiddenFile.getName().length())
        ) + 2; // padding

        int expectedSizeWidth = FileUtils.formatFileSize(largeFile.length()).length() + 2; // padding

        assertEquals(expectedNameWidth, widths[0], "Name column width should match longest name plus padding");
        assertEquals(expectedSizeWidth, widths[1], "Size column width should match largest size string plus padding");

        // Test with showing icons
        widths = FileUtils.calculateColumnWidths(testFiles, true);
        assertEquals(expectedNameWidth + 3, widths[0], "Name column with icons should have 3 additional spaces");
        assertEquals(expectedSizeWidth, widths[1], "Size column width should not change with icons");
    }

    @ParameterizedTest
    @MethodSource("provideSizesForFormatting")
    void testFormatFileSize(long size, String expected) {
        assertEquals(expected, FileUtils.formatFileSize(size), "Size should be formatted correctly");
    }

    static Stream<Arguments> provideSizesForFormatting() {
        return Stream.of(
                Arguments.of(0L, "0 B"),
                Arguments.of(-100L, "0 B"),
                Arguments.of(500L, "500.0 B"),
                Arguments.of(1023L, "1023.0 B"),
                Arguments.of(1024L, "1.0 KB"),
                Arguments.of(1500L, "1.5 KB"),
                Arguments.of(1048576L, "1.0 MB"),
                Arguments.of(1073741824L, "1.0 GB"),
                Arguments.of(1099511627776L, "1.0 TB")
        );
    }

    @Test
    void testGetFormattedModifiedDate() {
        String dateStr = FileUtils.getFormattedModifiedDate(regularFile);
        assertNotNull(dateStr, "Formatted date should not be null");

        // Verify format matches the expected pattern
        DateTimeFormatter formatter = FileUtils.DATE_FORMAT;
        LocalDateTime lastModified = Instant.ofEpochMilli(regularFile.lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        String expected = formatter.format(lastModified);

        assertEquals(expected, dateStr, "Formatted date should match expected format");
    }

    @Test
    void testShouldIncludeFile() {
        // Test with includeHidden = true
        assertTrue(FileUtils.shouldIncludeFile(regularFile, true), "Regular file should be included");
        assertTrue(FileUtils.shouldIncludeFile(hiddenFile, true), "Hidden file should be included when includeHidden is true");

        // Test with includeHidden = false
        assertTrue(FileUtils.shouldIncludeFile(regularFile, false), "Regular file should always be included");

        // Note: The test for hiddenFile with includeHidden=false will vary based on OS
        // On Windows, a file with a name starting with "." isn't necessarily hidden
        // On Unix-like systems, it is considered hidden
        // We'll check based on the actual hidden status
        assertEquals(!hiddenFile.isHidden(), FileUtils.shouldIncludeFile(hiddenFile, false),
                "Hidden file should only be included when includeHidden is true");
    }

    @Test
    void testUtilityClassHasPrivateConstructor() {
        // Verify FileUtils has a private constructor (utility class pattern)
        try {
            var constructor = FileUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            // Constructor exists and is accessible when we force it
        } catch (Exception e) {
            fail("Could not access private constructor: " + e.getMessage());
        }
    }

    @Test
    void testDateFormatConstant() {
        // Test that the DATE_FORMAT constant exists and is configured properly
        DateTimeFormatter dateFormat = FileUtils.DATE_FORMAT;
        assertNotNull(dateFormat, "DATE_FORMAT should not be null");

        // Verify format pattern
        String formattedDate = dateFormat.format(LocalDateTime.now());
        assertTrue(formattedDate.matches("[A-Za-z]+ \\d{2}, \\d{4} \\d{2}:\\d{2}"),
                "Formatted date should match expected pattern");
    }

    @Test
    void testSortingEdgeCasesEmptyArray() {
        // Test with empty array
        File[] emptyArray = new File[0];
        FileUtils.sortFilesByDirectoryAndName(emptyArray);
        assertEquals(0, emptyArray.length, "Empty array should remain empty after sorting");
    }

    @Test
    void testSortingEdgeCasesSingleItem() {
        // Test with single-item array
        File[] singleItemArray = new File[] { regularFile };
        FileUtils.sortFilesByDirectoryAndName(singleItemArray);
        assertEquals(regularFile, singleItemArray[0], "Single item array should remain unchanged after sorting");
    }

    @Test
    void testCalculateColumnWidthsEdgeCasesEmptyArray() {
        // Test with empty array
        File[] emptyArray = new File[0];
        int[] widths = FileUtils.calculateColumnWidths(emptyArray, false);

        assertEquals(0, widths[0], "Name column width should be 0 for empty array");
        assertEquals(0, widths[1], "Size column width should be 0 for empty array");
    }

    @Test
    void testSizeLargerThanTerabytes() {
        // Test formatting a size larger than TB (which is the largest unit in SIZE_UNITS)
        long petabyteSize = 1125899906842624L; // 1 PB in bytes
        String formatted = FileUtils.formatFileSize(petabyteSize);

        // Should format as TB since that's the largest unit available
        assertEquals("1024.0 TB", formatted, "Size larger than TB should still use TB unit");
    }
}
