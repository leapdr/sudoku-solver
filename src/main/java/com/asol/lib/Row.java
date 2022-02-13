package com.asol.lib;
import java.util.*;

public class Row extends Group{
    // skip counter
    private List<Integer> counter = new ArrayList<Integer>();

    // missing counter
    private List<Integer> missing = new ArrayList<Integer>();
    private int intMissingCount = 0;

    private int bin = 0;

    public Row(int n){
        super(n);
    }

    /**
     * Filling happened.
     * @param n
     */
    public void addToCounter(int n){
        this.bin ^= n;
        this.counter.add(Integer.valueOf(n));
    }
    
    public void addToMissing(int n){
        missing.add(Integer.valueOf(n));
        intMissingCount++;
    }

    public void removeToMissing(int n){
        missing.remove(Integer.valueOf(n));
        intMissingCount--;
    }

    public int getMissingCount(){
        return intMissingCount;
    }
}
