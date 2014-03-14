/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bplustree;

import java.io.File;

/**
 *
 * @author Ryan
 */
public class FileUtility {
    
    private static final int BUCKET_SIZE = 10;
    
    // Identifiers will be n%09d, l%09d 
    public static String getFilename(String identifier) {
        long num = Long.parseLong(identifier.substring(1));
        if (identifier.startsWith("n")) {
            return getNodeFilename(num);
        } else {
            return getLeafFilename(num);
        }
    }
    
    // Identifiers will be n%09d, l%09d 
    private static String getDirectory(String identifier) {
        long num = Long.parseLong(identifier.substring(1));
        if (identifier.startsWith("n")) {
            return getNodeDirectory(num);
        } else {
            return getLeafDirectory(num);
        }
    }
    
    public static void makeDirectory(String identifier) {
        String directory = getDirectory(identifier);
        File dir = new File(directory);
        dir.mkdirs();
    }
    
    private static String getNodeFilename(long num) {
        return String.format("%s/node_%09d.txt", getNodeDirectory(num), num);
    }
       
    private static String getNodeDirectory(long num) {
        long bucket = Math.round(Math.floor(num / BUCKET_SIZE));
        return String.format("files/nodes/%06d", bucket);        
    }
    
    private static String getLeafFilename(long num) {
        return String.format("%s/leaf_%09d.txt", getLeafDirectory(num), num);
    }
    
    private static String getLeafDirectory(long num) {
        long bucket = Math.round(Math.floor(num / BUCKET_SIZE));
        return String.format("files/leaves/%06d", bucket, num);
    }
    
    
}
