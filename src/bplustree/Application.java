package bplustree;

import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class Application {
    
    public static void main(String[] args) {
        BPlusTree bpt = new BPlusTree();
        
        bpt.createTree();
        bpt.insert(new Item("A", "record_1.bat"));        
        bpt.insert(new Item("C", "record_2.bat"));
        bpt.insert(new Item("B", "record_3.bat"));
        bpt.insert(new Item("D", "record_4.bat"));
        bpt.insert(new Item("E", "record_5.bat"));
        bpt.insert(new Item("F", "record_6.bat"));
        bpt.insert(new Item("G", "record_7.bat"));
        bpt.insert(new Item("H", "record_8.bat"));
        bpt.insert(new Item("I", "record_9.bat"));
        bpt.insert(new Item("J", "record_10.bat"));
        bpt.insert(new Item("K", "record_11.bat"));
        bpt.insert(new Item("L", "record_12.bat"));
        bpt.insert(new Item("M", "record_13.bat"));
        bpt.insert(new Item("N", "record_14.bat"));
        bpt.insert(new Item("O", "record_15.bat"));
        bpt.insert(new Item("P", "record_16.bat"));
        
        MetaFile.write();
        
        System.out.println("Done.");
    }
    
}