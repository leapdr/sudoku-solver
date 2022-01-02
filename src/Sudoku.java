package src;

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
                int b = ((x/3)*3) + (y/3);

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
    public Cell[][] getGrid(){
        return this.grid;
    }
}
