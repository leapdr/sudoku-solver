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

    public int getBoxOrder(int row, int col){
        return ((row/3)*3) + (col/3);
    }

    public void pencilIn(){
        int box;
        int count = 0;

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                box = getBoxOrder(i, j);
                if(grid[i][j].is(0)){ // check if the cell is not empty
                    for(int x=1; x<10; x++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        // if(!isInXYB(x, box, i, j)){
                        //     gridNote[i][j][count] = x;
                        //     count++;
                        // }
                    }
                    count = 0;
                }
            }
        }

        // log 
        System.out.println("Finished Penciling in");
    }

    public Cell[][] getSolution(){
        if(!this.isSolved){
            this.solve();
        }
        return this.grid;
    }
}
