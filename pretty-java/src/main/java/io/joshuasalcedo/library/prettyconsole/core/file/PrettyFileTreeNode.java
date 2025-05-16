package io.joshuasalcedo.library.prettyconsole.core.file;

import lombok.Data;

import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A node in a file tree structure that works with PrettyFile.
 * This class represents a single node in a file tree, containing a PrettyFile
 * and maintaining references to its child nodes.
 */
@Data
public class PrettyFileTreeNode {

    // The PrettyFile this node represents
    private PrettyFile file;

    // List of child nodes
    private List<PrettyFileTreeNode> children;

    // Depth of this node in the tree
    private int depth;

    // Whether this node is the last child of its parent
    private boolean isLastChild;

    /**
     * Create a new PrettyFileTreeNode for the given PrettyFile.
     */
    public PrettyFileTreeNode(PrettyFile file) {
        this.file = file;
        this.children = new ArrayList<>();
        this.depth = 0;
        this.isLastChild = false;
    }

    /**
     * Add a child node to this node.
     */
    public void addChild(PrettyFileTreeNode child) {
        children.add(child);
        // Update depth of the child
        child.setDepth(this.depth + 1);
    }

    /**
     * Build a tree of FileTreeNodes starting from this node.
     * This recursively adds child nodes for all files and directories.
     */
    public void buildTree(int maxDepth) {
        if (file.isDirectory() && depth < maxDepth) {
            PrettyFile[] files = file.listPrettyFiles();
            for (int i = 0; i < files.length; i++) {
                PrettyFile childFile = files[i];
                PrettyFileTreeNode childNode = new PrettyFileTreeNode(childFile);
                childNode.setLastChild(i == files.length - 1);
                addChild(childNode);
                childNode.buildTree(maxDepth);
            }
        }
    }

    /**
     * Build a complete tree of FileTreeNodes starting from this node.
     * This recursively adds child nodes for all files and directories without a depth limit.
     */
    public void buildTree() {
        buildTree(Integer.MAX_VALUE);
    }

    /**
     * Generate a string representation of the tree from this node down.
     */
    public String generateTreeString() {
        StringBuilder builder = new StringBuilder();
        generateTreeString(builder, "");
        return builder.toString();
    }

    /**
     * Generate a string representation of the tree, recursively.
     */
    private void generateTreeString(StringBuilder builder, String prefix) {
        // Add this node's representation
        builder.append(prefix);

        // Choose the appropriate branch character
        if (depth > 0) {
            if (isLastChild) {
                builder.append("└── ");
            } else {
                builder.append("├── ");
            }
        }

        // Add the file name
        builder.append(file.getFormattedName()).append("\n");

        // Recursively add children
        String childPrefix = prefix;
        if (depth > 0) {
            childPrefix += isLastChild ? "    " : "│   ";
        }

        for (PrettyFileTreeNode child : children) {
            child.generateTreeString(builder, childPrefix);
        }
    }

    /**
     * Build a tree of FileTreeNodes starting from this node, filtering files.
     * This recursively adds child nodes only for files that match the filter.
     */
    public void buildTreeFiltered(int maxDepth, FileFilter filter) {
        if (file.isDirectory() && depth < maxDepth) {
            // Make sure to use the FileFilter version of listPrettyFiles
            PrettyFile[] files = file.listPrettyFiles(filter);

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    PrettyFile childFile = files[i];
                    PrettyFileTreeNode childNode = new PrettyFileTreeNode(childFile);
                    childNode.setLastChild(i == files.length - 1);
                    addChild(childNode);

                    // Only recurse if the child is a directory
                    if (childFile.isDirectory()) {
                        childNode.buildTreeFiltered(maxDepth, filter);
                    }
                }
            }
        }
    }

    /**
     * Build a tree of FileTreeNodes starting from this node, filtering files by name.
     * This recursively adds child nodes only for files that match the filter.
     */
    public void buildTreeFiltered(int maxDepth, FilenameFilter filter) {
        if (file.isDirectory() && depth < maxDepth) {
            // Use the FilenameFilter version of listPrettyFiles
            PrettyFile[] files = file.listPrettyFiles(filter);

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    PrettyFile childFile = files[i];
                    PrettyFileTreeNode childNode = new PrettyFileTreeNode(childFile);
                    childNode.setLastChild(i == files.length - 1);
                    addChild(childNode);

                    // Only recurse if the child is a directory
                    if (childFile.isDirectory()) {
                        childNode.buildTreeFiltered(maxDepth, filter);
                    }
                }
            }
        }
    }
    /**
     * Get a safe copy of the children list.
     * This method returns an unmodifiable view of the children list to prevent
     * external code from modifying the internal state.
     *
     * @return An unmodifiable view of the children list
     */
    public List<PrettyFileTreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Get a safe copy of the file.
     * This method returns a new PrettyFile object with the same path as the original file
     * to prevent external code from modifying the internal state.
     *
     * @return A copy of the file
     */
    public PrettyFile getFile() {
        return new PrettyFile(file.getAbsolutePath());
    }
}
