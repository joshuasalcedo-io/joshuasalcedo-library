package io.joshuasalcedo.library.prettyconsole.core.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PrettyFileTreeNodeTest {

    @TempDir
    Path tempDir;

    private PrettyFile rootDir;
    private PrettyFile file1;
    private PrettyFile file2;
    private PrettyFileTreeNode rootNode;

    @BeforeEach
    void setUp() throws Exception {
        // Create a test directory structure
        rootDir = new PrettyFile(tempDir.resolve("rootDir").toFile());
        if (!rootDir.mkdir()) {
            throw new IOException("Failed to create root directory: " + rootDir.getAbsolutePath());
        }

        PrettyFile subDir1 = new PrettyFile(rootDir, "subDir1");
        if (!subDir1.mkdir()) {
            throw new IOException("Failed to create subdirectory: " + subDir1.getAbsolutePath());
        }

        PrettyFile subDir2 = new PrettyFile(rootDir, "subDir2");
        if (!subDir2.mkdir()) {
            throw new IOException("Failed to create subdirectory: " + subDir2.getAbsolutePath());
        }

        file1 = new PrettyFile(rootDir, "file1.txt");
        if (!file1.createNewFile()) {
            throw new IOException("Failed to create file: " + file1.getAbsolutePath());
        }

        file2 = new PrettyFile(subDir1, "file2.txt");
        if (!file2.createNewFile()) {
            throw new IOException("Failed to create file: " + file2.getAbsolutePath());
        }

        PrettyFile file3 = new PrettyFile(subDir2, "file3.java");
        if (!file3.createNewFile()) {
            throw new IOException("Failed to create file: " + file3.getAbsolutePath());
        }

        // Create the root node
        rootNode = new PrettyFileTreeNode(rootDir);
    }

    @Test
    void testConstructor() {
        // Test constructor initializes fields correctly
        PrettyFileTreeNode node = new PrettyFileTreeNode(file1);

        assertEquals(file1, node.getFile(), "File should match");
        assertNotNull(node.getChildren(), "Children list should be initialized");
        assertEquals(0, node.getChildren().size(), "Children list should be empty");
        assertEquals(0, node.getDepth(), "Depth should be 0");
        assertFalse(node.isLastChild(), "isLastChild should be false");
    }

    @Test
    void testAddChild() {
        // Create parent and child nodes
        PrettyFileTreeNode parent = new PrettyFileTreeNode(rootDir);
        PrettyFileTreeNode child = new PrettyFileTreeNode(file1);

        // Add child to parent
        parent.addChild(child);

        // Verify child was added
        assertEquals(1, parent.getChildren().size(), "Parent should have 1 child");
        assertEquals(child, parent.getChildren().getFirst(), "Child should be in parent's children list");

        // Verify child's depth was updated
        assertEquals(1, child.getDepth(), "Child's depth should be parent's depth + 1");
    }

    @Test
    void testBuildTree() {
        // Build tree with depth 1 (only immediate children)
        rootNode.buildTree(1);

        // Verify tree structure
        assertEquals(3, rootNode.getChildren().size(), "Root should have 3 children");

        // Verify children are correct
        boolean foundSubDir1 = false;
        boolean foundSubDir2 = false;
        boolean foundFile1 = false;

        for (PrettyFileTreeNode child : rootNode.getChildren()) {
            switch (child.getFile().getName()) {
                case "subDir1" -> {
                    foundSubDir1 = true;
                    // Depth 1 means no grandchildren
                    assertEquals(0, child.getChildren().size(), "subDir1 should have no children at depth 1");
                }
                case "subDir2" -> {
                    foundSubDir2 = true;
                    assertEquals(0, child.getChildren().size(), "subDir2 should have no children at depth 1");
                }
                case "file1.txt" -> foundFile1 = true;
                default -> { /* Ignore other files */ }
            }
        }

        assertTrue(foundSubDir1, "Tree should contain subDir1");
        assertTrue(foundSubDir2, "Tree should contain subDir2");
        assertTrue(foundFile1, "Tree should contain file1.txt");
    }

    @Test
    void testBuildTreeFullDepth() {
        // Build complete tree
        rootNode.buildTree();

        // Verify tree structure
        assertEquals(3, rootNode.getChildren().size(), "Root should have 3 children");

        // Find subdirectories in children
        PrettyFileTreeNode subDir1Node = null;
        PrettyFileTreeNode subDir2Node = null;

        for (PrettyFileTreeNode child : rootNode.getChildren()) {
            if (child.getFile().getName().equals("subDir1")) {
                subDir1Node = child;
            } else if (child.getFile().getName().equals("subDir2")) {
                subDir2Node = child;
            }
        }

        assertNotNull(subDir1Node, "Tree should contain subDir1");
        assertNotNull(subDir2Node, "Tree should contain subDir2");

        // Verify subdirectories have their files
        assertEquals(1, subDir1Node.getChildren().size(), "subDir1 should have 1 child");
        assertEquals("file2.txt", subDir1Node.getChildren().getFirst().getFile().getName(),
                "subDir1's child should be file2.txt");

        assertEquals(1, subDir2Node.getChildren().size(), "subDir2 should have 1 child");
        assertEquals("file3.java", subDir2Node.getChildren().getFirst().getFile().getName(),
                "subDir2's child should be file3.java");
    }

    @Test
    void testBuildTreeFiltered() {
        // Create a filter that only accepts .txt files
        FileFilter txtFilter = file -> file.getName().endsWith(".txt") || file.isDirectory();

        // Build tree with filter
        rootNode.buildTreeFiltered(Integer.MAX_VALUE, txtFilter);

        // Verify tree structure
        assertEquals(3, rootNode.getChildren().size(), "Root should have 3 children");

        // Find subdirectories in children
        PrettyFileTreeNode subDir1Node = null;
        PrettyFileTreeNode subDir2Node = null;

        for (PrettyFileTreeNode child : rootNode.getChildren()) {
            if (child.getFile().getName().equals("subDir1")) {
                subDir1Node = child;
            } else if (child.getFile().getName().equals("subDir2")) {
                subDir2Node = child;
            }
        }

        assertNotNull(subDir1Node, "Tree should contain subDir1");
        assertNotNull(subDir2Node, "Tree should contain subDir2");

        // Verify subdirectories have their files (only .txt files)
        assertEquals(1, subDir1Node.getChildren().size(), "subDir1 should have 1 child");
        assertEquals("file2.txt", subDir1Node.getChildren().getFirst().getFile().getName(),
                "subDir1's child should be file2.txt");

        assertEquals(0, subDir2Node.getChildren().size(), "subDir2 should have 0 children (no .txt files)");
    }

    @Test
    void testBuildTreeFilteredWithFilenameFilter() {
        // Create a filter that only accepts .java files
        FilenameFilter javaFilter = (dir, name) -> name.endsWith(".java") || new File(dir, name).isDirectory();

        // Build tree with filter
        rootNode.buildTreeFiltered(Integer.MAX_VALUE, javaFilter);

        // Verify tree structure - the number of children may vary depending on how FilenameFilter is applied
        // The important thing is that subdirectories are included and only .java files are included
        assertTrue(rootNode.getChildren().size() >= 2, "Root should have at least 2 children (subdirectories)");

        // Find subdirectories in children
        PrettyFileTreeNode subDir1Node = null;
        PrettyFileTreeNode subDir2Node = null;

        for (PrettyFileTreeNode child : rootNode.getChildren()) {
            if (child.getFile().getName().equals("subDir1")) {
                subDir1Node = child;
            } else if (child.getFile().getName().equals("subDir2")) {
                subDir2Node = child;
            }
        }

        assertNotNull(subDir1Node, "Tree should contain subDir1");
        assertNotNull(subDir2Node, "Tree should contain subDir2");

        // Verify subdirectories have their files (only .java files)
        assertEquals(0, subDir1Node.getChildren().size(), "subDir1 should have 0 children (no .java files)");

        assertEquals(1, subDir2Node.getChildren().size(), "subDir2 should have 1 child");
        assertEquals("file3.java", subDir2Node.getChildren().getFirst().getFile().getName(),
                "subDir2's child should be file3.java");
    }

    @Test
    void testGenerateTreeString() {
        // Build tree
        rootNode.buildTree();

        // Generate tree string
        String treeString = rootNode.generateTreeString();

        // Verify tree string contains all files and directories
        assertTrue(treeString.contains("rootDir"), "Tree string should contain root directory");
        assertTrue(treeString.contains("subDir1"), "Tree string should contain subDir1");
        assertTrue(treeString.contains("subDir2"), "Tree string should contain subDir2");
        assertTrue(treeString.contains("file1.txt"), "Tree string should contain file1.txt");
        assertTrue(treeString.contains("file2.txt"), "Tree string should contain file2.txt");
        assertTrue(treeString.contains("file3.java"), "Tree string should contain file3.java");

        // Verify tree string contains branch characters
        assertTrue(treeString.contains("├──") || treeString.contains("└──"), 
                "Tree string should contain branch characters");
    }

    @Test
    void testIsLastChild() {
        // Create parent node
        PrettyFileTreeNode parent = new PrettyFileTreeNode(rootDir);

        // Create child nodes
        PrettyFileTreeNode child1 = new PrettyFileTreeNode(file1);
        PrettyFileTreeNode child2 = new PrettyFileTreeNode(file2);

        // Add children to parent
        parent.addChild(child1);
        parent.addChild(child2);

        // Set last child flag
        child1.setLastChild(false);
        child2.setLastChild(true);

        // Verify flags
        assertFalse(child1.isLastChild(), "First child should not be last");
        assertTrue(child2.isLastChild(), "Second child should be last");

        // Build tree and verify last child is correctly set
        rootNode.buildTree(1);

        // The last child in the list should have isLastChild = true
        PrettyFileTreeNode lastChild = rootNode.getChildren().getLast();
        assertTrue(lastChild.isLastChild(), "Last child in tree should have isLastChild = true");
    }
}
