// package com.asb.sudokusolver;

// import android.util.Log;

/**
 * Created by aron4_000 on 9/2/2016.
 */

public class Solver {
    private Integer[][] output;
    private Integer[][] grid = new Integer[9][9];
    private Integer[][][] gridNote = new Integer[9][9][9];
    private boolean hasSolution = true;
    private boolean canRetry = false;

    public static void main(String[] args){
        Integer[][] in = new Integer [9][9];
        in[0] = new Integer[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        in[1] = new Integer[] {0, 0, 1, 0, 0, 9, 8, 0, 0};
        in[2] = new Integer[] {9, 2, 0, 7, 0, 0, 0, 0, 0};
        in[3] = new Integer[] {0, 0, 7, 2, 0, 8, 6, 5, 0};
        in[4] = new Integer[] {2, 8, 0, 6, 0, 0, 0, 0, 4};
        in[5] = new Integer[] {0, 0, 5, 0, 0, 0, 0, 3, 0};
        in[6] = new Integer[] {7, 0, 0, 4, 0, 0, 0, 0, 0};
        in[7] = new Integer[] {0, 5, 0, 0, 0, 6, 0, 0, 0};
        in[8] = new Integer[] {4, 0, 9, 0, 5, 0, 0, 1, 0};
        Solver solver = new Solver(in);
        // Integer[][] result = solver.getOuput();
    }

    public Solver(Integer[][] in){
        grid = in;
        solve();
    }

    public void solve(){
        boolean solved = false;
        boolean canRetry = true;

        pencilIn();
        claim(10, 10, 10);
        crossHatch();
    }

    public Integer[][] getOuput() {
        return grid;
    }

    public void pencilIn(){
        int box;
        int count = 0;

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                box = ((i/3)*3) + (j/3);
                if(grid[i][j]==0){ // check if the cell is not empty
                    for(int x=1; x<10; x++){
                        // check if the box, row or column already has the number
                        // adds the number to notes if it has not
                        if(checkXYB(x, box, i, j)){
                            gridNote[i][j][count] = x;
                            count++;
                        }
                    }
                    count = 0;
                }
            }
        }

