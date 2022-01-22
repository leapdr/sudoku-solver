# Sudoku Solver

This is a simple Java application which AIMS to solve ALL sudoku problems without bruteforcing or guessing. The project uses [Apache Maven](https://maven.apache.org/).

## Terminologies

Please refer to the following definition:

* Grid: means the puzzle itself.
* X: (row) horizontal direction of the puzzle.
* Y: (column) vertical direction of the puzzle.
* Note: a candidate number on a specific unit/cell in the puzzle.
* Cell: a single unit in the puzzle determined by x, y.
* Box: pertains to a set of cell that forms smaller squares or rectangles within the puzzle.
* Skip Counter: determined by specific row (x) or column (y), this contains all the numbers that was already filled in that row/column.
* Pencelling In: process of listing of possible notes on the grid.
* Crosshatching: also called Hidden Singles, is the process of determining which number should be filled on an empty cell based on the adjacent rows and columns of the box where the cell belongs.
* Naked Single: means that a specific cell only contains 1 note and therefore that number should be the correct number for that cell.
