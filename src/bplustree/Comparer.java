package bplustree;

public class Comparer {
    
    private MetaFile metaFile;
    
    public Comparer(MetaFile metaFile) {
        this.metaFile = metaFile;
    }
    
    public int compare(String key1, String key2) {
        if (metaFile.areKeysNumbers()) {
            return compareNumbers(key1, key2);
        } else {
            return key1.compareToIgnoreCase(key2);
        }
    }
    
    private int compareNumbers(String key1, String key2) {
        int num1 = Integer.parseInt(key1);
        int num2 = Integer.parseInt(key2);
        
        return Integer.compare(num1, num2);
    }
    
}
