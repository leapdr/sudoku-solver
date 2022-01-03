package src;
import javax.security.auth.x500.X500Principal;
import samples.samples;
import java.util.*;

// package com.asb.sudokusolver;

// import android.util.Log;

/**
 * Created by aron4_000 on 9/2/2016.
 */

public class Solver2 {
    // main grid
    private Cell[][] grid;

    private int size;
    private boolean isSolved = false;

    public Solver2(Cell[][] in, int size){
        this.grid = in;
        this.size = size;
    }

    public void solve(){
        // initialize pencilling in (setting candidates)
        pencilIn();

        // initial iteration
        for(int y = 0; y < this.size; y++){
            for(int x = 0; x < this.size; x++){
                // cross hatching
                crossHatch(x, y);
            }
        }
    }

    /**
     * Fill in the grid with a specific num permanently
     * Remove notes from unfilled aligned Cells
     * @param n The number to be filled
     * @param x row
     * @param y column
     */
    private void fillCell(int n, int x, int y, int b){
        grid[y][x].fill(n);

        for(int c=0; c<9; c++){
            // x, row
            this.grid[y][c].removeToNote(n);

            // y, col
            this.grid[c][x].removeToNote(n);

            // box
            this.grid[Sudoku.getYFromB(b, c)][Sudoku.getXFromB(b, c)].removeToNote(n);
        }
    }

    /**
     * Set the whole puzzle with notes/candidates
     * @TODO test notes per cell
     */
    private void pencilIn(){
        int b;

        for(int y=0; y<9; y++){
            for(int x=0; x<9; x++){
                b = Sudoku.getBoxOrder(x, y);

                if( !grid[y][x].isFilled() ){ // check if the cell is not empty
                    Note note = new Note(this.size);

                    for(int n=1; n<=this.size; n++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        if(!isInXYB(n, x, y, b)){
                            note.addN(n);
                        }
                    }

                    // check if note is claimable
                    if(note.isSingle()){
                        fillCell(note.getN(0), x, y, b);
                    }

                    // assign the notes to cell
                    grid[y][x].setNotes(note);
                } 
            }
        }

        // log 
        System.out.println("Finished Penciling in");
    }

    private void crossHatch(int x, int y){
        // determine the other two vertical and horizontal adjacent line
        // @TODO implement with other sizes
        int xDeterminant = x % 3;
        int yDeterminant = y % 3;

        // adjacents
        int ax, bx, ay, by = 0;
        switch(xDeterminant){
            case 0:
                ax = x+1;
                bx = x+2;
                break;
            case 1:
                ax = x-1;
                bx = x+1;
                break;
            case 2:
                ax = x-2;
                bx = x-1;
                break;
            default:
                // raise exception here
                break;
        }
        switch(yDeterminant){
            case 0:
                ay = y+1;
                by = y+2;
                break;
            case 1:
                ay = y-1;
                by = y+1;
                break;
            case 2:
                ay = y-2;
                by = y-1;
                break;
            default:
                // raise exception here
                break;
        }

        
    }

    public boolean isInXYB(int n, int x, int y, int b){
        // check row (i), column(j) and box(box) if it already has the number(x)
        for(int c=0; c<9; c++){
            if( this.grid[y][c].is(n) ) return true; // x coordinates
            if( this.grid[c][x].is(n) ) return true; // y coordinates
            if( this.grid[Sudoku.getYFromB(b, c)][Sudoku.getXFromB(b, c)].is(n) ) return true; // box
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
