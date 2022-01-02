package src;

import samples.samples;

public class App {
    public static void main(String[] args){
        Sudoku sudoku = new Sudoku();
        sudoku.setPuzzle(samples.Easy1);
        sudoku.solvePuzzle();

        
    }
}
