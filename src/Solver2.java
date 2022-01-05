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

    // skip operation counter for row, col and b
    private List<List<Integer>> skipX = new ArrayList<List<Integer>>();
    private List<List<Integer>> skipY = new ArrayList<List<Integer>>();
    private List<List<Integer>> skipB = new ArrayList<List<Integer>>();

    public Solver2(Cell[][] in, int size){
        this.grid = in;
        this.size = size;

        // initialize skip counters
        for(int c = 0; c < this.size; c++){
            List<Integer> tmp = new ArrayList<Integer>();
            skipX.add(tmp);
            skipY.add(tmp);
            skipB.add(tmp);
        }
    }

    public void solve(){
        // initialize pencilling in (setting candidates)
        pencilIn();

        // initial iteration
        for(int y = 0; y < this.size; y++){
            for(int x = 0; x < this.size; x++){
                Cell unit = grid[y][x];
                int b = Sudoku.getBoxOrder(x, y);

                // naked single
                if(unit.getNotes().isSingle()){
                    fillCell(unit.getNotes().getN(0), x, y, b);
                }

                // cross hatching | hidden single
                if(!unit.isFilled()){
                    for(int n = 1; n <= this.size; n++){
                        if(isToBeSkipped(n, x, y, b)){
                            continue;
                        }
                        crossHatch(n, x, y, b);
                    }
                }
            }
        }
    }

    /**
     * <p>Fill in the grid with a specific num permanently.</p>
     * <p>Remove notes from unfilled aligned Cells.</p>
     * <p>Add n to skip counters.</p>
     * <p>Perform claiming.</p>
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

        // add to skip counters
        this.skipB.get(b).add(n);
        this.skipX.get(x).add(n);
        this.skipY.get(y).add(n);
    }

    /**
     * Set the whole puzzle with notes/candidates
     * @TODO test notes per cell
     */
    private void pencilIn(){
        for(int y=0; y<9; y++){
            for(int x=0; x<9; x++){
                int b = Sudoku.getBoxOrder(x, y);

                if( !grid[y][x].isFilled() ){ // check if the cell is not empty
                    Note note = new Note(this.size);

                    for(int n=1; n<=this.size; n++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        if(!isInXYB(n, x, y, b)){
                            note.addN(n);
                        }
                    }

                    // assign the notes to cell
                    grid[y][x].setNotes(note);
                } else {
                    Integer n = grid[y][x].N;

                    // add to skip counters if it's still not in it
                    if( !skipX.get(x).contains(n) ){
                        skipX.get(x).add(n);
                    }
                    if( !skipY.get(y).contains(n) ){
                        skipY.get(y).add(n);
                    }
                    if( !skipB.get(b).contains(n) ){
                        skipB.get(b).add(n);
                    }
                }
            }
        }

        // log 
        System.out.println("Finished Penciling in");
    }

    private boolean crossHatch(int n, int x, int y, int b){
        if(isInXYB(n, x, y, b)){
            return false;
        }

        // determine the other two vertical and horizontal adjacent line
        // @TODO implement with other sizes
        int xDeterminant = x % 3;
        int yDeterminant = y % 3;

        // adjacents within the box
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

        return false;
    }

    /**
     * Checks whether n is to be skipped for filling the current cell
     * @param n
     * @param x
     * @param y
     * @param b
     * @return
     */
    private boolean isToBeSkipped(int n, int x, int y, int b){
        return this.skipB.get(b).contains(n) 
            || this.skipX.get(x).contains(n)
            || this.skipY.get(y).contains(n);
    }

    /**
     * Check if n is in row, col or box. This function is used
     * in the initial pencillingIn and is different from 
     * <code>isToBeSkipped</code>.
     * @param n
     * @param x
     * @param y
     * @param b
     * @return
     */
    private boolean isInXYB(int n, int x, int y, int b){
        // check row (i), column(j) and box(box) if it already has the number(x)
        for(int c=0; c<9; c++){
            if( this.grid[y][c].is(n) ) return true; // x coordinates
            if( this.grid[c][x].is(n) ) return true; // y coordinates
            if( this.grid[Sudoku.getYFromB(b, c)][Sudoku.getXFromB(b, c)].is(n) ) return true; // box
        }

        return false;
    }

    /**
     * Checks if n is within the row x
     * @param n
     * @param x
     * @return
     */
    private boolean isInX(int n, int x){
        return this.skipX.get(x).contains(n);
    }

    /**
     * Checks if n is within the column y
     * @param n
     * @param y
     * @return
     */
    private boolean isInY(int n, int y){
        return this.skipY.get(y).contains(n);
    }

    public Cell[][] getSolution(){
        if(!this.isSolved){
            this.solve();
        }
        return this.grid;
    }
}
