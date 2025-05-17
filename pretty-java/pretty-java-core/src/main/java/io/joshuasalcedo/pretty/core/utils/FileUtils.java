package io.joshuasalcedo.pretty.core.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Utility class for file operations and information gathering.
 * <p>
 * This class provides utility methods for file operations without any formatting logic.
 * It's focused on file processing functionality that can be used by other components.
 * </p>
 *
 * @author JoshuaSalcedo
 * @since 1.1.0
 */
public final class FileUtils {

    private static final String[] SIZE_UNITS = { "B", "KB", "MB", "GB", "TB" };
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    private FileUtils() {
        // Utility class, prevent instantiation
    }

    /**
     * Sort files with directories first, then alphabetically
     *
     * @param files The array of files to sort
     */
    public static void sortFilesByDirectoryAndName(File[] files) {
        Arrays.sort(
            files,
            (a, b) -> {
                if (a.isDirectory() && !b.isDirectory()) {
                    return -1;
                } else if (!a.isDirectory() && b.isDirectory()) {
                    return 1;
                } else {
                    return a.getName().compareTo(b.getName());
                }
            }
        );
    }

    /**
     * Calculate column widths for detailed file listing
     *
     * @param files The array of files
     * @param showIcons Whether icons are being displayed
     * @return An int array with [nameColWidth, sizeColWidth]
     */
    public static int[] calculateColumnWidths(File[] files, boolean showIcons) {
        int nameColWidth = 0;
        int sizeColWidth = 0;

        for (File file : files) {
            nameColWidth = Math.max(nameColWidth, file.getName().length());

            if (!file.isDirectory()) {
                String sizeStr = formatFileSize(file.length());
                sizeColWidth = Math.max(sizeColWidth, sizeStr.length());
            }
        }

        // Add extra space for icons if needed
        if (showIcons) {
            nameColWidth += 3; // emoji + space
        }

        // Add some padding
        nameColWidth += 2;
        sizeColWidth += 2;

        return new int[] { nameColWidth, sizeColWidth };
    }

    /**
     * Format a file size in human-readable format
     *
     * @param size The size in bytes
     * @return A formatted string representation of the size
     */
    public static String formatFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }

        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        digitGroups = Math.min(digitGroups, SIZE_UNITS.length - 1);

        double value = size / Math.pow(1024, digitGroups);
        return String.format("%.1f %s", value, SIZE_UNITS[digitGroups]);
    }

    /**
     * Get a formatted date string for a file's last modified time
     *
     * @param file The file to get the last modified date from
     * @return A formatted date string
     */
    public static String getFormattedModifiedDate(File file) {
        Date lastModified = new Date(file.lastModified());
        return DATE_FORMAT.format(lastModified);
    }

    /**
     * Check if a file is hidden and should be included based on the includeHidden parameter
     *
     * @param file The file to check
     * @param includeHidden Whether to include hidden files
     * @return True if the file should be included, false otherwise
     */
    public static boolean shouldIncludeFile(File file, boolean includeHidden) {
        return includeHidden || !file.isHidden();
    }
}
