package bplustree;

public class Comparer {
    
    public static int compare(String key1, String key2) {
        if (MetaFile.areKeysNumbers()) {
            return compareNumbers(key1, key2);
        } else {
            return key1.compareToIgnoreCase(key2);
        }
    }
    
    private static int compareNumbers(String key1, String key2) {
        int num1 = Integer.parseInt(key1);
        int num2 = Integer.parseInt(key2);
        
        return Integer.compare(num1, num2);
    }
    
}
