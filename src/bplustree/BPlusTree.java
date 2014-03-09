package bplustree;

import java.io.File;

public class BPlusTree {

    public BPlusTree() {
        
    }
    
    public void createTree() {
        File leaves = new File("files/leaves");
        leaves.mkdirs();
        
        File nodes = new File("files/nodes");
        nodes.mkdirs();        
        
        MetaFile.init();
    }
    
    public void insert(Item item) {
        
        if (!MetaFile.isRootANode()) {
            // Root is a leaf OR null
            Leaf l = new Leaf(MetaFile.getRootFilename());
            l.insert(item);
        } else {
            // Root is a node
            // Todo: search nodes recursively to find leaf to insert into    
            System.out.println("Root is a node, can't insert");
        }
        
    }
    
}
