package io.joshuasalcedo.pretty.core.model.file;

import io.joshuasalcedo.pretty.core.model.stream.PrettyPrintStream;
import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.theme.TerminalStyle;
import io.joshuasalcedo.pretty.core.utils.FileUtils;
import io.joshuasalcedo.pretty.core.utils.TerminalUtils;
import lombok.Getter;

import java.io.*;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of File that provides RGB color styling and visualization capabilities.
 * <p>
 * This class extends the standard Java File class, adding methods for styling
 * and formatting file names, as well as providing easy access to file metadata
 * in visually appealing formats with true color support.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a pretty file
 * PrettyFile file = new PrettyFile("/path/to/file.txt");
 *
 * // Get formatted name with icon and color
 * System.out.println(file.withIcon().withColor(RGBColor.of(0, 0, 255)).getFormattedName());
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

    // Default colors
    private static final RGBColor DEFAULT_FILE_COLOR = RGBColor.of(0, 230, 64).withName("bright-green");
    private static final RGBColor DEFAULT_DIR_COLOR = RGBColor.of(50, 150, 255).withName("bright-blue");

    private boolean showIcon = false;
    private RGBColor fileColor = DEFAULT_FILE_COLOR;
    private RGBColor dirColor = DEFAULT_DIR_COLOR;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean useFormatting = true;

    // Optional terminal style to use instead of individual style settings
    private TerminalStyle terminalStyle = null;

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
     * @param color The RGB color to apply
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withColor(RGBColor color) {
        if (isDirectory()) {
            this.dirColor = color;
        } else {
            this.fileColor = color;
        }
        this.terminalStyle = null; // Reset terminal style when setting individual color
        return this;
    }

    /**
     * Set separate colors for files and directories.
     *
     * @param fileColor The RGB color for files
     * @param dirColor The RGB color for directories
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withColors(RGBColor fileColor, RGBColor dirColor) {
        this.fileColor = fileColor;
        this.dirColor = dirColor;
        this.terminalStyle = null; // Reset terminal style when setting individual colors
        return this;
    }

    /**
     * Set the text to be bold.
     *
     * @param enabled Whether bold formatting should be enabled
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withBold(boolean enabled) {
        this.bold = enabled;
        this.terminalStyle = null; // Reset terminal style when setting individual style
        return this;
    }

    /**
     * Set the text to be italic.
     *
     * @param enabled Whether italic formatting should be enabled
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withItalic(boolean enabled) {
        this.italic = enabled;
        this.terminalStyle = null; // Reset terminal style when setting individual style
        return this;
    }

    /**
     * Set the text to be underlined.
     *
     * @param enabled Whether underline formatting should be enabled
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withUnderline(boolean enabled) {
        this.underline = enabled;
        this.terminalStyle = null; // Reset terminal style when setting individual style
        return this;
    }

    /**
     * Set a predefined terminal style for this file.
     * This will override individual color and style settings.
     *
     * @param style The terminal style to apply
     * @return This PrettyFile for method chaining
     */
    public PrettyFile withStyle(TerminalStyle style) {
        this.terminalStyle = style;
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
        this.fileColor = DEFAULT_FILE_COLOR;
        this.dirColor = DEFAULT_DIR_COLOR;
        this.bold = false;
        this.italic = false;
        this.underline = false;
        this.terminalStyle = null;
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

        if (!useFormatting || !TerminalUtils.isAnsiSupported()) {
            result.append(getName());
            return result.toString();
        }

        String name = getName();

        // If a terminal style is set, use it
        if (terminalStyle != null) {
            result.append(terminalStyle.apply(name));
            return result.toString();
        }

        // Otherwise, apply individual formatting
        StringBuilder formatCodes = new StringBuilder();

        // Apply text styles
        if (bold) formatCodes.append("\u001B[1m");
        if (italic) formatCodes.append("\u001B[3m");
        if (underline) formatCodes.append("\u001B[4m");

        // Apply color
        RGBColor color = isDirectory() ? dirColor : fileColor;
        formatCodes.append(color.toAnsi());

        // Format the name
        result.append(formatCodes);
        result.append(name);
        result.append("\u001B[0m"); // Reset formatting

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
            PrettyFile pf = new PrettyFile(files[i]);

            // Copy styling options
            pf.showIcon = this.showIcon;
            pf.fileColor = this.fileColor;
            pf.dirColor = this.dirColor;
            pf.bold = this.bold;
            pf.italic = this.italic;
            pf.underline = this.underline;
            pf.terminalStyle = this.terminalStyle;
            pf.useFormatting = this.useFormatting;

            prettyFiles[i] = pf;
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

        // Size with custom color
        String sizeStr = isDirectory() ? "<Directory>" : FileUtils.formatFileSize(length());
        if (useFormatting && TerminalUtils.isAnsiSupported()) {
            RGBColor sizeColor = RGBColor.of(255, 165, 0).withName("orange"); // Orange for size
            result.append("Size: ").append(sizeColor.apply(sizeStr)).append("\n");
        } else {
            result.append("Size: ").append(sizeStr).append("\n");
        }

        // Modified date with custom color
        LocalDateTime lastModified = Instant.ofEpochMilli(lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        String dateStr = DATE_FORMAT.format(lastModified);

        if (useFormatting && TerminalUtils.isAnsiSupported()) {
            RGBColor dateColor = RGBColor.of(0, 191, 255).withName("deep-sky-blue"); // Blue for date
            result.append("Modified: ").append(dateColor.apply(dateStr)).append("\n");
        } else {
            result.append("Modified: ").append(dateStr).append("\n");
        }

        // Permissions with custom colors
        result.append("Permissions: ");
        if (useFormatting && TerminalUtils.isAnsiSupported()) {
            RGBColor readColor = RGBColor.of(0, 200, 0).withName("green"); // Green for read
            RGBColor writeColor = RGBColor.of(255, 69, 0).withName("red-orange"); // Red-orange for write
            RGBColor execColor = RGBColor.of(138, 43, 226).withName("blue-violet"); // Blue-violet for execute

            result.append(canRead() ? readColor.apply("r") : "-");
            result.append(canWrite() ? writeColor.apply("w") : "-");
            result.append(canExecute() ? execColor.apply("x") : "-");
        } else {
            result.append(canRead() ? "r" : "-");
            result.append(canWrite() ? "w" : "-");
            result.append(canExecute() ? "x" : "-");
        }

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
     * Print a formatted list of files in this directory to the console.
     */
    public void printFormattedFileList() {
        PrettyFile[] files = listPrettyFiles();

        for (PrettyFile file : files) {
            System.out.println(file.getFormattedName());
        }
    }

    /**
     * Print a formatted list of files in this directory to a PrettyPrintStream.
     *
     * @param out The PrettyPrintStream to print to
     */
    public void printFormattedFileList(PrettyPrintStream out) {
        PrettyFile[] files = listPrettyFiles();

        for (PrettyFile file : files) {
            // For PrettyPrintStream, we can just print the name and let the stream apply the formatting
            if (file.isDirectory()) {
                out.foreground(dirColor);
            } else {
                out.foreground(fileColor);
            }
            out.bold(bold).italic(italic).underline(underline);

            if (showIcon) {
                out.print(file.getIcon() + " ");
            }

            out.println(file.getName());
            out.reset(); // Reset formatting for next item
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

    /**
     * Compares this PrettyFile with another object for equality.
     * Two PrettyFile objects are considered equal if they refer to the same file
     * and have the same formatting settings.
     *
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrettyFile other)) {
            return false;
        }
        // First check if the underlying File objects are equal
        if (!super.equals(obj)) {
            return false;
        }
        // Then check the additional fields
        return showIcon == other.showIcon &&
                useFormatting == other.useFormatting &&
                bold == other.bold &&
                italic == other.italic &&
                underline == other.underline &&
                (fileColor == null ? other.fileColor == null : fileColor.equals(other.fileColor)) &&
                (dirColor == null ? other.dirColor == null : dirColor.equals(other.dirColor)) &&
                (terminalStyle == null ? other.terminalStyle == null : terminalStyle.equals(other.terminalStyle));
    }

    /**
     * Returns a hash code for this PrettyFile.
     * The hash code is based on the underlying File and the formatting settings.
     *
     * @return A hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (showIcon ? 1 : 0);
        result = 31 * result + (useFormatting ? 1 : 0);
        result = 31 * result + (bold ? 1 : 0);
        result = 31 * result + (italic ? 1 : 0);
        result = 31 * result + (underline ? 1 : 0);
        result = 31 * result + (fileColor != null ? fileColor.hashCode() : 0);
        result = 31 * result + (dirColor != null ? dirColor.hashCode() : 0);
        result = 31 * result + (terminalStyle != null ? terminalStyle.hashCode() : 0);
        return result;
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

        PrettyFile prettyParent = new PrettyFile(parent);

        // Copy styling options
        prettyParent.showIcon = this.showIcon;
        prettyParent.fileColor = this.fileColor;
        prettyParent.dirColor = this.dirColor;
        prettyParent.bold = this.bold;
        prettyParent.italic = this.italic;
        prettyParent.underline = this.underline;
        prettyParent.terminalStyle = this.terminalStyle;
        prettyParent.useFormatting = this.useFormatting;

        return prettyParent;
    }

    /**
     * Demonstrates the PrettyFile capabilities.
     */
    public static void main(String[] args) {
        // Create a PrettyFile for the current directory
        PrettyFile currentDir = new PrettyFile(System.getProperty("user.dir"))
                .withIcon()
                .withColors(
                        RGBColor.of(112, 209, 147), // Light green for files
                        RGBColor.of(79, 148, 205)   // Blue for directories
                )
                .withBold(true);

        System.out.println("Current Directory: " + currentDir.getFormattedName());
        System.out.println("\nDirectory Contents:");

        PrettyFile[] files = currentDir.listPrettyFiles();

        // Create a PrettyPrintStream to display the files
        PrettyPrintStream out = new PrettyPrintStream(System.out);

        // Group files by type for better organization
        out.foreground(RGBColor.of(255, 215, 0)).bold(true).println("\n== DIRECTORIES ==");
        for (PrettyFile file : files) {
            if (file.isDirectory()) {
                out.println(file.getFormattedName());
            }
        }

        out.foreground(RGBColor.of(255, 215, 0)).bold(true).println("\n== FILES ==");
        for (PrettyFile file : files) {
            if (!file.isDirectory()) {
                out.println(file.getFormattedName());
            }
        }

        // Show detailed information for a specific file
        if (files.length > 0) {
            PrettyFile sampleFile = files[0];
            out.foreground(RGBColor.of(255, 215, 0)).bold(true).println("\n== FILE DETAILS ==");
            out.foreground(RGBColor.of(255, 255, 255)).println(sampleFile.getFormattedDetails());
        }

        // Show file information with TerminalStyle
        out.style(TerminalStyle.UI_HEADER).println("\n== USING TERMINAL STYLES ==");

        for (int i = 0; i < Math.min(files.length, 5); i++) {
            PrettyFile file = files[i];
            if (file.isDirectory()) {
                out.println(TerminalStyle.UI_MENU, file.getName());
            } else {
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();
                switch (extension) {
                    case "java", "class" -> out.println(TerminalStyle.DOC_CODE, file.getName());
                    case "md", "txt" -> out.println(TerminalStyle.DOC_SECTION, file.getName());
                    case "json", "xml", "yaml", "yml" -> out.println(TerminalStyle.CONFIG_INFO, file.getName());
                    case "log" -> out.println(TerminalStyle.DEBUG, file.getName());
                    default -> out.println(TerminalStyle.PLAIN, file.getName());
                }
            }
        }

        out.reset();
    }
}