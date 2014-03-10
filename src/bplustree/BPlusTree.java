package bplustree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BPlusTree {

    public BPlusTree() {
        
    }
    
    public void createTree() {
        File leaves = new File("files/leaves");
        leaves.mkdirs();
        
        File nodes = new File("files/nodes");
        nodes.mkdirs();        
        
        MetaFile.init();
    }
    
    public void insert(Item item) {
        
        
        Stack<Node> traversal = new Stack<>();
        if (!MetaFile.isRootANode()) {
            // Root is a leaf OR null
            Leaf l = new Leaf(MetaFile.getRootFilename());
            l.insert(item, traversal);
        } else {
            // Root is a node
            Node start = new Node(MetaFile.getRootFilename());
            searchAndInsert(item, start, traversal);
        }
        
    }
    
    // Gets called recursively
    private void searchAndInsert(Item item, Node node, Stack<Node> traversal) {
        traversal.push(node);
        
        String[] keys = node.getKeys();
        int pointerId = -1; // Starts out as the index of the last pointer, i.e. item is greater than the last key
        
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null || item.key.compareTo(keys[i]) < 0) { // Item key comes before keys[i]
                pointerId = i;
                break;
            }
        }
        
        String pointer = node.getPointers()[pointerId];
        
        if (pointer.contains("node")) {
            // Continue traversal
            Node next = new Node(pointer);
            searchAndInsert(item, next, traversal);
        } else {
            // We're at a leaf.  Insert into the leaf.
            Leaf l = new Leaf(pointer);
            l.insert(item, traversal);
        }
        
    }
    
}
