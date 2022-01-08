package src;
import samples.Samples;

public class Sudoku {
    private int size;
    private Cell[][] grid;
    private Cell[][] solution;
    private Solver2 solver;

    public Sudoku(){
        this(9);
    }

    /**
     * Initialize sudoku puzzle, size, Cell
     * @param size The puzzle size
     */
    public Sudoku(int size){
        this.size = size;
        this.grid = new Cell[size][size];
    }

    public void setPuzzle(int[][] puzzle){
        // initialize sudoku puzzle with Cells (Units)
        for(int y = 0; y < this.size; y++){
            for(int x = 0; x < this.size; x++){
                // checks if Cell is already filled (given) in the puzzle
                int n = puzzle[y][x] != 0 ? puzzle[y][x] : 0;

                // get the box number of the Cell
                int b = getBoxOrder(x, y);

                grid[y][x] = new Cell(x, y, b, n);
            }
        }
    }

    /**
     * Solve the sudoku puzzle
     */
    public void solvePuzzle(){
        this.solver = new Solver2(this.grid, this.size);
        this.solver.solve();
        this.solution = solver.getSolution();
    }

    /**
     * Display the solved puzzle
     */
    public void showSolved(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(this.solution[i][j] + " ");
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
    public Cell[][] getGrid(){
        return this.grid;
    }

    /**
     * Get the smaller box order within the grid
     * @TODO implement on other sizes
     * 
     * @param x
     * @param y
     * @return
     */
    public static int getBoxOrder(int x, int y){
        return ((x/3)*3) + (y/3);
    }

    /**
     * Get the Y axis of Cell c in the box
     * @param b
     * @param c
     * @return
     */
    public static int getYFromB(int b, int c){
        return ((b/3)*3)+(c/3);
    }

    /**
     * Get the X axis of Cell c in the box
     * @param b
     * @param c
     * @return
     */
    public static int getXFromB(int b, int c){
        return ((b%3)*3)+(c%3);
    }
}
