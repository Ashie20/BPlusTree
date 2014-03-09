package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    
    private String fileName;
    private String[] pointers;
    private String[] keys;
    
    public Node(String fileName) {
        if (fileName == null) {
            initNewNode();
        } else {
            this.fileName = fileName;
            read();
        }
        
        // TODO: This should cover the first case, does it work always though?
        if (!MetaFile.isRootANode()) {
            MetaFile.setRootNode(this.fileName);
        }
    }
    
    private void initNewNode() {
        fileName = MetaFile.getNextNodeFilename();
        pointers = new String[MetaFile.FAN_OUT];
        keys = new String[MetaFile.FAN_OUT - 1];
    }
    
    // Todo: probably make this private
    public void write() {
        String contents = String.format("%s\n", fileName);
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
            
            String storedFileName = s.nextLine();
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
    
    public void insert(Promotion p) {
        if (isFull()) {
            // TODO: This sucks. Split and promote, but we have to notify the leaves...
        } else {
            insertLocal(p);
        }
        
        write();
    }
    
    private void insertLocal(Promotion p) {
        int insertAt = -1;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null || p.key.compareTo(keys[i]) < 0) {
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
}