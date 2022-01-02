package src;
import javax.security.auth.x500.X500Principal;
import samples.samples;

// package com.asb.sudokusolver;

// import android.util.Log;

/**
 * Created by aron4_000 on 9/2/2016.
 */

public class Solver2 {
    // main grid
    private int size;
    private boolean isSolved = false;
    private Cell[][] grid;

    public Solver2(Cell[][] in, int size){
        this.grid = in;
        this.size = size;
    }

    public void solve(){
        // initialize pencilling in (setting candidates)
        pencilIn();
    }

    /**
     * Fill in the grid with a specific num
     * @param n The number to be filled
     * @param x row
     * @param y column
     */
    private void fill(int n, int x, int y){
        grid[y][x].fill(n);
    }

    /**
     * The cell at x, y is filled permanently
     * Remove notes from unfilled Cells
     * @param x
     * @param y
     */
    private void claim(int x, int y){
        int b = Sudoku.getBoxOrder(x, y);
    }

    public void pencilIn(){
        int b;

        for(int y=0; y<9; y++){
            for(int x=0; x<9; x++){
                b = Sudoku.getBoxOrder(x, y);

                if( grid[y][x].is(0) ){ // check if the cell is not empty
                    Note note = new Note(this.size);

                    for(int n=1; n<10; n++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        if(!isInXYB(n, b, y, x)){
                            note.addN(n);
                        }
                    }

                    // check if note is claimable
                    if(note.getCount() == 1){
                        fill(note.getN(0), x, y);
                    }
                } else { // remove notes from x, y, b
                    
                }
            }
        }

        // log 
        System.out.println("Finished Penciling in");
    }

    public boolean isInXYB(int n, int b, int y, int x){
        // check row (i), column(j) and box(box) if it already has the number(x)
        for(int c=0; c<9; c++){
            if( this.grid[y][c].is(n) ) return true; // x coordinates
            if( this.grid[c][x].is(n) ) return true; // y coordinates
            if( this.grid[((b/3)*3)+(c/3)][((b%3)*3)+(c%3)].is(n) ) return true; // box
        }

        return false;
    }

    public Cell[][] getSolution(){
        if(!this.isSolved){
            this.solve();
        }
        return this.grid;
    }
}
