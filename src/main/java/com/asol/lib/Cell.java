package com.asol.lib;

public class Cell {
    private int x;
    private int y;
    private int b;

    public int N;

    public boolean filled;
    private Note note;

    /**
     * Create an empty cell
     * @param x Grid row
     * @param y Grid column
     * @param b Grid small box
     */
    public Cell(int x, int y, int b){
        this(x, y, b, 0);
    }

    /**
     * Create a filled cell
     * @param x Grid row
     * @param y Grid column
     * @param b Grid small box
     * @param n Cell number to be put
     */
    public Cell(int x, int y, int b, int n){
        this.x = x;
        this.y = y;
        this.b = b;

        this.filled = n != 0;
        this.N = n;
    }

    /**
     * Add N to notes
     * @param n
     */
    public void addToNote(int n){
        this.note.addN(n);
    }

    /**
     * Remove N to notes if it's not yet filled
     * @param n
     */
    public void removeToNote(int n){
        if( !this.isFilled() ){
            this.note.removeN(n);
        }
    }

    /**
     * Finally fill a cell (Caim)
     * @param n
     */
    public void fill(int n){
        this.N = n;
        this.filled = true;
    }

    /**
     * Checks if this current cell is already filled
     * @return True|alse
     */
    public boolean isFilled(){
        return this.filled;
    }

    /**
     * Get the Cell notes
     * @return
     */
    public Note getNotes(){
        return this.note;
    }

    public int noteCount(){
        return this.note.getCount();
    }

    /**
     * Set the Cell notes
     * @param notes
     */
    public void setNotes(Note notes){
        this.note = notes;
    }

    /**
     * Get the cell's Row
     * @return Row
     */
    public int getCellRow(){
        return this.x;
    }

    /**
     * Get the cell's Col
     * @return Col
     */
    public int getCellCol(){
        return this.y;
    }

    /** 
     * Get the cell's Small Box
     * @return Box
     */
    public int getCellBox(){
        return this.b;
    }

    /**
     * Checks the value of the Cell
     * @param n The number to be compared
     * @return
     */
    public boolean is(int n){
        return this.N == n;
    }

    /**
     * Checks whether this cell has a specifc note n
     * @param n 
     * @return
     */
    public boolean has(int n){
        return this.note.hasN(n);
    }
}
