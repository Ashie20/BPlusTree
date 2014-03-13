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
    private static String rootIdentifier = null;
    private static int nodeCount = 0;
    private static int leafCount = 0;
    private static boolean keysAreNumbers = true;
    public static int FAN_OUT = 10;
    
    // Things to not store (stay in memory only)
    private static boolean writeImmediately = true;
    private final static String metaFileName = "files/meta.txt";
    
    
    
    public static boolean exists() {
        File file = new File(metaFileName);
        return file.exists() && file.canRead();
    }
    
    public static void read() {
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
            FAN_OUT = Integer.parseInt(values[3]);
            keysAreNumbers = Boolean.parseBoolean(values[4]);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void write()
    {
        try {
            String contents = String.format("%s,%d,%d,%d,%s", rootIdentifier, nodeCount, leafCount, FAN_OUT, keysAreNumbers ? "true" : "false");
            FileWriter fw = new FileWriter(metaFileName);
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(BPlusTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void init() {
        write();
    }
        
    public static void setRootNode(String filename) {
        rootIdentifier = filename;
        
        if (writeImmediately) write();
    }
    
    public static String getNextLeafIdentifier() {
        String filename = String.format("l%09d", leafCount);
        leafCount ++;
                
        if (writeImmediately) write();
        
        return filename;
    }
    
    public static String getNextNodeIdentifier() {
        String filename = String.format("n%09d", nodeCount);
        nodeCount ++;
        
        if (writeImmediately) write();
        
        return filename;
    }
    
    public static String getRootIdentifier() {
        return rootIdentifier;
    }
    
    public static boolean isRootANode() {
        if (rootIdentifier == null) return false;
        else return rootIdentifier.contains("n");
    }
    
    public static boolean areKeysNumbers() {
        return keysAreNumbers;
    }
    
    public static void setKeyType(boolean keysAreNumbers) {
        MetaFile.keysAreNumbers = keysAreNumbers;
    }
    
    /* Set writeImmediately to false to prevent I/O on the meta file during periods
       when a large number of operations will be completed while this class is in
       memory, i.e. during an initial insert.  Be sure to call write() and se this
       back to true when you are done.
    */
    public static void setWriteMode(boolean writeImmediately) {
        MetaFile.writeImmediately = writeImmediately;
    }
    
}
