package io.joshuasalcedo.pretty.core.theme;

import io.joshuasalcedo.pretty.core.properties.RGBColor;
import lombok.Data;

import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A node in a file tree structure that works with RGB-enabled PrettyFile.
 * This class represents a single node in a file tree, containing a PrettyFile
 * and maintaining references to its child nodes. It supports full RGB color styling
 * and advanced tree visualization.
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

    // Tree branch styling
    private RGBColor branchColor = RGBColor.of(150, 150, 150).withName("branch-gray");
    private RGBColor leafBranchColor = RGBColor.of(100, 200, 100).withName("leaf-branch-green");
    private boolean useColoredBranches = true;

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

                // Transfer styling to child nodes
                childNode.setBranchColor(this.branchColor);
                childNode.setLeafBranchColor(this.leafBranchColor);
                childNode.setUseColoredBranches(this.useColoredBranches);

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

        // Choose the appropriate branch character and apply styling
        if (depth > 0) {
            String branchSymbol;
            RGBColor currentBranchColor;

            if (isLastChild) {
                branchSymbol = "└── ";
                currentBranchColor = leafBranchColor;
            } else {
                branchSymbol = "├── ";
                currentBranchColor = branchColor;
            }

            // Apply coloring to branch if enabled
            if (useColoredBranches) {
                builder.append(currentBranchColor.apply(branchSymbol));
            } else {
                builder.append(branchSymbol);
            }
        }

        // Add the file name
        builder.append(file.getFormattedName()).append("\n");

        // Recursively add children
        String childPrefix = prefix;
        if (depth > 0) {
            if (useColoredBranches) {
                childPrefix += isLastChild ? "    " : branchColor.apply("│   ");
            } else {
                childPrefix += isLastChild ? "    " : "│   ";
            }
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

                    // Transfer styling to child nodes
                    childNode.setBranchColor(this.branchColor);
                    childNode.setLeafBranchColor(this.leafBranchColor);
                    childNode.setUseColoredBranches(this.useColoredBranches);

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

                    // Transfer styling to child nodes
                    childNode.setBranchColor(this.branchColor);
                    childNode.setLeafBranchColor(this.leafBranchColor);
                    childNode.setUseColoredBranches(this.useColoredBranches);

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
     * Set the color for tree branches.
     *
     * @param color The RGB color for regular branches
     * @return This PrettyFileTreeNode for method chaining
     */
    public PrettyFileTreeNode withBranchColor(RGBColor color) {
        this.branchColor = color;
        return this;
    }

    /**
     * Set the color for leaf branches (└── symbols).
     *
     * @param color The RGB color for leaf branches
     * @return This PrettyFileTreeNode for method chaining
     */
    public PrettyFileTreeNode withLeafBranchColor(RGBColor color) {
        this.leafBranchColor = color;
        return this;
    }

    /**
     * Enable or disable colored branches.
     *
     * @param enabled Whether branches should be colored
     * @return This PrettyFileTreeNode for method chaining
     */
    public PrettyFileTreeNode withColoredBranches(boolean enabled) {
        this.useColoredBranches = enabled;
        return this;
    }

    /**
     * Apply terminal style to files in the tree.
     * This applies the given style to all file nodes in the tree.
     *
     * @param style The terminal style to apply
     * @return This PrettyFileTreeNode for method chaining
     */
    public PrettyFileTreeNode withStyle(TerminalStyle style) {
        applyStyleToTree(style);
        return this;
    }

    /**
     * Apply a terminal style recursively to all nodes in the tree.
     *
     * @param style The terminal style to apply
     */
    private void applyStyleToTree(TerminalStyle style) {
        file.withStyle(style);
        for (PrettyFileTreeNode child : children) {
            child.applyStyleToTree(style);
        }
    }

    /**
     * Apply file and directory colors to all nodes in the tree.
     *
     * @param fileColor The RGB color for files
     * @param dirColor The RGB color for directories
     * @return This PrettyFileTreeNode for method chaining
     */
    public PrettyFileTreeNode withColors(RGBColor fileColor, RGBColor dirColor) {
        applyColorsToTree(fileColor, dirColor);
        return this;
    }

    /**
     * Apply file and directory colors recursively to all nodes in the tree.
     *
     * @param fileColor The RGB color for files
     * @param dirColor The RGB color for directories
     */
    private void applyColorsToTree(RGBColor fileColor, RGBColor dirColor) {
        file.withColors(fileColor, dirColor);
        for (PrettyFileTreeNode child : children) {
            child.applyColorsToTree(fileColor, dirColor);
        }
    }

    /**
     * Print this tree to a PrettyPrintStream.
     *
     * @param out The PrettyPrintStream to print to
     */
    public void printTree(PrettyPrintStream out) {
        out.println(generateTreeString());
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

    /**
     * Demonstrates the PrettyFileTreeNode capabilities.
     */
    public static void main(String[] args) {
        // Create a PrettyFile for the current directory
        PrettyFile rootFile = new PrettyFile(System.getProperty("user.dir"))
                .withIcon()
                .withColors(
                        RGBColor.of(112, 209, 147), // Light green for files
                        RGBColor.of(79, 148, 205)   // Blue for directories
                )
                .withBold(true);

        // Create a tree node for the root file
        PrettyFileTreeNode root = new PrettyFileTreeNode(rootFile);

        // Configure tree appearance
        root.withBranchColor(RGBColor.of(180, 180, 180))
                .withLeafBranchColor(RGBColor.of(100, 180, 100))
                .withColoredBranches(true);

        // Build the tree with a depth limit of 3
        root.buildTree(3);

        // Create a PrettyPrintStream to display the tree
        PrettyPrintStream out = new PrettyPrintStream(System.out);

        // Print a header
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("=== File System Tree ===");

        // Print the tree
        root.printTree(out);

        // Reset formatting
        out.reset();

        // Example of filtered tree
        out.foreground(RGBColor.fromHex("#FFD700")).bold(true)
                .println("\n=== Java Files Only ===");

        // Create a new tree node for Java files
        PrettyFileTreeNode javaRoot = new PrettyFileTreeNode(rootFile);

        // Configure tree appearance with different colors
        javaRoot.withBranchColor(RGBColor.of(180, 180, 180))
                .withLeafBranchColor(RGBColor.of(180, 100, 100))
                .withColoredBranches(true);

        // Build a filtered tree that only includes Java files
        javaRoot.buildTreeFiltered(3, file ->
                file.isDirectory() || file.getName().toLowerCase().endsWith(".java"));

        // Print the filtered tree
        javaRoot.printTree(out);

        // Reset formatting
        out.reset();
    }
}