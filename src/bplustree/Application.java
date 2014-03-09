package bplustree;

/**
 *
 * @author Ryan
 */
public class Application {
    
    public static void main(String[] args) {
        BPlusTree bpt = new BPlusTree();
        
        bpt.createTree();
        bpt.insert(new Item("A", "record_1.bat"));
        bpt.insert(new Item("B", "record_2.bat"));
        bpt.insert(new Item("C", "record_3.bat"));
        bpt.insert(new Item("D", "record_4.bat"));
        bpt.insert(new Item("E", "record_5.bat"));
        
        
        MetaFile m = MetaFile.getMetaFile();
        
        System.out.println("Done.");
    }
    
}
