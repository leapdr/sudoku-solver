package com.asol.lib;
import javax.annotation.processing.FilerException;
import javax.security.auth.x500.X500Principal;
import java.lang.Math;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

// package com.asb.sudokusolver;

// import android.util.Log;

/**
 * Second revision of the Sudoku Solver.
 * In this entire class:
 *  x: means the row on the grid
 *  y: means the column on the grid
 *  b: means the box on the grid
 *  n: means a number, can be a candidate or final value of cell
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

    private FileWriter solutionHistoryFile;

    public Solver2(Cell[][] in, int size){
        // initialize solution history file
        try{
            solutionHistoryFile = new FileWriter("solution.txt", true);
        } catch(Exception e){
            e.printStackTrace();
        }

        this.grid = in;
        this.size = size;

        // initialize skip counters
        addHistory("Initializing skip counters");
        for(int c = 0; c < this.size; c++){
            List<Integer> tmp = new ArrayList<Integer>();
            skipX.add(tmp);
            tmp = new ArrayList<Integer>();
            skipY.add(tmp);
            tmp = new ArrayList<Integer>();
            skipB.add(tmp);
        }
    }

    public void solve(){
        // initialize pencilling in (setting candidates)
        pencilIn();

        // initial iteration
        for(int x = 0; x < this.size; x++){
            // claiming
            claim(x);

            // skip whole row if all units were already filled
            if(this.skipX.get(x).size() == this.size){
                addHistory("Skip row " + x);
                continue;
            }

            for(int y = 0; y < this.size; y++){
                // skip whole col if all units were already filled
                if(this.skipY.get(y).size() == this.size){
                    addHistory("Skip column " + y);
                    continue;
                }

                // skip cell if filled
                if(grid[x][y].isFilled()){
                    continue;
                }

                Cell unit = grid[x][y];
                int b = Sudoku.getBoxOrder(x, y);

                // naked single
                if(unit.getNotes().isSingle()){
                    addHistory("Naked single found at " + x + " " + y);
                    fillCell(unit.getNotes().getN(0), x, y, b);
                }

                // cross hatching | hidden single
                if(!unit.isFilled()){
                    for(int n = 1; n <= this.size; n++){
                        if(isToBeSkipped(n, x, y, b)){
                            continue;
                        }
                        // terminate crosshatching if filled
                        if( crossHatch(n, x, y, b) ){
                            break;
                        }
                    }
                }
            }
        }

        this.isSolved = true;
    }

    /**
     * <p>Fill in the grid with a specific num permanently.</p>
     * <p>Remove notes from unfilled aligned Cells.</p>
     * <p>Add n to skip counters.</p>
     * <p>Cleanup related notes.</p>
     */
    private void fillCell(int n, int x, int y, int b){
        addHistory("Filling to " + x + " " + y + " " + b + ": " + n);
        this.grid[x][y].fill(n);

        for(int c=0; c<9; c++){
            int nb;

            // x, row
            if( !this.grid[x][c].isFilled() ){
                nb = Sudoku.getBoxOrder(x, c);
                addHistory("Removing note to " + x + " " + c + " " + nb + ": " + n);
                this.grid[x][c].removeToNote(n);
            }

            // y, col
            if( !this.grid[c][y].isFilled() ){
                nb = Sudoku.getBoxOrder(c, y);
                addHistory("Removing note to " + c + " " + y + " " + nb + ": " + n);
                this.grid[c][y].removeToNote(n);
            }

            // box
            int ny = Sudoku.getYFromB(b, c);
            int nx = Sudoku.getXFromB(b, c);
            if( !this.grid[nx][ny].isFilled() ){
                addHistory("Removing note to " + nx + " " + ny + " " + b + ": " + n);
                this.grid[nx][ny].removeToNote(n);
            }
        }

        // add to skip counters
        this.addToBeSkipped(n, x, y, b, false);
    }

    /**
     * Set the whole puzzle with notes/candidates
     * @TODO test notes per cell
     */
    private void pencilIn(){
        addHistory("Pencelling In: Started");

        for(int x=0; x<9; x++){
            for(int y=0; y<9; y++){
                int b = Sudoku.getBoxOrder(x, y);

                if( !grid[x][y].isFilled() ){ // check if the cell is not empty
                    Note note = new Note(this.size);

                    for(int n=1; n<=this.size; n++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        if(!isInXYB(n, x, y, b)){
                            addHistory("Adding note to " + x + " " + y + " " + b + ": " + n);
                            note.addN(n);
                        }
                    }

                    // assign the notes to cell
                    grid[x][y].setNotes(note);
                } else {
                    Integer n = grid[x][y].N;

                    // initial, force add to skip counters
                    this.addToBeSkipped(n, x, y, b, true);
                }
            }
        }

        addHistory("Finished Penciling in");
    }

    /**
     * Claiming
     * @param xy The x and also the y for claiming
     */
    private void claim(int xy){
        int x = xy;
        int y = xy;

        for(int n = 1; n <= this.size; n++){
            // skip if already in row & column
            boolean skipX = this.isInX(n, x);
            boolean skipY = this.isInY(n, y);

            if(skipX && skipY){
                continue;
            }

            // for naked candidates
            int countFoundX = 0;
            int countFoundY = 0;

            // these are not chromosomes!!
            int lastXX = 0;
            int lastXY = 0;
            int lastYX = 0;
            int lastYY = 0;

            ArrayList<Integer> bx = new ArrayList<>();
            ArrayList<Integer> by = new ArrayList<>();

            for(int i = 0; i < this.size; i++){
                // x claiming
                if(!skipX){
                    if( !grid[x][i].isFilled() && grid[x][i].has(n) ){
                        countFoundX++;
                        lastXX = x; lastXY = i;

                        int tmpXBox = Sudoku.getBoxOrder(x, i);
                        if( !bx.contains(tmpXBox) ){
                            bx.add(tmpXBox);
                        }
                    }
                }

                // y claiming
                if(!skipY){
                    if( !grid[i][y].isFilled() && grid[i][y].has(n) ){
                        countFoundY++;
                        lastYX = i; lastYY = y;

                        int tmpYBox = Sudoku.getBoxOrder(i, y);
                        if( !by.contains(tmpYBox) ){
                            by.add(tmpYBox);
                        }
                    }
                }
            }

            // fill if hidden single is found
            if(countFoundX == 1){
                addHistory("Hidden Single found at: " + lastXX + " " + lastXY);
                fillCell(n, lastXX, lastXY, Sudoku.getBoxOrder(lastXX, lastYY));
            }

            if(countFoundY == 1){
                addHistory("Hidden Single found at: " + lastYX + " " + lastYY);
                fillCell(n, lastYX, lastYY, Sudoku.getBoxOrder(lastYX, lastYY));
            }

            if( countFoundX == 1 || countFoundY == 1){
                continue;
            }

            // remove notes if claiming is possible
            // @TODO, skip if n is already on the other two boxes;
            if( bx.size() == 1 ){
                cleanBoxNote(bx.get(0), x, 'x', n);
            }

            if( by.size() == 1 ){
                cleanBoxNote(by.get(0), y, 'y', n);
            }
        }
    }

    private void cleanBoxNote(int b, int xy, char d, int n) {
        for(int i = 0; i < this.size; i++){
            int bx = Sudoku.getXFromB(b, i);
            int by = Sudoku.getYFromB(b, i);

            if(grid[bx][by].isFilled() || !grid[bx][by].has(n)){
                continue;
            }

            switch(d){
                case 'x':
                    if(bx != xy){
                        addHistory("Claiming: removing note to " + bx + " " + by + " " + b + ": " + n);
                        grid[bx][by].removeToNote(n);
                    }
                    break;
                case 'y':
                    if(by != xy){
                        addHistory("Claiming: removing note to " + bx + " " + by + " " + b + ": " + n);
                        grid[bx][by].removeToNote(n);
                    }
                    break;
            }
        }
    }

    /**
     * Cross Hatching | Hidden Singles
     * @return
     */
    private boolean crossHatch(int n, int x, int y, int b){
        // determines the other two vertical and horizontal adjacent line
        // @TODO implement with other sizes
        int xDeterminant = x % 3;
        int yDeterminant = y % 3;

        // x adjacents within the box
        int ax = 0, bx = 0, ay = 0, by = 0;
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
        // y adjacents within the box
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

        // only fill when other 8 cells are already filled or in skipCounters
        boolean inAX = this.isInX(n, ax);
        boolean inBX = this.isInX(n, bx);
        boolean inAY = this.isInY(n, ay);
        boolean inBY = this.isInY(n, by);

        if( (this.grid[x][ay].isFilled() || inAY )
            && (this.grid[x][by].isFilled() || inBY ) 
            && (this.grid[ax][y].isFilled() || inAX ) 
            && (this.grid[bx][y].isFilled() || inBX ) 

            && (this.grid[ax][ay].isFilled() || inAX || inAY ) 
            && (this.grid[ax][by].isFilled() || inAX || inBY )
            && (this.grid[bx][ay].isFilled() || inBX || inAY ) 
            && (this.grid[bx][by].isFilled() || inBX || inBY ) 
        ){
            addHistory("Crosshatching point found through: " + ax + " " + bx + " " + ay + " " + by);
            this.fillCell(n, x, y, b);
            return true;
        }

        return false;
    }

    /**
     * Add n to skip counters
     * @param forceAdd No checking needed
     */
    private void addToBeSkipped(int n, int x, int y, int b, boolean forceAdd){
        if(forceAdd){
            // x skip counters
            addHistory("Force adding to X skip counter " + x + ": " + n);
            this.skipX.get(x).add(n);

            // y skip counters
            addHistory("Force adding to Y skip counter " + y + ": " + n);
            this.skipY.get(y).add(n);

            // b skip counters
            addHistory("Force adding to B skip counter " + b + ": " + n);
            this.skipB.get(b).add(n);
        } else {
            if( !isInX(n, x) && this.skipX.get(x).size() < this.size - 1 ){
                addHistory("Adding to X skip counter " + x + ": " + n);
                this.skipX.get(x).add(n);

                if(this.skipX.get(x).size() == this.size - 1){
                    nakedClaimX(x);
                }
            }
            if( !isInY(n, y) && this.skipY.get(y).size() < this.size - 1){
                addHistory("Adding to Y skip counter " + y + ": " + n);
                this.skipY.get(y).add(n);

                if(this.skipX.get(x).size() == this.size - 1){
                    nakedClaimY(y);
                }
            }
            if( !skipB.get(b).contains(n) && this.skipB.get(b).size() < this.size - 1 ){
                addHistory("Adding to B skip counter " + b + ": " + n);
                this.skipB.get(b).add(n);

                if(this.skipB.get(b).size() == this.size - 1){
                    nakedClaimB(b);
                }
            }
            else{
                if(skipB.get(b).contains(n)){
                    String msg = "";
                    for(int i =0; i < skipB.get(b).size(); i++){
                        msg += " " + skipB.get(b).get(i);
                    }
                    addHistory("B contains " + n + ". Skipping b counter " + b + " : " + msg);
                } else {
                    addHistory("Skipping b counter " + b + " with " + n);
                }
            }
        }
    }

    /**
     * Find the missing number from the list
     * @param list The skip counter, can be X, Y, or B
     * @return
     */
    private int getMissingNFromNaked(List<Integer> list){
        
        int collection = list.stream().reduce(Integer.valueOf(0), (Integer a, Integer b) -> {
            return a + Integer.valueOf((int) Math.pow(2, (b-1)));
        });
        
        for(int c=1; c <= this.size ; c++){
            if( (collection & (int) Math.pow(2, c-1)) == 0){
                return c;
            }
        }

        return 0;
    }

    /**
     * Naked claiming operation for row
     */
    private void nakedClaimX(int x){
        int missingN = this.getMissingNFromNaked(this.skipX.get(x));

        // find the location of naked cell
        for(int c = 0; c < this.size; c++){
            if( !this.grid[x][c].isFilled() ){
                addHistory("Naked claim X found.");
                this.fillCell(missingN, x, c, Sudoku.getBoxOrder(x, c));
                break;
            }
        }
    }

    private void nakedClaimY(int y){
        int missingN = this.getMissingNFromNaked(this.skipY.get(y));

        // find the location of naked cell
        for(int c = 0; c < this.size; c++){
            if( !this.grid[c][y].isFilled() ){
                addHistory("Naked claim Y found.");
                this.fillCell(missingN, c, y, Sudoku.getBoxOrder(c, y));
                break;
            }
        }
    }

    private void nakedClaimB(int b){
        int missingN = this.getMissingNFromNaked(this.skipB.get(b));

        // find the location of naked cell
        for(int c = 0; c < this.size; c++){
            int x = Sudoku.getXFromB(b, c);
            int y = Sudoku.getYFromB(b, c);

            if( !this.grid[x][y].isFilled() ){
                addHistory("Naked claim B found.");
                this.fillCell(missingN, x, y, b);
                break;
            }
        }
    }

    /**
     * Checks whether n is to be skipped for filling the current cell
     * @return
     */
    private boolean isToBeSkipped(int n, int x, int y, int b){
        return this.skipB.get(b).contains(n) 
            || this.isInX(n, x)
            || this.isInY(n, y);
    }

    /**
     * Check if n is in row, col or box. This function is used
     * in the initial pencillingIn and is different from 
     * <code>isToBeSkipped</code>.
     * @return
     */
    private boolean isInXYB(int n, int x, int y, int b){
        // check row (i), column(j) and box(box) if it already has the number(x)
        for(int c=0; c<this.size; c++){
            if( this.grid[x][c].is(n) ) return true; // x coordinates
            if( this.grid[c][y].is(n) ) return true; // y coordinates
            if( this.grid[Sudoku.getXFromB(b, c)][Sudoku.getYFromB(b, c)].is(n) ) return true; // box
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

    /**
     * Checks if n is within the column y
     * @param n
     * @param b
     * @return
     */
    private boolean isInB(int n, int b){
        return this.skipB.get(b).contains(n);
    }

    public Cell[][] getSolution(boolean toPrint){
        if(!this.isSolved){
            this.solve();
        }

        if(toPrint){
            String line = "";
            for(int y = 0; y < this.size; y++){
                for(int x = 0; x < this.size; x++){
                    line = line + " " + grid[y][x].N;
                }
                addHistory(line);
                line = "";
            }
        }

        try{
            this.solutionHistoryFile.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return this.grid;
    }

    private void addHistory(String message){
        System.out.println(message);

        try{
            solutionHistoryFile.append(message + "\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
