package bplustree;

import binaryReader.*;
import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class Application {
    
    public static void main(String[] args) {
        
        System.out.println("Menu:");
        System.out.println("\t1: Locations Table");
        System.out.println("\t2: Users Table");
        System.out.println("\t3: Times Table");
        System.out.println("\t4: Dates Table");
        System.out.println("\t5: Messages Table");
        System.out.println("\t6: Test Search Locations");
        
        Scanner in = new Scanner(System.in);
        int choice = Integer.parseInt(in.nextLine());
        
        switch (choice) {
            case 1:
                createLocationsTree();
                break;
            case 2: 
                createUsersTree();
                break;
            case 3: 
                createTimesTree();
                break;
            case 4: 
                createDatesTree();
                break;
            case 5:
                createMessagesTree();
                break;
            case 6: 
                testSearchLocations();
                break;
            default:
                System.out.println("Not a valid option");
                break;
        }
        
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
    
    private static void createDatesTree() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for dates structure: ");
        String basePath = in.nextLine();
                
        System.out.print("Enter base path to existing dates directory: ");
        String originalsPath = in.nextLine();
        
        System.out.print("Enter number of dates: ");
        int num = Integer.parseInt(in.nextLine());
        
        BPlusTree bpt = new BPlusTree(basePath);
        bpt.getMetaFile().setKeyType(true);
        bpt.getMetaFile().setWriteMode(false);
        
        for (int i = 0; i < num; i++) {
            String filename = String.format("%s/date_%06d.dat", originalsPath, i);
            
            if (i % 100 == 0) {
                System.out.println("Inserting " + filename);
            }
            
            DateRow row = new DateRow(filename);
            Item item = new Item(row.getMonth() + "", row.getFileName());
            bpt.insert(item);
            
        }
        
        bpt.getMetaFile().setWriteMode(true);
        bpt.getMetaFile().write();
    }
    
    private static void createUsersTree() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for users structure: ");
        String basePath = in.nextLine();
                
        System.out.print("Enter base path to existing users directory: ");
        String originalsPath = in.nextLine();
        
        System.out.print("Enter number of users: ");
        int num = Integer.parseInt(in.nextLine());
        
        BPlusTree bpt = new BPlusTree(basePath);
        bpt.getMetaFile().setKeyType(true);
        bpt.getMetaFile().setWriteMode(false);
        
        for (int i = 0; i < num; i++) {
            String filename = String.format("%s/user_%06d.dat", originalsPath, i);
            
            if (i % 100 == 0) {
                System.out.println("Inserting " + filename);
            }
            
            UserRow row = new UserRow(filename);
            Item item = new Item(row.getLocationID() + "", row.getFileName());
            bpt.insert(item);
            
        }
        
        bpt.getMetaFile().setWriteMode(true);
        bpt.getMetaFile().write();
    }
    
    private static void createLocationsTree() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for locations tree structure: ");
        String basePath = in.nextLine();
                
        System.out.print("Enter base path to existing locations directory: ");
        String originalsPath = in.nextLine();
        
        System.out.print("Enter number of locations: ");
        int num = Integer.parseInt(in.nextLine());
        
        BPlusTree bpt = new BPlusTree(basePath);
        bpt.getMetaFile().setWriteMode(false);
        
        for (int i = 0; i < num; i++) {
            String filename = String.format("%s/location_%06d.dat", originalsPath, i);
            
            if (i % 100 == 0) {
                System.out.println("Inserting " + filename);
            }
            
            LocationRow row = new LocationRow(filename);
            Item item = new Item(row.getState() + "", row.getFileName());
            bpt.insert(item);
            
        }
        
        bpt.getMetaFile().setWriteMode(true);
        bpt.getMetaFile().write();
    }
    
    private static void createMessagesTree() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for messages tree structure: ");
        String basePath = in.nextLine();
                
        System.out.print("Enter base path to existing messages directory: ");
        String originalsPath = in.nextLine();
        
        System.out.print("Enter number of messages: ");
        int num = Integer.parseInt(in.nextLine());
        
        BPlusTree bpt = new BPlusTree(basePath);
        bpt.getMetaFile().setKeyType(true);
        bpt.getMetaFile().setWriteMode(false);
        
        for (int i = 0; i < num; i++) {
            int subdir = (i / 100000 + 1) * 100000;
            String filename = String.format("%s/%06d/message_%08d.dat", originalsPath, subdir, i);
            
            if (i % 1000 == 0) {
                System.out.println("Inserting " + filename);
            }
            
            MessageRow row = new MessageRow(filename);
            Item item = new Item(row.getTimeID()+ "", row.getFileName());
            bpt.insert(item);
            
        }
        
        bpt.getMetaFile().setWriteMode(true);
        bpt.getMetaFile().write();
    }
    
    private static void testSearchLocations() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter base path for messages tree structure: ");
        String basePath = in.nextLine();
        
        BPlusTree bpt = new BPlusTree(basePath);
        
        List<Item> results = bpt.search("Nebraska");
        
        System.out.println("Results: " + results.size());
        for (Item i : results) {
            System.out.println(i.key + ": " + i.value);
        }
    }
}