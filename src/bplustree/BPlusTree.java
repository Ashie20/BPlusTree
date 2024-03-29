package bplustree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BPlusTree {
    
    private MetaFile metaFile;
    private FileUtility fileUtility;
    private String baseDirectory;
    private Comparer comparer;

    public BPlusTree(String baseDirectory) {
        
        this.baseDirectory = baseDirectory;
        
        metaFile = new MetaFile(baseDirectory);
        fileUtility = new FileUtility(metaFile);
        
        if (metaFile.exists()) {
            metaFile.read();
        } else {
            createTree();
        }
        
        
        comparer = new Comparer(metaFile);
    }
    
    public MetaFile getMetaFile() {
        return metaFile;
    }
    
    private void createTree() {
        File leaves = new File("leaves");
        leaves.mkdirs();
        
        File nodes = new File("nodes");
        nodes.mkdirs();        
        
        metaFile.init();
    }
    
    public void insert(Item item) {
        
        Stack<Node> traversal = new Stack<>();
        if (!metaFile.isRootANode()) {
            // Root is a leaf OR null
            Leaf l = new Leaf(metaFile.getRootIdentifier(), metaFile);
            l.insert(item, traversal);
        } else {
            // Root is a node
            Node start = new Node(metaFile.getRootIdentifier(), metaFile);
            searchAndInsert(item, start, traversal);
        }
        
    }
        
    public List<Item> search(String key) {
        if (metaFile.getRootIdentifier() == null) {
            return new ArrayList<>();
        }        
                
        SearchResult first = searchForFirst(key);
        List<Item> results = new ArrayList<>();
        
        if (!first.success || metaFile.getRootIdentifier() == null) {
            return results;
        }        
        
        // TODO: start linearly traversing the leaves starting from the first item in the Leaf
        Leaf leaf = first.leaf;
        Item[] items = leaf.getItems();
        int index = first.firstIndex;
        
        while (true) {
            // We're at a null element OR reached the end of a leaf
            // We need to check the next leaf
            if (items[index] == null || index >= items.length) {
                leaf = leaf.getNext();
                if (leaf == null) break;
                
                index = 0;
                items = leaf.getItems();
            }
            
            // We have items in this leaf.  See if we're equal.  If not, just quit!
            if (items[index].key.equals(key)) {
                results.add(items[index]);
                index++;
            } else {
                break;
            }
        }
        
        return results;
    }
    
    private SearchResult searchForFirst(String key) {
        if (!metaFile.isRootANode()) {
            // Root is a leaf OR null
            Leaf l = new Leaf(metaFile.getRootIdentifier(), metaFile);
            return searchLeaf(l, key);
        } else {
            // Root is a node
            Node start = new Node(metaFile.getRootIdentifier(), metaFile);
            return searchNodes(start, key);
        }
    }
    
    private SearchResult searchLeaf(Leaf l, String key) {
        // TODO: Use a binary search here
        
        Item[] items = l.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) break;
            if (items[i].key.equals(key)) {
                return new SearchResult(l, i, true);
            }
        }
        
        return new SearchResult(l, -1, false);
    }
    
    private SearchResult searchNodes(Node node, String key) {
        String[] keys = node.getKeys();
        int pointerId = keys.length; // Starts out as the index of the last pointer, i.e. item is greater than the last key
        
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null || comparer.compare(key, keys[i]) <= 0) { // Item key comes before keys[i]
                pointerId = i;
                break;
            }
        }
        
        String pointer = node.getPointers()[pointerId];
        
        if (pointer.startsWith("n")) {
            Node next = new Node(pointer, metaFile);
            return searchNodes(next, key);
        } else {
            Leaf l = new Leaf(pointer, metaFile);
            return searchLeaf(l, key); // Base case
        }
    }
    
    // Gets called recursively
    private void searchAndInsert(Item item, Node node, Stack<Node> traversal) {
        traversal.push(node);
        
        String[] keys = node.getKeys();
        int pointerId = keys.length; // Starts out as the index of the last pointer, i.e. item is greater than the last key
        
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null || comparer.compare(item.key, keys[i]) < 0) { // Item key comes before keys[i]
                pointerId = i;
                break;
            }
        }
        
        String pointer = node.getPointers()[pointerId];
        
        if (pointer.startsWith("n")) {
            // Continue traversal
            Node next = new Node(pointer, metaFile);
            searchAndInsert(item, next, traversal);
        } else {
            // We're at a leaf.  Insert into the leaf.
            Leaf l = new Leaf(pointer, metaFile);
            l.insert(item, traversal);
        }
        
    }    
    
}
