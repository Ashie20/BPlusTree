package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Leaf {
    
    private String identifier;
    
    private String previousLeaf;
    private String nextLeaf;
    
    private Item[] items;
    
    private MetaFile metaFile;
    
    public Leaf(Item[] items, String previousLeaf, String nextLeaf, MetaFile metaFile) {
        this.metaFile = metaFile;
        
        initNewLeaf();
        this.items = items;
        this.previousLeaf = previousLeaf;
        this.nextLeaf = nextLeaf;
    }
    
    public Leaf(String identifier, MetaFile metaFile) {
        this.metaFile = metaFile;
        
        if (identifier == null) {
            initNewLeaf();
        } else {
            this.identifier = identifier;
            read();
        }
        
        
        // Todo: is this necessary
        if (metaFile.getRootIdentifier()== null) {
            metaFile.setRootNode(this.identifier);
        }
    }
    
    private void initNewLeaf() {
        previousLeaf = null;
        nextLeaf = null;
        identifier = metaFile.getNextLeafIdentifier();
        items = new Item[metaFile.FAN_OUT];
    }
    
    public Item[] getItems() {
        return items;
    }
    
    public Leaf getNext() {
        if (nextLeaf == null) {
            return null;
        }
        return new Leaf(nextLeaf, metaFile);
    }
    
    // Todo: probably make this private
    public void write() {
        String contents = String.format("%s,%s,%s\n", identifier, previousLeaf, nextLeaf);
        for (int i = 0; i < items.length; i++) {
            String key = items[i] == null ? "null" : items[i].key;
            contents += key + ",";
        }
        contents += "\n";
        for (int i = 0; i < items.length; i++) {
            String val = items[i] == null ? "null" : items[i].value;
            contents += val + ",";
        }
        
        FileUtility fu = new FileUtility(metaFile);
                
        try {
            fu.makeDirectory(identifier);
            String fileName = fu.getFilename(identifier);
            FileWriter fw = new FileWriter(fileName);
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void read() {
        FileUtility fu = new FileUtility(metaFile);
        try {
            File f = new File(fu.getFilename(identifier));
            Scanner s = new Scanner(f);
            
            String[] info = s.nextLine().split(",");
            String[] keys = s.nextLine().split(",");
            String[] values = s.nextLine().split(",");
            
            identifier = getNullOrVal(info[0]);
            previousLeaf = getNullOrVal(info[1]);
            nextLeaf = getNullOrVal(info[2]);
            
            Item[] fileItems = new Item[keys.length];
            for (int i=0; i< fileItems.length; i++) {
                if (!keys[i].equals("null")) {
                    fileItems[i] = new Item(keys[i], values[i]);
                } 
            }
            items = fileItems;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Inserts the item into this leaf.  It may require splitting and promoting
     * 
     * @param item The item to insert into the leaf
     * @param traversal A list of the nodes searched to get to this leaf.  It will
     * be an empty list if there are no parent nodes (i.e. the root is a leaf)
     */
    public void insert(Item item, Stack<Node> traversal) {
        if (isFull()) {
            // split and promote
            Leaf newLeaf = split();
            
            Comparer comparer = new Comparer(metaFile);
            
            // This is bad but is has to be here
            if (comparer.compare(item.key, newLeaf.items[0].key) < 0) {
                this.insertLocal(item);
            } else {
                newLeaf.insertLocal(item);
                newLeaf.write();
            }
            
            promote(newLeaf, traversal);
        } else {
            // Insert the item according to its key
            insertLocal(item);
        }
        
        // Write this leaf back to its file
        write();        
    }
    
    private void insertLocal(Item item) {
        Comparer comparer = new Comparer(metaFile);
        int insertAt = -1;
        for (int i = 0 ; i < items.length; i++) {
            if (items[i] == null || comparer.compare(item.key, items[i].key) < 0) {
                insertAt = i;
                break;
            }
        }
        for (int i = items.length - 1; i > insertAt; i--) {
            items[i] = items[i-1];
        }
        items[insertAt] = item;       
    }
    
    /**
     * Takes the current leaf and splits it into two, returning the new leaf.
     * It modifies the current leaf's items and next pointer, and creates a new
     * leaf with the second half of the first's items and a previous pointer
     * May also set fileName and parentFile name
     * @return a new Leaf object
     */
    private Leaf split() {
        Item[] newItems = new Item[items.length];
        int j = 0;
        for (int i = items.length / 2; i < items.length; i++) {
            newItems[j] = items[i];
            items[i] = null;
            j++;
        }
        
        Leaf newLeaf = new Leaf(newItems, this.identifier, nextLeaf, metaFile);
        newLeaf.write();
        this.nextLeaf = newLeaf.identifier;
        
        return newLeaf;
    }
    
    /**
     * Promotes this leaf and the given newLeaf, using the first key from
     * newLeaf as the key to promote.  It may have to make a new node?
     * @param newLeaf 
     */
    private void promote(Leaf newLeaf, Stack<Node> traversal) {
        Promotion p = new Promotion(this.identifier, newLeaf.items[0].key, newLeaf.identifier);
                
        Node parent;
        if (traversal.isEmpty()) {
            parent = new Node(p, metaFile);  // Creates a new node and writes to disk
        } else {
            parent = traversal.pop();
            parent.insert(p, traversal);
        }
        
    }
    
    private boolean isFull() {
        int capacity = items.length;
        int size = capacity;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) size--;
        }
        
        return size == capacity;        
    }
    
    private String getNullOrVal(String val) {
        if (val == null || val.equals("null")) return null;
        return val;
    }
    
}