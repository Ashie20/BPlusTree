/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bplustree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan
 */
public class Leaf {
    
    private String[] values;
    private String nextFilename;
    private boolean isRootNode;
    
    public Leaf(String[] values, String nextFilename) {
        this.values = values;
        this.nextFilename = nextFilename;
    }
    
    public Leaf(String[] values, boolean isRootNode) {
        this.values = values;
        this.isRootNode = isRootNode;
    }
    
    public String writeLeaf(String filename) {
        
        try {
            String output = "";
            for (String value : values) {
                output += value + ",";
            }   output += nextFilename;
            
            if (isRootNode) {
                MetaFile.setRootNode(filename);
            }
            
            FileWriter fw = new FileWriter(filename);
            fw.write(output);
            fw.close();
            return filename;
        } catch (IOException ex) {
            Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return "null";
    }
    
    public String writeNewLeaf() {
        MetaFile m = MetaFile.getMetaFile();
        String filename = String.format("files/leaves/leaf_%06d.txt", m.leafCount);
        MetaFile.incrementLeafCount();
        writeLeaf(filename);
        return filename;
    }
    
    public static Leaf readLeaf(String filename) {
       try {
           File f = new File(filename);
           Scanner s = new Scanner(f);
           String contents = s.nextLine();
           s.close();
           
           String beginning = contents.substring(0, contents.lastIndexOf(","));
           String nextFileName = contents.substring(contents.lastIndexOf(",") + 1);
           
           String[] values = beginning.split(",");
           Leaf l = new Leaf(values, nextFileName);
           
           return l;
           
       } catch (Exception ex) {
           Logger.getLogger(Leaf.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
    }
    
    public int getCapacity() {
        return values.length;
    }
    
    public int getSize() {
        int count = 0;
        for (String s : values) {
            if (!s.equals("null")) count ++;
        }
        return count;
    }
    
    public boolean isFull() {
        return getCapacity() == getSize();
    }
    
    public void insert(Item item) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null || values[i].equals("null")) {
                values[i] = item.value;
                return;
            }
        }
    }
    
    public String[] getValues() {
        return values;
    }
    
}
