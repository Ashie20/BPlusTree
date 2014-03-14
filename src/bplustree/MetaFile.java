package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetaFile {
    
    // Things to store
    private String rootIdentifier = null;
    private int nodeCount = 0;
    private int leafCount = 0;
    private boolean keysAreNumbers = true;
    public int FAN_OUT = 200;
    public int BUCKET_SIZE = 500;    
    
    // Things to not store (stay in memory only)
    private boolean writeImmediately = true;
    private String metaFileName;
    private String baseDirectory;
    
    public MetaFile(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.metaFileName = baseDirectory + "/meta.txt";
    }
    
    public boolean exists() {
        File file = new File(metaFileName);
        return file.exists() && file.canRead();
    }
    
    public void read() {
        try {
            File file = new File(metaFileName);
            Scanner s = new Scanner(file);
            String contents = s.nextLine();
            s.close();
            
            String[] values = contents.split(",");
            
            rootIdentifier = values[0];
            if (rootIdentifier.equals("null")) rootIdentifier = null;
            
            nodeCount = Integer.parseInt(values[1]);
            leafCount = Integer.parseInt(values[2]);
            BUCKET_SIZE = Integer.parseInt(values[3]);
            FAN_OUT = Integer.parseInt(values[4]);
            keysAreNumbers = Boolean.parseBoolean(values[5]);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void write()
    {
        try {
            String contents = String.format("%s,%d,%d,%d,%d,%s", rootIdentifier, nodeCount, leafCount, BUCKET_SIZE, FAN_OUT, keysAreNumbers ? "true" : "false");
            FileWriter fw = new FileWriter(metaFileName);
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(BPlusTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void init() {
        write();
    }
        
    public void setRootNode(String filename) {
        rootIdentifier = filename;
        
        if (writeImmediately) write();
    }
    
    public String getNextLeafIdentifier() {
        String filename = String.format("l%09d", leafCount);
        leafCount ++;
                
        if (writeImmediately) write();
        
        return filename;
    }
    
    public String getNextNodeIdentifier() {
        String filename = String.format("n%09d", nodeCount);
        nodeCount ++;
        
        if (writeImmediately) write();
        
        return filename;
    }
    
    public String getRootIdentifier() {
        return rootIdentifier;
    }
    
    public boolean isRootANode() {
        if (rootIdentifier == null) return false;
        else return rootIdentifier.contains("n");
    }
    
    public boolean areKeysNumbers() {
        return keysAreNumbers;
    }
    
    public void setKeyType(boolean keysAreNumbers) {
        this.keysAreNumbers = keysAreNumbers;
    }
    
    /* Set writeImmediately to false to prevent I/O on the meta file during periods
       when a large number of operations will be completed while this class is in
       memory, i.e. during an initial insert.  Be sure to call write() and se this
       back to true when you are done.
    */
    public void setWriteMode(boolean writeImmediately) {
        this.writeImmediately = writeImmediately;
    }
    
    public String getBaseDirectory() {
        return baseDirectory;
    }
}
