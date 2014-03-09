package bplustree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MetaFile {
    public String rootFileName;
    public int nodeCount;
    public int leafCount;
    public static int fanOut = 4;
    
    public static MetaFile getMetaFile() {
        try {
            File file = new File("files/meta.txt");
            Scanner s = new Scanner(file);
            String contents = s.nextLine();
            s.close();
            
            String[] values = contents.split(",");
            
            MetaFile result = new MetaFile();
            result.rootFileName = values[0];
            result.nodeCount = Integer.parseInt(values[1]);
            result.leafCount = Integer.parseInt(values[2]);
            
            return result;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void createMetaFile(String fileName, int nodes, int leaves)
    {
        try {
            String contents = String.format("%s,%d,%d", fileName, nodes, leaves);
            FileWriter fw = new FileWriter("files/meta.txt");
            fw.write(contents);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(BPlusTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void incrementLeafCount() {
        MetaFile m = getMetaFile();
        createMetaFile(m.rootFileName, m.nodeCount, m.leafCount + 1);
    }
    
    public static void incrementNodeCount() {
        MetaFile m = getMetaFile();
        createMetaFile(m.rootFileName, m.nodeCount + 1, m.leafCount);
    }
    
    public static void setRootNode(String filename) {
         MetaFile m = getMetaFile();
        createMetaFile(filename, m.nodeCount, m.leafCount);
    }
}
