package bplustree;

public class Promotion {
    public String leftPointer;
    public String rightPointer;
    public String key;
    
    public Promotion(String leftPointer, String key, String rightPointer) {
        this.leftPointer = leftPointer;
        this.key = key;
        this.rightPointer = rightPointer;
    }
}
