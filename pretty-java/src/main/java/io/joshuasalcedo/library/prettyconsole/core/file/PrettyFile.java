package io.joshuasalcedo.library.prettyconsole.core.file;

import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.style.StyleFormatter;
import io.joshuasalcedo.library.prettyconsole.style.TextStyle;
import io.joshuasalcedo.library.prettyconsole.utils.FileUtils;

import java.io.*;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;

/**
 * An extension of File that provides styling and visualization capabilities.
 * <p>
 * This class extends the standard Java File class, adding methods for styling
 * and formatting file names, as well as providing easy access to file metadata
 * in visually appealing formats.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a pretty file
 * PrettyFile file = new PrettyFile("/path/to/file.txt");
 *
 * // Get formatted name with icon and color
 * System.out.println(file.withIcon().withColor(ForegroundColor.BLUE).getFormattedName());
 *
 * // Print file details
 * System.out.println(file.getFormattedDetails());
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.1.0
 */
public class PrettyFile extends File {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private boolean showIcon = false;
    private ForegroundColor fileColor = ForegroundColor.BRIGHT_GREEN;
    private ForegroundColor dirColor = ForegroundColor.BRIGHT_BLUE;
    private TextStyle style = null;
    private boolean useFormatting = true;

    /**
     * Enum representing file types with their shell-compatible emoji equivalents.
     * These emojis are selected to work well in most terminal environments.
     */
    @Getter
    public enum FileTypeEmoji {
        // Programming Languages
        JAVA("☕"),
        HTML_FILE("🌐"),
        CSS_FILE("🎨"),
        JS("📜"),
        TS("📘"),
        PY("🐍"),
        RB("💎"),
        PHP("🐘"),
        CS("🔷"),
        CPP("⚙️"),
        C("🔨"),
        GO("🔹"),
        RS("🦀"),
        SWIFT("🔸"),
        KOTLIN("🧩"),
        DART("🎯"),
        LUA("🌙"),
        R("📊"),
        SCALA("📐"),
        PERL("🐫"),
        HASKELL("λ"),
        SQL("🗃️"),

        // Config Files
        JSON("📋"),
        XML("📝"),
        YAML("🔧"),
        TOML("⚙️"),
        INI("🔩"),
        PROPERTIES("🔑"),

        // Markup/Documentation
        MD("📚"),
        RST("📖"),
        TEX("📄"),
        PDF("📕"),
        DOC("📘"),
        XLS("📗"),
        PPT("📙"),

        // Build/Package Management
        GRADLE("🏗️"),
        MVN("🏺"),
        POM("🧱"),

        // Version Control
        GIT("🔄"),

        // Images
        PNG("🖼️"),
        JPG("📷"),
        SVG("🔍"),
        GIF("🎬"),

        // Video/Audio
        MP4("📹"),
        MP3("🎵"),
        WAV("🔊"),

        // Archives
        ZIP("📦"),
        JAR("📦"),
        WAR("📦"),
        TAR("📚"),

        // Other
        LOG("📜"),
        TXT("📄"),
        BIN("💾"),
        EXE("⚡"),
        SH("🐚"),
        BAT("🖥️"),

        // Folders
        DIR("📁"),

        // Unknown
        UNKNOWN("❓");

        /**
         * -- GETTER --
         *  Get the emoji representation
         *
         */
        private final String emoji;

        /**
         * Constructor for FileTypeEmoji
         * @param emoji The emoji representation
         */
        FileTypeEmoji(String emoji) {
            this.emoji = emoji;
        }

        /**
         * Get the appropriate FileTypeEmoji for a given filename
         * @param filename The name of the file
         * @param isDirectory Whether the path is a directory
         * @return The matching FileTypeEmoji
         */
        public static FileTypeEmoji fromFilename(String filename, boolean isDirectory) {
            if (isDirectory) {
                return DIR;
            }

            if (filename == null || filename.isEmpty()) {
                return UNKNOWN;
            }

            int lastDotIndex = filename.lastIndexOf('.');
            if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
                return UNKNOWN;
            }

            String extension = filename.substring(lastDotIndex + 1).toLowerCase();

