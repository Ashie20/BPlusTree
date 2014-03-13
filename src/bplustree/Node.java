package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    
    private String identifer;
    private String[] pointers;
    private String[] keys;
    
    public Node(String identifer) {
        if (identifer == null) {
            initNewNode();
        } else {
            this.identifer = identifer;
            read();
        }
    }
    
    public Node(String[] keys, String[] pointers) {
        initNewNode();
        this.keys = keys;
        this.pointers = pointers;
        
        write();
    }
    
    public Node(Promotion p) {
        initNewNode();
        this.pointers[0] = p.leftPointer;
        this.keys[0] = p.key;
        this.pointers[1] = p.rightPointer;
        
        write();
        
        MetaFile.setRootNode(this.identifer);
    }
    
    private void initNewNode() {
        identifer = MetaFile.getNextNodeIdentifier();
        pointers = new String[MetaFile.FAN_OUT];
        keys = new String[MetaFile.FAN_OUT - 1];
    }
    
    // Todo: probably make this private
    public void write() {
        String contents = String.format("%s\n", identifer);
        for (int i = 0; i < pointers.length; i++) {
            String pointer = pointers[i] == null ? "null" : pointers[i];
            contents += pointer + ",";
        }
        contents += "\n";
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i] == null ? "null" : keys[i];
            contents += key + ",";
        }
                
        try {
            FileUtility.makeDirectory(identifer);
            FileWriter fw = new FileWriter(FileUtility.getFilename(identifer));
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void read() {
        try {
            File f = new File(FileUtility.getFilename(identifer));
            Scanner s = new Scanner(f);
            
            String storedIdentifier = s.nextLine();
            String[] pointers = s.nextLine().split(",");
            String[] keys = s.nextLine().split(",");
            
            for (int i = 0; i < pointers.length; i++) {
                pointers[i] = getNullOrVal(pointers[i]);
            }
            for (int i = 0; i < keys.length; i++) {
                keys[i] = getNullOrVal(keys[i]);
            }
            
            this.pointers = pointers;
            this.keys = keys;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insert(Promotion p, Stack<Node> traversal) {
        if (isFull()) {
            // TODO: Split and promote up the traversal?
            // Also, set new root node for the bplustree
            
            // Insert promotion into list of of keys sequentially
            // copy second half of keys/pointers into new node
            // Pull out the middle element into a new promotion pointing to the two nodes
            // if traversal is empty, we're at the root node.  promotion will make a new root
            // else, pop off the next node and call insert() on that node, passing the shortened traversal up
            
            // Check if we are the root node.  If so, promotion set make new root node
            
            
            // Implementation -----------------------
            
            // Copy into larger array in order
            String[] newKeys = new String[keys.length + 1];
            String[] newPointers = new String[pointers.length + 1];
            
            int insertAt = keys.length;
            for (int i = 0; i < keys.length; i++) {
                if (Comparer.compare(p.key, keys[i]) < 0) {
                    insertAt = i;
                    break;
                }
            }
            
            for (int i = 0; i < insertAt; i++) {
                newKeys[i] = keys[i];
                newPointers[i] = pointers[i];
            }
            newKeys[insertAt] = p.key;
            newPointers[insertAt] = p.leftPointer; // should be the same as pointers[insertAt];
            newPointers[insertAt + 1] = p.rightPointer;
            for (int i = newKeys.length - 1; i > insertAt; i--) {
                newKeys[i] = keys[i-1];
                newPointers[i+1] = pointers[i];
            }
            
            // Pull out middle element
            int midIndex = newKeys.length/2;
            String middleKey =  newKeys[midIndex];
            
            // Update current keys/pointers
            for (int i = 0; i < midIndex; i++) {
                keys[i] = newKeys[i];
                pointers[i] = newPointers[i];
            }
            pointers[midIndex] = newPointers[midIndex];
            
            for (int i = midIndex; i< keys.length; i++) {
                keys[i] = null;
                pointers[i+1] = null;
            }
            
            // New keys/pointers
            String[] newNodeKeys = new String[keys.length];
            String[] newNodePointers = new String[pointers.length];
            
            int k;
            int j = 0;
            for (k = midIndex + 1; k < newKeys.length; k++) {
                newNodeKeys[j] = newKeys[k];
                newNodePointers[j] = newPointers[k];
                j++;
            }
            newNodePointers[j] = newPointers[k]; // TODO: Does this work?
            
            
            // Create a new node
            Node newNode = new Node(newNodeKeys, newNodePointers);
            
            // Create a new promotion
            Promotion newP = new Promotion(this.identifer, middleKey, newNode.identifer);
            
            // Insert into traversal
            if (traversal.isEmpty()) {
                Node root = new Node(newP);
            } else {
                Node parent = traversal.pop();
                parent.insert(newP, traversal);
            }
            
            
        } else {
            insertLocal(p);
        }
        
        write();
    }
    
    private void insertLocal(Promotion p) {
        int insertAt = -1;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null || Comparer.compare(p.key, keys[i]) < 0) {
                insertAt = i;
                break;
            }
        }
        
        for (int i = keys.length - 1; i > insertAt; i--) {
            keys[i] = keys[i-1];
            pointers[i + 1] = pointers[i]; // TODO: check this
        }
        
        // Todo: does this even work?
        pointers[insertAt] = p.leftPointer;
        keys[insertAt] = p.key;
        pointers[insertAt + 1] = p.rightPointer;
    }
    
    private boolean isFull() {
        int capacity = keys.length;
        int size = capacity;
        for (String k : keys) {
            if (k == null) size --;
        }
        return size == capacity;
    }
    
    private String getNullOrVal(String val) {
        if (val == null || val.equals("null")) return null;
        return val;
    }
    
    public String[] getKeys() {
        return keys;
    }
    
    public String[] getPointers() {
        return pointers;
    }
}