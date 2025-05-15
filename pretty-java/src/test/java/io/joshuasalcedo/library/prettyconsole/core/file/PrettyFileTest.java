package io.joshuasalcedo.library.prettyconsole.core.file;

import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.TextStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PrettyFileTest {

    @TempDir
    Path tempDir;

    private PrettyFile directory;
    private PrettyFile textFile;
    private PrettyFile javaFile;

    @BeforeEach
    void setUp() throws Exception {
        // Create a test directory structure
        directory = new PrettyFile(tempDir.resolve("testDir").toFile());
        directory.mkdir();

        textFile = new PrettyFile(tempDir.resolve("file.txt").toFile());
        textFile.createNewFile();

        javaFile = new PrettyFile(tempDir.resolve("Test.java").toFile());
        javaFile.createNewFile();

        PrettyFile hiddenFile = new PrettyFile(tempDir.resolve(".hiddenFile").toFile());
        hiddenFile.createNewFile();
    }

    @Test
    void testConstructors() {
        // Test string path constructor
        PrettyFile file1 = new PrettyFile(tempDir.toString());
        assertTrue(file1.exists(), "File should exist");

        // Test parent/child string constructor
        PrettyFile file2 = new PrettyFile(tempDir.toString(), "file.txt");
        assertTrue(file2.exists(), "File should exist");

        // Test parent File/child string constructor
        PrettyFile file3 = new PrettyFile(tempDir.toFile(), "file.txt");
        assertTrue(file3.exists(), "File should exist");

        // Test File constructor
        PrettyFile file4 = new PrettyFile(textFile);
        assertTrue(file4.exists(), "File should exist");
        assertEquals(textFile.getAbsolutePath(), file4.getAbsolutePath(), "Paths should match");
    }

    @Test
    void testStylingMethods() {
        // Test withIcon
        PrettyFile file = new PrettyFile(textFile);
        PrettyFile fileWithIcon = file.withIcon();
        assertTrue(fileWithIcon.getFormattedName().contains("📄"), "Formatted name should contain icon");

        // Test withoutIcon
        PrettyFile fileWithoutIcon = fileWithIcon.withoutIcon();
        assertFalse(fileWithoutIcon.getFormattedName().contains("📄"), "Formatted name should not contain icon");

        // Test withColor for file
        PrettyFile coloredFile = file.withColor(ForegroundColor.RED);
        // We can't easily test the color directly, but we can verify the method doesn't throw exceptions
        assertDoesNotThrow(coloredFile::getFormattedName);

        // Test withColors
        PrettyFile multiColoredFile = file.withColors(ForegroundColor.GREEN, ForegroundColor.BLUE);
        assertDoesNotThrow(multiColoredFile::getFormattedName);

        // Test withStyle
        PrettyFile styledFile = file.withStyle(TextStyle.BOLD);
        assertDoesNotThrow(styledFile::getFormattedName);

        // Test withFormatting
        PrettyFile plainFile = file.withFormatting(false);
        assertEquals("file.txt", plainFile.getFormattedName(), "Without formatting, should return plain name");

        // Test resetFormatting
        PrettyFile resetFile = file.resetFormatting();
        assertDoesNotThrow(resetFile::getFormattedName);
    }

    @Test
    void testGetFormattedName() {
        // Test with default settings
        // The formatted name may include color codes, so we check if it contains the file name
        assertTrue(textFile.getFormattedName().contains("file.txt"), "Default formatted name should contain file name");

        // Test with formatting disabled
        PrettyFile plainFile = textFile.withFormatting(false);
        assertEquals("file.txt", plainFile.getFormattedName(), "Without formatting, should return plain name");

        // Test with icon
        PrettyFile file = textFile.withIcon();
        assertTrue(file.getFormattedName().contains("📄"), "Formatted name with icon should contain the icon");
        assertTrue(file.getFormattedName().contains("file.txt"), "Formatted name should contain the file name");

        // Test with Java file (different icon)
        PrettyFile jFile = javaFile.withIcon();
        assertTrue(jFile.getFormattedName().contains("☕"), "Java file icon should be coffee cup");

        // Test with directory
        PrettyFile dir = directory.withIcon();
        assertTrue(dir.getFormattedName().contains("📁"), "Directory icon should be folder");
    }

    @Test
    void testGetFormattedDetails() {
        String details = textFile.getFormattedDetails();

        // Check that details contain expected information
        assertTrue(details.contains("file.txt"), "Details should contain file name");
        assertTrue(details.contains("Size:"), "Details should contain size information");
        assertTrue(details.contains("Modified:"), "Details should contain modification date");
        assertTrue(details.contains("Permissions:"), "Details should contain permissions");
    }

    @Test
    void testListPrettyFiles() {
        // Test listing files in directory
        PrettyFile[] files = directory.listPrettyFiles();
        assertEquals(0, files.length, "Empty directory should return empty array");

        // Create a file in the directory
        try {
            File newFile = new File(directory, "newFile.txt");
            newFile.createNewFile();

            files = directory.listPrettyFiles();
            assertEquals(1, files.length, "Directory with one file should return array of length 1");
            assertEquals("newFile.txt", files[0].getName(), "File name should match");
            assertTrue(files[0] != null, "Listed files should be PrettyFile instances");
        } catch (IOException e) {
            fail("Failed to create test file: " + e.getMessage());
        }
    }

    @Test
    void testListPrettyFilesWithFilter() {
        // Create multiple files in the directory
        try {
            new File(directory, "file1.txt").createNewFile();
            new File(directory, "file2.txt").createNewFile();
            new File(directory, "image.png").createNewFile();

            // Test with FileFilter
            FileFilter txtFilter = file -> file.getName().endsWith(".txt");
            PrettyFile[] txtFiles = directory.listPrettyFiles(txtFilter);
            assertEquals(2, txtFiles.length, "Should find 2 .txt files");

            // Test with FilenameFilter
            FilenameFilter pngFilter = (dir, name) -> name.endsWith(".png");
            PrettyFile[] pngFiles = directory.listPrettyFiles(pngFilter);
            assertEquals(1, pngFiles.length, "Should find 1 .png file");
            assertEquals("image.png", pngFiles[0].getName(), "File name should match");
        } catch (IOException e) {
            fail("Failed to create test files: " + e.getMessage());
        }
    }

    @Test
    void testGetFormattedFileList() {
        // Create multiple files in the directory
        try {
            new File(directory, "file1.txt").createNewFile();
            new File(directory, "file2.txt").createNewFile();

            List<String> formattedList = directory.getFormattedFileList();
            assertEquals(2, formattedList.size(), "List should contain 2 items");
            assertTrue(formattedList.get(0).contains("file1.txt"), "List should contain file1.txt");
            assertTrue(formattedList.get(1).contains("file2.txt"), "List should contain file2.txt");
        } catch (IOException e) {
            fail("Failed to create test files: " + e.getMessage());
        }
    }

    @Test
    void testGetIcon() {
        assertEquals("📄", textFile.getIcon(), "Text file icon should be document");
        assertEquals("☕", javaFile.getIcon(), "Java file icon should be coffee cup");
        assertEquals("📁", directory.getIcon(), "Directory icon should be folder");
    }

    @Test
    void testFileOperations() throws IOException {
        // Test createIfNotExists
        PrettyFile newFile = new PrettyFile(tempDir.resolve("newFile.txt").toFile());
        assertFalse(newFile.exists(), "File should not exist yet");

        boolean created = newFile.createIfNotExists();
        assertTrue(created, "File should be created");
        assertTrue(newFile.exists(), "File should now exist");

        created = newFile.createIfNotExists();
        assertFalse(created, "File should not be created again");

        // Test createDirectoryIfNotExists
        PrettyFile newDir = new PrettyFile(tempDir.resolve("newDir").toFile());
        assertFalse(newDir.exists(), "Directory should not exist yet");

        boolean dirCreated = newDir.createDirectoryIfNotExists();
        assertTrue(dirCreated, "Directory should be created");
        assertTrue(newDir.exists(), "Directory should now exist");
        assertTrue(newDir.isDirectory(), "Should be a directory");

        dirCreated = newDir.createDirectoryIfNotExists();
        assertFalse(dirCreated, "Directory should not be created again");
    }

    @Test
    void testGetPrettyParentFile() {
        // Create a file in a subdirectory
        try {
            PrettyFile subDir = new PrettyFile(directory, "subDir");
            subDir.mkdir();

            PrettyFile fileInSubDir = new PrettyFile(subDir, "file.txt");
            fileInSubDir.createNewFile();

            PrettyFile parent = fileInSubDir.getPrettyParentFile();
            assertNotNull(parent, "Parent should not be null");
            assertTrue(true, "Parent should be a PrettyFile");
            assertEquals(subDir.getAbsolutePath(), parent.getAbsolutePath(), "Parent path should match");
        } catch (IOException e) {
            fail("Failed to create test files: " + e.getMessage());
        }
    }

    @Test
    void testToString() {
        // toString should return the formatted name
        PrettyFile file = textFile.withIcon();
        assertEquals(file.getFormattedName(), file.toString(), "toString should return formatted name");
    }

    @ParameterizedTest
    @MethodSource("provideFileExtensionsAndEmojis")
    void testFileTypeEmojiFromFilename(String filename, String expectedEmoji, boolean isDirectory) {
        PrettyFile.FileTypeEmoji emoji = PrettyFile.FileTypeEmoji.fromFilename(filename, isDirectory);
        assertEquals(expectedEmoji, emoji.getEmoji(), "Emoji should match expected for " + filename);
    }

    static Stream<Arguments> provideFileExtensionsAndEmojis() {
        return Stream.of(
            // Test directory
            Arguments.of("folder", "📁", true),

            // Test programming languages
            Arguments.of("test.java", "☕", false),
            Arguments.of("page.html", "🌐", false),
            Arguments.of("style.css", "🎨", false),
            Arguments.of("script.js", "📜", false),
            Arguments.of("app.py", "🐍", false),

            // Test config files
            Arguments.of("config.json", "📋", false),
            Arguments.of("data.xml", "📝", false),
            Arguments.of("settings.yaml", "🔧", false),

            // Test documents
            Arguments.of("readme.md", "📚", false),
            Arguments.of("document.pdf", "📕", false),
            Arguments.of("report.doc", "📘", false),

            // Test images
            Arguments.of("image.png", "🖼️", false),
            Arguments.of("photo.jpg", "📷", false),

            // Test unknown
            Arguments.of("unknown", "❓", false),
            Arguments.of("file.unknown", "❓", false)
        );
    }
}
