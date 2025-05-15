package io.joshuasalcedo.library.prettyconsole.format.layout;

import io.joshuasalcedo.library.prettyconsole.api.VisualFormat;
import io.joshuasalcedo.library.prettyconsole.core.file.PrettyFile;
import io.joshuasalcedo.library.prettyconsole.core.file.PrettyFileTreeNode;
import io.joshuasalcedo.library.prettyconsole.format.VisualizationType;
import io.joshuasalcedo.library.prettyconsole.style.ForegroundColor;
import io.joshuasalcedo.library.prettyconsole.style.Style;
import io.joshuasalcedo.library.prettyconsole.utils.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * Formatter that creates a visual representation of a file tree.
 * <p>
 * The FileTreeFormat creates a tree-like structure showing files and directories
 * with proper indentation and branch characters. It integrates with PrettyFile and
 * PrettyFileTreeNode to provide a rich visual representation of file hierarchies.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a file tree formatter with default settings
 * FileTreeFormat treeFormat = new FileTreeFormat();
 * System.out.println(treeFormat.format("/path/to/directory"));
 *
 * // Create a customized file tree formatter
 * FileTreeFormat customTree = new FileTreeFormat()
 *     .withMaxDepth(3)
 *     .withColor(ForegroundColor.BLUE);
 * System.out.println(customTree.format(new File("/path/to/directory")));
 * </pre>
 *
 * @author JoshuaSalcedo
 * @since 1.1.0
 */
public class FileTreeFormat implements VisualFormat {

    private final int width;
    private final ForegroundColor color;
    private final int maxDepth;
    private final FileFilter fileFilter;
    private final FilenameFilter filenameFilter;

    /**
     * Creates a FileTreeFormat with default settings (80 characters width, white color, unlimited depth).
     */
    public FileTreeFormat() {
        this(80, ForegroundColor.WHITE, Integer.MAX_VALUE, null, null);
    }

    /**
     * Creates a FileTreeFormat with the specified width.
     *
     * @param width The width of the tree display in characters
     */
    public FileTreeFormat(int width) {
        this(width, ForegroundColor.WHITE, Integer.MAX_VALUE, null, null);
    }

    /**
     * Creates a FileTreeFormat with the specified settings.
     *
     * @param width          The width of the tree display in characters
     * @param color          The color of the tree structure (branch characters)
     * @param maxDepth       The maximum depth of the tree to display
     * @param fileFilter     Optional filter for files (can be null)
     * @param filenameFilter Optional filter for filenames (can be null)
     */
    public FileTreeFormat(int width, ForegroundColor color, int maxDepth, 
                          FileFilter fileFilter, FilenameFilter filenameFilter) {
        this.width = width;
        this.color = color;
        this.maxDepth = maxDepth;
        this.fileFilter = fileFilter;
        this.filenameFilter = filenameFilter;
    }

    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.TREE;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public VisualFormat withWidth(int width) {
        return new FileTreeFormat(width, this.color, this.maxDepth, this.fileFilter, this.filenameFilter);
    }

    /**
     * Creates a new formatter with the specified color.
     *
     * @param color The color of the tree structure
     * @return A new formatter with the updated color
     */
    public FileTreeFormat withColor(ForegroundColor color) {
        return new FileTreeFormat(this.width, color, this.maxDepth, this.fileFilter, this.filenameFilter);
    }

    /**
     * Creates a new formatter with the specified maximum depth.
     *
     * @param maxDepth The maximum depth of the tree to display
     * @return A new formatter with the updated maximum depth
     */
    public FileTreeFormat withMaxDepth(int maxDepth) {
        return new FileTreeFormat(this.width, this.color, maxDepth, this.fileFilter, this.filenameFilter);
    }

    /**
     * Creates a new formatter with the specified file filter.
     *
     * @param fileFilter The filter to apply to files
     * @return A new formatter with the updated file filter
     */
    public FileTreeFormat withFileFilter(FileFilter fileFilter) {
        return new FileTreeFormat(this.width, this.color, this.maxDepth, fileFilter, this.filenameFilter);
    }

    /**
     * Creates a new formatter with the specified filename filter.
     *
     * @param filenameFilter The filter to apply to filenames
     * @return A new formatter with the updated filename filter
     */
    public FileTreeFormat withFilenameFilter(FilenameFilter filenameFilter) {
        return new FileTreeFormat(this.width, this.color, this.maxDepth, this.fileFilter, filenameFilter);
    }

    /**
     * Formats a file path string into a tree representation.
     *
     * @param filePath The path to the root directory or file
     * @return A formatted string representation of the file tree
     */
    @Override
    public String format(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        
        return format(new File(filePath));
    }

    /**
     * Formats a File object into a tree representation.
     *
     * @param file The root directory or file
     * @return A formatted string representation of the file tree
     */
    public String format(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        
        PrettyFile prettyFile = new PrettyFile(file).withIcon();
        return formatPrettyFile(prettyFile);
    }

    /**
     * Formats a PrettyFile object into a tree representation.
     *
     * @param file The root directory or file
     * @return A formatted string representation of the file tree
     */
    public String formatPrettyFile(PrettyFile file) {
        if (file == null || !file.exists()) {
            return "";
        }
        
        // Create the root node
        PrettyFileTreeNode rootNode = new PrettyFileTreeNode(file);
        
        // Build the tree with appropriate filters
        if (fileFilter != null) {
            rootNode.buildTreeFiltered(maxDepth, fileFilter);
        } else if (filenameFilter != null) {
            rootNode.buildTreeFiltered(maxDepth, filenameFilter);
        } else {
            rootNode.buildTree(maxDepth);
        }
        
        // Generate the tree string
        return rootNode.generateTreeString();
    }
}