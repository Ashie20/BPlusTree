package bplustree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BPlusTree {

    public BPlusTree() {
        
    }
    
    public void createTree() {
        MetaFile.createMetaFile("null", 0, 0);
    }
    
    public void insert(Item item) {
        MetaFile metaFile = MetaFile.getMetaFile();
        
        // Inserting our first item into the tree
        if (metaFile.rootFileName.equals("null")) {
            insertFirstItem(item);
        } else if (metaFile.rootFileName.contains("leaf")) {
            insertIntoLeaf(item, metaFile.rootFileName);
        } else if (metaFile.rootFileName.contains("node")) {
            insertIntoNode(item);
        }
    }
    
    
    private void insertFirstItem(Item item) {
        String[] values = new String[MetaFile.fanOut];
        values[0] = item.value;
        
        Leaf l = new Leaf(values, true);
        l.writeNewLeaf();
    }
    
    private void insertIntoLeaf(Item item, String leafFileName) {
        Leaf l = Leaf.readLeaf(leafFileName);
        
        // TODO: need to sort these
        
        if (!l.isFull()) {
            l.insert(item);
            l.writeLeaf(leafFileName);
        } else {
            // it's full
            
            String[] originalValues = l.getValues();
            String[] newValues = new String[MetaFile.fanOut];
            
            int j = 0;
            for (int i = MetaFile.fanOut / 2 ; i< originalValues.length; i++) {
                newValues[j] = originalValues[i];
                originalValues[i] = "null";
                j++;
            }
            
            Leaf newLeaf = new Leaf(newValues, "null");
            newLeaf.insert(item);
            String newFileName = newLeaf.writeNewLeaf();
            
            Leaf oldLeaf = new Leaf(originalValues, newFileName);
            oldLeaf.writeLeaf(leafFileName);
            
            promote(leafFileName, item, newFileName);         
        }
    }
    
    private void insertIntoNode(Item item) {
        
    }
    
    private void promote(String fileName1, Item item, String fileName2) {
        MetaFile m = MetaFile.getMetaFile();
        if (m.rootFileName.contains("leaf")) {
            createNewNode(fileName1, item, fileName2);
        } else {
            
        }
        
    }
    
    private void createNewNode(String fileName1, Item item, String fileName2) {
        String[] keys = new String[MetaFile.fanOut - 1];
        String[] pointers = new String[MetaFile.fanOut];
        
        pointers[0] = fileName1;
        keys[0] = item.key;
        pointers[1] = fileName2;
        
        Node n = new Node(keys, pointers);
        String fileName = n.writeNewNode();
        
        Node test = Node.readNode(fileName);
        System.out.println("test");
    }
    
}
