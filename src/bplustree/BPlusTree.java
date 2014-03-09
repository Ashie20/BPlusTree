package bplustree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        
        if (!MetaFile.isRootANode()) {
            // Root is a leaf OR null
            List<Node> traversal = new ArrayList<Node>();
            
            Leaf l = new Leaf(MetaFile.getRootFilename());
            l.insert(item, traversal);
        } else {
            // Root is a node
            
            Node start = new Node(MetaFile.getRootFilename());
            List<Node> traversal = new ArrayList<Node>();
            
            searchAndInsert(item, start, traversal);
            
        }
        
    }
    
    private void searchAndInsert(Item item, Node node, List<Node> traversal) {
        traversal.add(node);
        
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
