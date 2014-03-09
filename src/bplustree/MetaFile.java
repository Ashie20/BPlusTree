package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetaFile {
    
    private static String rootFileName;
    private static int nodeCount;
    private static int leafCount;
    public final static int FAN_OUT = 4;
    
    public static void read() {
        try {
            File file = new File("files/meta.txt");
            Scanner s = new Scanner(file);
            String contents = s.nextLine();
            s.close();
            
            String[] values = contents.split(",");
            
            rootFileName = values[0];
            if (rootFileName.equals("null")) rootFileName = null;
            
            nodeCount = Integer.parseInt(values[1]);
            leafCount = Integer.parseInt(values[2]);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void write()
    {
        try {
            String contents = String.format("%s,%d,%d", rootFileName, nodeCount, leafCount);
            FileWriter fw = new FileWriter("files/meta.txt");
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(BPlusTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void init() {
        rootFileName = null;
        nodeCount = 0;
        leafCount = 0;
        write();
    }
        
    public static void setRootNode(String filename) {
        rootFileName = filename;
    }
    
    public static String getNextLeafFilename() {
        String filename = String.format("files/leaves/leaf_%06d.txt", leafCount);
        leafCount ++;
        return filename;
    }
    
    public static String getNextNodeFilename() {
        String filename = String.format("files/nodes/node_%06d.txt", nodeCount);
        nodeCount ++;
        return filename;
    }
    
    public static String getRootFilename() {
        return rootFileName;
    }
    
    public static boolean isRootANode() {
        if (rootFileName == null) return false;
        else return rootFileName.contains("node");
    }
}
