package src;

import samples.Samples;

public class App {
    public static void main(String[] args){
        Sudoku sudoku = new Sudoku();
        sudoku.setPuzzle(Samples.Easy1);
        sudoku.solvePuzzle();

        
    }
}