            return switch (extension) {
                // Programming Languages
                case "java" -> JAVA;
                case "html", "htm" -> HTML_FILE;
                case "css" -> CSS_FILE;
                case "js", "mjs" -> JS;
                case "ts" -> TS;
                case "py", "pyc", "pyd", "pyo", "pyw", "pyz" -> PY;
                case "rb", "rbw" -> RB;
                case "php", "phtml", "php3", "php4", "php5", "php7", "phps", "php-s", "pht" -> PHP;
                case "cs" -> CS;
                case "cpp", "cc", "cxx", "c++", "cp", "hpp", "h++", "hh" -> CPP;
                case "c", "h" -> C;
                case "go" -> GO;
                case "rs" -> RS;
                case "swift" -> SWIFT;
                case "kt", "kts" -> KOTLIN;
                case "dart" -> DART;
                case "lua" -> LUA;
                case "r", "rdata", "rds", "rda" -> R;
                case "scala", "sc" -> SCALA;
                case "pl", "pm", "t", "pod" -> PERL;
                case "hs", "lhs" -> HASKELL;
                case "sql", "msql", "mysql", "pgsql", "tsql" -> SQL;
                // Config Files
                case "json" -> JSON;
                case "xml" -> XML;
                case "yml", "yaml" -> YAML;
                case "toml" -> TOML;
                case "ini" -> INI;
                case "properties", "props", "prop" -> PROPERTIES;
                // Markup/Documentation
                case "md", "markdown", "mdown", "mkdn" -> MD;
                case "rst" -> RST;
                case "tex", "latex" -> TEX;
                case "pdf" -> PDF;
                case "doc", "docx" -> DOC;
                case "xls", "xlsx", "xlsm" -> XLS;
                case "ppt", "pptx", "pptm" -> PPT;
                // Build/Package Management
                case "gradle" -> GRADLE;
                case "pom" -> POM;
                // Version Control
                case "git", "gitignore", "gitattributes", "gitmodules" -> GIT;
                // Images
                case "png" -> PNG;
                case "jpg", "jpeg", "jpe", "jif", "jfif", "jfi" -> JPG;
                case "svg" -> SVG;
                case "gif" -> GIF;
                // Video/Audio
                case "mp4", "m4v", "mov", "avi", "wmv", "flv", "webm" -> MP4;
                case "mp3", "m4a", "aac", "ogg" -> MP3;
                case "wav", "wave", "flac" -> WAV;
                // Archives
                case "zip", "gz", "bz2", "7z", "rar", "z" -> ZIP;
                case "jar" -> JAR;
                case "war", "ear" -> WAR;
                case "tar", "tgz" -> TAR;
                // Other
                case "log" -> LOG;
                case "txt", "text" -> TXT;
                case "bin", "dat" -> BIN;
                case "exe", "msi", "app" -> EXE;
                case "sh", "bash", "zsh", "ksh" -> SH;
                case "bat", "cmd" -> BAT;
                default -> UNKNOWN;
            };
        }
    }

    // Constructors that mirror the File constructors

    public PrettyFile(String pathname) {
        super(pathname);
    }

    public PrettyFile(String parent, String child) {
        super(parent, child);
    }

    public PrettyFile(File parent, String child) {
        super(parent, child);
    }

    public PrettyFile(URI uri) {
        super(uri);
    }

    // Additional constructors for creating from existing File objects

    public PrettyFile(File file) {
        super(file.getAbsolutePath());
    }

    // Configuration methods for styling

    /**
     * Enable icon display for this file.
     *
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withIcon() {
        this.showIcon = true;
        return this;
    }

    /**
     * Disable icon display for this file.
     *
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withoutIcon() {
        this.showIcon = false;
        return this;
    }

    /**
     * Set the color for this file's name.
     *
     * @param color The color to apply
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withColor(ForegroundColor color) {
        if (isDirectory()) {
            this.dirColor = color;
        } else {
            this.fileColor = color;
        }
        return this;
    }

    /**
     * Set separate colors for files and directories.
     *
     * @param fileColor The color for files
     * @param dirColor The color for directories
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withColors(ForegroundColor fileColor, ForegroundColor dirColor) {
        this.fileColor = fileColor;
        this.dirColor = dirColor;
        return this;
    }

    /**
     * Set the text style for this file's name.
     *
     * @param style The style to apply
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withStyle(TextStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Enable or disable formatting.
     *
     * @param enabled Whether formatting should be enabled
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withFormatting(boolean enabled) {
        this.useFormatting = enabled;
        return this;
    }

    /**
     * Reset all formatting options to defaults.
     *
     * @return This PrettyFile for method chaining
     */
    public PrettyFile resetFormatting() {
        this.showIcon = false;
        this.fileColor = ForegroundColor.BRIGHT_GREEN;
        this.dirColor = ForegroundColor.BRIGHT_BLUE;
        this.style = null;
        this.useFormatting = true;
        return this;
    }

    // Output methods

    /**
     * Get the formatted name of this file with applied styling.
     *
     * @return The formatted name
     */
    public String getFormattedName() {
        StringBuilder result = new StringBuilder();

        if (showIcon) {
            FileTypeEmoji emoji = FileTypeEmoji.fromFilename(getName(), isDirectory());
            result.append(emoji.getEmoji()).append(" ");
        }

        if (!useFormatting) {
            result.append(getName());
            return result.toString();
        }

        StyleFormatter formatter = new StyleFormatter();

        if (style != null) {
            formatter.withStyle(style);
        }

        if (isDirectory()) {
            formatter.withColor(dirColor);
        } else {
            formatter.withColor(fileColor);
        }

        result.append(Style.safeApply(formatter, getName()));
        return result.toString();
    }

    /**
     * List files in this directory with pretty formatting.
     *
     * @return An array of PrettyFile objects
     */
    public PrettyFile[] listPrettyFiles() {
        if (!isDirectory()) {
            return new PrettyFile[0];
        }

        File[] files = listFiles();
        return getPrettyFiles(files);
    }

    /**
     * List files in this directory with pretty formatting, filtered by a FileFilter.
     *
     * @param filter The filter to apply
     * @return An array of PrettyFile objects
     */
    public PrettyFile[] listPrettyFiles(FileFilter filter) {
        if (!isDirectory()) {
            return new PrettyFile[0];
        }

        File[] files = listFiles(filter);
        return getPrettyFiles(files);
    }

    /**
     * List files in this directory with pretty formatting, filtered by a FilenameFilter.
     *
     * @param filter The filter to apply
     * @return An array of PrettyFile objects
     */
    public PrettyFile[] listPrettyFiles(FilenameFilter filter) {
        if (!isDirectory()) {
            return new PrettyFile[0];
        }

        File[] files = listFiles(filter);
        return getPrettyFiles(files);
    }

    /**
     * Convert an array of File objects to PrettyFile objects.
     *
     * @param files The array of File objects
     * @return An array of PrettyFile objects
     */
    private PrettyFile[] getPrettyFiles(File[] files) {
        if (files == null) {
            return new PrettyFile[0];
        }

        PrettyFile[] prettyFiles = new PrettyFile[files.length];
        for (int i = 0; i < files.length; i++) {
            prettyFiles[i] =
                new PrettyFile(files[i])
                    .withIcon()
                    .withColors(fileColor, dirColor)
                    .withStyle(style)
                    .withFormatting(useFormatting);
        }

        return prettyFiles;
    }

    /**
     * Get formatted details about this file including size and modification date.
     *
     * @return The formatted details
     */
    public String getFormattedDetails() {
        StringBuilder result = new StringBuilder();

        result.append(getFormattedName());
        result.append("\n");

        // Size
        String sizeStr = isDirectory() ? "<Directory>" : FileUtils.formatFileSize(length());

        result.append("Size: ").append(sizeStr).append("\n");

        // Modified date
        LocalDateTime lastModified = Instant.ofEpochMilli(lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        result.append("Modified: ").append(DATE_FORMAT.format(lastModified)).append("\n");

        // Permissions
        result.append("Permissions: ");
        result.append(canRead() ? "r" : "-");
        result.append(canWrite() ? "w" : "-");
        result.append(canExecute() ? "x" : "-");

        return result.toString();
    }

    /**
     * Get the formatted names of all files in this directory.
     *
     * @return A list of formatted file names
     */
    public List<String> getFormattedFileList() {
        PrettyFile[] files = listPrettyFiles();
        List<String> formattedNames = new ArrayList<>(files.length);

        for (PrettyFile file : files) {
            formattedNames.add(file.getFormattedName());
        }

        return formattedNames;
    }

    /**
     * Print a formatted list of files in this directory to System.out.
     */
    public void printFormattedFileList() {
        PrettyFile[] files = listPrettyFiles();

        for (PrettyFile file : files) {
            System.out.println(file.getFormattedName());
        }
    }

    /**
     * Get the icon for this file based on its type.
     *
     * @return The emoji icon
     */
    public String getIcon() {
        return FileTypeEmoji.fromFilename(getName(), isDirectory()).getEmoji();
    }

    /**
     * Return the formatted name when toString is called.
     *
     * @return The formatted name
     */
    @Override
    public String toString() {
        return getFormattedName();
    }

    // Additional file-related utility methods

    /**
     * Create an empty file with this path if it doesn't exist.
     *
     * @return true if the file was created, false if it already exists
     * @throws IOException if an I/O error occurred or if parent directories could not be created
     */
    public boolean createIfNotExists() throws IOException {
        if (!exists()) {
            File parent = getParentFile();
            if (parent != null && !parent.exists()) {
                boolean dirCreated = parent.mkdirs();
                if (!dirCreated) {
                    throw new IOException("Failed to create parent directories for " + getAbsolutePath());
                }
            }
            return createNewFile();
        }
        return false;
    }

    /**
     * Create a directory with this path if it doesn't exist.
     *
     * @return true if the directory was created, false if it already exists
     */
    public boolean createDirectoryIfNotExists() {
        if (!exists()) {
            return mkdirs();
        }
        return false;
    }

    /**
     * Get a PrettyFile representing the parent directory.
     *
     * @return A PrettyFile for the parent directory, or null if this file has no parent
     */
    public PrettyFile getPrettyParentFile() {
        File parent = getParentFile();
        if (parent == null) {
            return null;
        }
        return new PrettyFile(parent)
            .withIcon()
            .withColors(fileColor, dirColor)
            .withStyle(style)
            .withFormatting(useFormatting);
    }
}
