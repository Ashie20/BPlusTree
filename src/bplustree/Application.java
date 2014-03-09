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
        
        MetaFile.write();
        
        System.out.println("Done.");
    }
    
}
