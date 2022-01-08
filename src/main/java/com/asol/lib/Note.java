package com.asol.lib;
import java.util.*;

/**
 * This
 */
public class Note{
    private final int MAX_NOTE;
    private List<Integer> notes = new ArrayList<Integer>();

    /**
     * Initialize Note object
     */
    public Note(int size){
        this.MAX_NOTE = size;
    }

    /**
     * Check if n is already on the notes
     * @param n
     * @return
     */
    public boolean hasN(int n){
        return notes.contains(Integer.valueOf(n));
    }

    /**
     * Remove n from the notes
     * @param n
     * @return
     */
    public boolean removeN(int n){
        return notes.remove(Integer.valueOf(n));
    }

    /**
     * Add n to the notes
     * @param n
     */
    public void addN(int n){
        notes.add(Integer.valueOf(n));

        if(notes.size() > MAX_NOTE){
            // @TODO throw an exception here
        }
    }

    /**
     * Get the note at specific index
     * @param index
     * @return
     */
    public int getN(int index){
        return notes.get(index);
    }

    /**
     * Get the current note count
     * @return
     */
    public int getCount(){
        return notes.size();
    }

    public boolean isSingle(){
        return notes.size() == 1;
    }
}