        cleanNote();
        cleanNote();
        System.out.println("Finished Penciling in");
    }

    public void crossHatch(){
        int num = 1;
        int box, cell;
        int y, x;
        int puty = 0, putx = 0;
        box = cell = 0;
        while(num < 10){
            while(box < 9){
                for(int i=0; i<3; i++){
                    y = ((box/3)*3)+i;
                    for(int j=0; j<3; j++){
                        x = ((box%3)*3)+j;
                        if(checkXYB(num, box, y, x) && grid[y][x] == 0){
                            if(!checkNote(num, box, y, x)){
                                puty = y;
                                putx = x;
                                cell++;
                            }
                        }
                    }
                }
                if(cell == 1){
                    grid[puty][putx] = num;
                    clearNote(num, puty, putx);
                    canRetry = true;
                    cleanNote();
                    claim(box, puty, putx);
                }
                cell = 0;
                box ++;
            }
            box = 0;

            if(canRetry == true){
                num = 1;
                canRetry = false;
            }
            else num ++;
        }
        System.out.println("Finished CrossHatching");
    }

    public void claim(int box, int y, int x){
        int county = 0;
        int countx = 0;
        int coory1 = 0, coorx1 = 0, coory2 = 0, coorx2 = 0;

        if(box == 10 && y == 10 && x == 10){
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(countNote(i, j) == 1){
                        grid[i][j] = gridNote[i][j][0];
                        clearNote(gridNote[i][j][0], i, j);
                    }
                }
            }

            for(int n=1; n<10; n++){
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){
                        if(searchNote(n, i, j)){countx ++; coory1=i; coorx1=j;}
                        if(searchNote(n, j, i)){county ++; coory2=j; coorx2=i;}
                    }
                    if(countx == 1){
                        grid[coory1][coorx1] = n;
                        clearNote(n, coory1, coorx1);
                        cleanNote();
                    }
                    if(county == 1){
                        grid[coory2][coorx2] = n;
                        clearNote(n, coory2, coorx2);
                        cleanNote();
                    }
                    countx = 0; county = 0;
                }
            }
        }
        else{
            int checkx = 0;
            int checky = 0;
            int checkb = 0;
            int i1 = 0, j1 = 0, i2 = 0, j2 = 0, i3 = 0, j3 = 0;

            for(int n = 1; n<10; n++){
                for(int count = 0; count<9; count++){
                    if(searchNote(n, y, count)){ checkx++; i1 = y; j1 = count;}
                    if(searchNote(n, count, x)){ checky++; i2 = count; j2 = x;}
                    if(searchNote(n, ((box/3)*3)+(count/3), ((box%3)*3)+(count%3))){
                        i3 = ((box/3)*3)+(count/3);
                        j3 = ((box%3)*3)+(count%3);
                        checkb++;
                    }
                }

                if(checkx == 1){
                    grid[i1][j1] = n;
                    clearNote(n, i1, j1);
                    cleanNote();
                }
                if(checky == 1){
                    grid[i2][j2] = n;
                    clearNote(n, i2, j2);
                    cleanNote();
                }
                if(checkb == 1){
                    grid[i3][j3] = n;
                    clearNote(n, i3, j3);
                    cleanNote();
                }

                checkx = checky = checkb = 0;
            }
        }
    }

    public void cleanNote(){
        int box;

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                box = ((i/3)*3) + (j/3);
                if(grid[i][j]==0){
                    for(int x=0; x<9; x++){
                        if(gridNote[i][j][x] != null){
                            if(gridNote[i][j][x] > 0 && gridNote[i][j][x] < 10){
                                if(checkNote(gridNote[i][j][x], box, i, j)){
                                    removeNote(gridNote[i][j][x], i, j);
                                    x=0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int countNote(int i, int j){
        int count = 0;
        for(int c=0; c<9; c++){
            if(gridNote[i][j][c] != null){
                if(gridNote[i][j][c] > 0 && gridNote[i][j][c] < 10) count++;
            }
        }
        return count;
    }

    public void clearNote(int n, int i, int j){
        int box = ((i/3)*3) + (j/3);

        for(int count=0; count<9; count++){
            if(searchNote(n, i, count)) removeNote(n, i, count);
            if(searchNote(n, count, j)) removeNote(n, count, j);
            if(searchNote(n, ((box/3)*3)+(count/3), ((box%3)*3)+(count%3)))
                removeNote(n, ((box/3)*3)+(count/3), ((box%3)*3)+(count%3)); // box
        }

        gridNote[i][j][0] = 0;
        gridNote[i][j][1] = 0;
    }


    void removeNote(int n, int y, int x){
        int step=0;
        for(int c=0; c<9; c++){
            if(gridNote[y][x][c] != null){
                if(gridNote[y][x][c] == n){
                    if(checkifnull(y, x, c+1)) {
                        if (gridNote[y][x][c + 1] > 0 && gridNote[y][x][c + 1] < 10) {
                            step = c + 1;
                            while (step != 9) {
                                if (gridNote[y][x][step] != null) {
                                    if (gridNote[y][x][step] < 10 && gridNote[y][x].length > 0) {
                                        gridNote[y][x][step - 1] = gridNote[y][x][step];
                                    }
                                }
                                step++;
                            }
                        } else gridNote[y][x][c] = 0;
                        return;
                    }
                }
            }
        }
    }

    public boolean checkXYB(int x, int box, int i, int j){
        // check row (i), column(j) and box(box) if it already has the number(x)

        System.out.println("Penciling In");
        for(int count=0; count<9; count++){
            if(grid[i][count] == x) return false; // x coordinates
            if(grid[count][j] == x) return false; // y coordinates
            if(grid[((box/3)*3)+(count/3)][((box%3)*3)+(count%3)] == x) return false; // box
        }

        return true;
    }

    public boolean checkNote(int x, int box, int i, int j){
        int bx1 = 0, bx2 = 0, by1 = 0, by2 = 0;
        boolean check = true;

        if(box%3==0){ bx1 = box + 1; bx2 = box + 2;}
        else if(box%3==1){ bx1 = box - 1; bx2 = box + 1;}
        else if(box%3==2){ bx1 = box - 1; bx2 = box - 2;}

        if(box/3==0){ by1 = box + 3; by2 = box + 6;}
        else if(box/3==1){ by1 = box - 3; by2 = box + 3;}
        else if(box/3==2){ by1 = box - 3; by2 = box - 6;}

        check = search(x, bx1, 'x', i) || search(x, bx2, 'x', i)
                || search(x, by1, 'y', j) || search(x, by2, 'y', j);

        return check;
    }

    public boolean search(int n, int b, char dir, int val){
        int x = (b%3)*3;
        int y = (b/3)*3;
        boolean c = true;
        boolean a, d;
        int val1 = 0, val2 = 0;

        if(val%3 == 0){val1 = val+1; val2 = val+2;}
        else if(val%3 == 1){val1 = val-1; val2 = val+1;}
        else if(val%3 == 2){val1 = val-1; val2 = val-2;}

        if(dir == 'x'){
            int x1, x2, x3;
            x1 = x;
            x2 = x+1;
            x3 = x+2;
            c = (searchNote(n, val, x3) || searchNote(n, val, x2) || searchNote(n, val, x1))
                    && (!searchNote(n, val1, x1) && !searchNote(n, val1, x2) && !searchNote(n, val1, x3))
                    && (!searchNote(n, val2, x1) && !searchNote(n, val2, x2) && !searchNote(n, val2, x3));
        }
        else if(dir == 'y'){
            int y1, y2, y3;
            y1 = y;
            y2 = y+1;
            y3 = y+2;
            c = (searchNote(n, y1, val) || searchNote(n, y2, val) || searchNote(n, y3, val))
                    && (!searchNote(n, y1, val1) && !searchNote(n, y2, val1) && !searchNote(n, y3, val1))
                    && (!searchNote(n, y1, val2) && !searchNote(n, y2, val2) && !searchNote(n, y3, val2));
        }

        return c;
    }

    public boolean searchNote(int x, int i, int j){
        for(int count=0; count<9; count++){
            if(gridNote[i][j][count] != null){
                if(gridNote[i][j][count] > 0 && gridNote[i][j][count] < 10 && gridNote[i][j][count] == x) return true;
                if(gridNote[i][j][count] > 9)return false;
            }
        }
        return false;
    }

    public boolean checkifnull(int x, int y, int z){
        if(gridNote[x][y][z] != null) return true;
        return false;
    }
}
