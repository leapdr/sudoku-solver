package src;

public class Sudoku {
    private int size;
    private int[][] grid = new int[9][9];
    private Solver2 solver;

    public Sudoku(){
        this(9);
    }

    public Sudoku(int size){
        this.size = size;
    }

    public void setPuzzle(int[][] puzzle){
        this.grid = puzzle;
    }

    /**
     * Solve the sudoku puzzle
     */
    public void solvePuzzle(){

    }

    /**
     * Display the solved puzzle
     */
    public void showSolved(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(this.grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Get the sudoku size
     * @return
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Get the current puzzle grid
     * @return
     */
    public int[][] getGrid(){
        return this.grid;
    }
}
