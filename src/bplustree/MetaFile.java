package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetaFile {
    
    private static String rootIdentifier;
    private static int nodeCount;
    private static int leafCount;
    public final static int FAN_OUT = 10;
    private final static String metaFileName = "files/meta.txt";
    private static boolean writeImmediately = true;
    
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
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void write()
    {
        try {
            String contents = String.format("%s,%d,%d", rootIdentifier, nodeCount, leafCount);
            FileWriter fw = new FileWriter(metaFileName);
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(BPlusTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void init() {
        rootIdentifier = null;
        nodeCount = 0;
        leafCount = 0;
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
    
    /* Set writeImmediately to false to prevent I/O on the meta file during periods
       when a large number of operations will be completed while this class is in
       memory, i.e. during an initial insert.  Be sure to call write() and se this
       back to true when you are done.
    */
    public static void setWriteMode(boolean writeImmediately) {
        MetaFile.writeImmediately = writeImmediately;
    }
    
}
