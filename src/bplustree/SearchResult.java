package bplustree;

import bplustree.Leaf;

public class SearchResult {

    public Leaf leaf;
    public int firstIndex;
    public boolean success;

    public SearchResult(Leaf leaf, int firstIndex, boolean success) {
        this.leaf = leaf;
        this.firstIndex = firstIndex;
        this.success = success;
    }
}
