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
    
    private MetaFile metaFile;
    
    public FileUtility(MetaFile metaFile) {
        this.metaFile = metaFile;
    }
    
    // Identifiers will be n%09d, l%09d 
    public String getFilename(String identifier) {
        long num = Long.parseLong(identifier.substring(1));
        if (identifier.startsWith("n")) {
            return getNodeFilename(num);
        } else {
            return getLeafFilename(num);
        }
    }
    
    // Identifiers will be n%09d, l%09d 
    private String getDirectory(String identifier) {
        long num = Long.parseLong(identifier.substring(1));
        if (identifier.startsWith("n")) {
            return getNodeDirectory(num);
        } else {
            return getLeafDirectory(num);
        }
    }
    
    public void makeDirectory(String identifier) {
        String directory = getDirectory(identifier);
        File dir = new File(directory);
        dir.mkdirs();
    }
    
    private String getNodeFilename(long num) {
        return String.format("%s/node_%09d.txt", getNodeDirectory(num), num);
    }
       
    private String getNodeDirectory(long num) {
        long bucket = Math.round(Math.floor(num / metaFile.BUCKET_SIZE));
        return String.format("%s/nodes/%06d", metaFile.getBaseDirectory(), bucket);        
    }
    
    private String getLeafFilename(long num) {
        return String.format("%s/leaf_%09d.txt", getLeafDirectory(num), num);
    }
    
    private String getLeafDirectory(long num) {
        long bucket = Math.round(Math.floor(num / metaFile.BUCKET_SIZE));
        return String.format("%s/leaves/%06d", metaFile.getBaseDirectory(), bucket, num);
    }
    
}
