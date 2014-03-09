package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Leaf {
    
    private String fileName;
    private String parentFileName;
    
    private String previousLeaf;
    private String nextLeaf;
    
    private Item[] items;
    
    public Leaf(Item[] items, String previousLeaf, String nextLeaf, String parentFileName) {
        initNewLeaf();
        this.items = items;
        this.previousLeaf = previousLeaf;
        this.nextLeaf = nextLeaf;
        this.parentFileName = parentFileName;
    }
    
    public Leaf(String fileName) {
        if (fileName == null) {
            initNewLeaf();
        } else {
            this.fileName = fileName;
            read();
        }
        
        if (MetaFile.getRootFilename() == null) {
            MetaFile.setRootNode(this.fileName);
        }
    }
    
    private void initNewLeaf() {
        previousLeaf = null;
        nextLeaf = null;
        fileName = MetaFile.getNextLeafFilename();
        parentFileName = null;
        items = new Item[MetaFile.FAN_OUT];
    }
    
    // Todo: probably make this private
    public void write() {
        String contents = String.format("%s,%s,%s,%s\n", fileName, parentFileName, previousLeaf, nextLeaf);
        for (int i = 0; i < items.length; i++) {
            String key = items[i] == null ? "null" : items[i].key;
            contents += key + ",";
        }
        contents += "\n";
        for (int i = 0; i < items.length; i++) {
            String val = items[i] == null ? "null" : items[i].value;
            contents += val + ",";
        }
                
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void read() {
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            
            String[] info = s.nextLine().split(",");
            String[] keys = s.nextLine().split(",");
            String[] values = s.nextLine().split(",");
            
            fileName = getNullOrVal(info[0]);
            parentFileName = getNullOrVal(info[1]);
            previousLeaf = getNullOrVal(info[2]);
            nextLeaf = getNullOrVal(info[3]);
            
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
    
    public void insert(Item item) {
        if (isFull()) {
            // split and promote
            Leaf newLeaf = split();
            promote(newLeaf);
        } else {
            // Insert the item according to its key
            insertLocal(item);
        }
        
        // Write this leaf back to its file
        write();
    }
    
    private void insertLocal(Item item) {
        int insertAt = -1;
        for (int i = 0 ; i < items.length; i++) {
            if (items[i] == null || item.key.compareTo(items[i].key) < 0) {
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
        
        // TODO: is copying up the parentFileName right?
        Leaf newLeaf = new Leaf(newItems, this.fileName, nextLeaf, parentFileName);
        newLeaf.write();
        this.nextLeaf = newLeaf.fileName;
        
        return newLeaf;
    }
    
    /**
     * Promotes this leaf and the given newLeaf, using the first key from
     * newLeaf as the key to promote.  It may have to make a new node?
     * @param newLeaf 
     */
    private void promote(Leaf newLeaf) {
        // TODO: we may have to deal with newLeaf's parent file name here
        Promotion p = new Promotion(this.fileName, newLeaf.items[0].key, newLeaf.fileName);
                
        // TODO: Crap.  We also may have to have parent.insert return new parent file names if they change
        Node parent = new Node(this.parentFileName);
        parent.insert(p);
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