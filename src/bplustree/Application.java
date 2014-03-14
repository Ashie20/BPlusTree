package bplustree;

import binaryReader.TimeRow;
import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class Application {
    
    public static void main(String[] args) {
        //createTimesTree();
        
        BPlusTree bpt = new BPlusTree("C:\\Users\\Ryan\\Desktop\\tree");
        bpt.getMetaFile().setKeyType(false);
                    
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
        
        //MetaFile.write();
        
        List<Item> results = bpt.search("O");
        
        System.out.println("Results:");
        for (Item i : results) {
            System.out.println(i.key + ": " + i.value);
        }
        if (results.isEmpty()) {
            System.out.println("Empty result set");
        }
        
        System.out.println("Done.");
        
    }
        
    private static void createTimesTree() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for tree structure: ");
        String basePath = in.nextLine();
                
        System.out.print("Enter base path to existing times directory: ");
        String originalsPath = in.nextLine();
        
        System.out.print("Enter number of times: ");
        int num = Integer.parseInt(in.nextLine());
        
        BPlusTree bpt = new BPlusTree(basePath);
        bpt.getMetaFile().setWriteMode(false);
        
        for (int i = 0; i < num; i++) {
            String filename = String.format("%s/time_%06d.dat", originalsPath, i);
            
            if (i % 100 == 0) {
                System.out.println("Inserting " + filename);
            }
            
            TimeRow tr = new TimeRow(filename);
            Item item = new Item(tr.getHour() + "", tr.getFileName());
            bpt.insert(item);
            
        }
        
        bpt.getMetaFile().setWriteMode(true);
        bpt.getMetaFile().write();
    }
    
}