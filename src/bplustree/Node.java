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
public class Node {
    
    private String[] keys;
    private String[] pointers;
    
    public Node(String[] keys, String[] pointers) {
        this.keys = keys;
        this.pointers = pointers;
    }
    
    public String writeNode(String filename) {
        
        try {
            String output = "";
            for (String key : keys) {
                output += key + ",";
            }
            output += "|";
            for (String pointer : pointers) {
                output += pointer + ",";
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
    
    public String writeNewNode() {
        MetaFile m = MetaFile.getMetaFile();
        String filename = String.format("files/nodes/node_%06d.txt", m.nodeCount);
        MetaFile.incrementNodeCount();
        writeNode(filename);
        return filename;
    }
    
    public static Node readNode(String filename) {
       try {
           File f = new File(filename);
           Scanner s = new Scanner(f);
           String contents = s.nextLine();
           s.close();
           
           String[] parts = contents.split("|");
           
           String[] partKeys = parts[0].split(",");
           String[] partPointers = parts[1].split(",");
           
           String[] keys = new String[MetaFile.fanOut -1 ];
           String[] pointers = new String[MetaFile.fanOut];
           
           for (int i = 0; i< partKeys.length; i++) {
               keys[i] = partKeys[i];
           }
           for (int i = 0; i< partPointers.length; i++) {
               pointers[i] = partPointers[i];
           }
           
           Node n = new Node(keys, pointers);
           return n;
           
       } catch (Exception ex) {
           Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
    }
    
    public int getCapacity() {
        return keys.length;
    }
    
    public int getSize() {
        int count = 0;
        for (String s : keys) {
            if (!s.equals("null")) count ++;
        }
        return count;
    }
    
    public boolean isFull() {
        return getCapacity() == getSize();
    }
    
    public String[] getKeys() {
        return keys;
    }
    
    public String[] getPointers() {
        return pointers;
    }
    
}
