package src;

public class Cell {
    private int x;
    private int y;
    private int b;

    public int N;

    public boolean filled = false;
    public Note note;

    /**
     * Create an empty cell
     * @param x Grid row
     * @param y Grid column
     * @param b Grid small box
     */
    public Cell(int x, int y, int b){
        this(x, y, b, 0);
    }

    /**
     * Create a filled cell
     * @param x Grid row
     * @param y Grid column
     * @param b Grid small box
     * @param n Cell number to be put
     */
    public Cell(int x, int y, int b, int n){
        this.x = x;
        this.y = y;
        this.b = b;

        filled = true;
        this.N = n;
    }

    /**
     * Checks if this current cell is already filled
     * @return True|alse
     */
    public boolean isFilled(){
        return this.filled;
    }

    /**
     * Get the cell's Row
     * @return Row
     */
    public int getCellRow(){
        return this.x;
    }

    /**
     * Get the cell's Col
     * @return Col
     */
    public int getCellCol(){
        return this.y;
    }

    /** 
     * Get the cell's Small Box
     * @return Box
     */
    public int getCellBox(){
        return this.b;
    }
}
