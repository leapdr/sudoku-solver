package com.asol.app;

import com.asol.samples.Sample;
import com.asol.lib.Sudoku;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Sudoku sudoku = new Sudoku();
        sudoku.setPuzzle(Sample.Hard1);
        sudoku.solvePuzzle();
    }
}
