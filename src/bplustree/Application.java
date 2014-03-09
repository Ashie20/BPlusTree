package bplustree;

import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class Application {
    
    public static void main(String[] args) {
        sydneyIsCool();
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
    
    public static void sydneyIsCool() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!");
        boolean quit = false;
        while (!quit) {
            System.out.print("Please enter your favorite color or 'exit' to quit:  ");
            String favoriteColor = scanner.nextLine();
            favoriteColor = favoriteColor.toLowerCase();
            switch (favoriteColor) {
                case "blue":
                    System.out.println("Wow! You must really like the sky.");
                    break;
                case "red":
                    System.out.println("You must be a vampire!");
                    break;
                case "purple":
                    System.out.println("Yummy!");
                    break;
                case "orange":
                    System.out.println("Good choice!");
                    break;
                case "yellow":
                    System.out.println("You're just a little ray of sunshine!");
                    break;
                case "pink":
                    System.out.println("You must be in lurv!");
                    break;
                case "green":
                    System.out.println("Wow, isn't nature cool!");
                    break;
                case "exit":
                    quit = true;
                    break;
                default:
                    System.out.println("Try again! That color is too ugly to process.");
            }
        }
    }
}
