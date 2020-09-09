package sudoku.computationlogic;

import java.util.Random;
import java.lang.*;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;
import static sudoku.problemdomain.SudokuGame.SQRT_GRID_BOUNDARY;

public class GameGenerator {

    public static int[][] getNewGameGrid() {

        return unsolveGame(getSolvedGame());
    }

    /**
     * 1. Generate a new 9x9 2D Array.
     * 2. Populate 3 diagonal 3x3 matrices - These matrices are independent of the other empty matrices
     * 3. Fill rest - Using a recursive approach. For each empty cell, try all numbers until a safe value is found
     *
     * @return newGrid
     */
    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        newGrid = fillDiagonal(newGrid);
        newGrid = fillRest(0, SQRT_GRID_BOUNDARY, newGrid);

        return newGrid;
    }

    /**
     * Remove 35 elements from the solved sudoku puzzle.
     * 1. Copy values of the solvedGame to a new array
     * 2. Using a random number generator, remove element at random coordinate
     * 3. Test the new array to be solvable
     * 4. Cycle back to step 1
     *
     * @param solvedGame
     * @return solvableArray
     */
    private static int[][] unsolveGame(int[][] solvedGame) {
        boolean solvable = false;

        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        //Take values from solvedGame and write to new unsolved; i.e. reset to initial state
        SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

        while (solvable == false){

            int k = 35;
            while (k != 0) {
                int cellId = randomGenerator(GRID_BOUNDARY*GRID_BOUNDARY);
                int xCoordinate = (cellId/GRID_BOUNDARY);
                int yCoordinate = (cellId%GRID_BOUNDARY);

                if (yCoordinate != 0) {
                    yCoordinate = yCoordinate-1;
                }
                if (solvableArray[xCoordinate][yCoordinate] != 0){
                    k--;
                    solvableArray[xCoordinate][yCoordinate] = 0;
                }
            }
            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }

        return solvableArray;
    }

    public static int[][] fillDiagonal(int[][] grid) {
        for (int i = 0; i < GRID_BOUNDARY; i = i + 3){
            fillMatrix(i, i, grid);
        }
        return grid;
    }

    public static int[][] fillMatrix(int row, int col, int[][] grid){
        int num;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                do {
                    num = randomGenerator(9);
                }
                while (!unUsedInMatrix(row, col, num, grid));

                grid[row + i][col + j] = num;
            }
        }
        return grid;
    }

    public static int[][] fillRest(int i, int j, int[][] grid)
    {

        // if column is greater than/equal to 9 and row is less than 8
        // then increment row by 1 and set column to 0
        if (j>=GRID_BOUNDARY && i<GRID_BOUNDARY-1)
        {
            i = i + 1;
            j = 0;
        }

        // if column and row exceeds 9
        // return grid and exit recursive function
        if (i>=GRID_BOUNDARY && j>=GRID_BOUNDARY){
            return grid;
        }

        // if row is less than 3 and if column is less than 3
        // then set column to 3 (to skip top left 3x3 matrix)
        if (i < SQRT_GRID_BOUNDARY)
        {
            if (j < SQRT_GRID_BOUNDARY)
                j = SQRT_GRID_BOUNDARY;
        }

        // if row is less than 6 and column is
        else if (i < GRID_BOUNDARY-SQRT_GRID_BOUNDARY)
        {
            if (j == (int) (i/SQRT_GRID_BOUNDARY)*SQRT_GRID_BOUNDARY)
                j =  j + SQRT_GRID_BOUNDARY;
        }

        // if column is 6
        // then increment row by 1 and set column to 0
        // if row is greater than or equal to 9, exit recursive loop
        else
        {
            if (j == GRID_BOUNDARY-SQRT_GRID_BOUNDARY)
            {
                i = i + 1;
                j = 0;
                if (i>=GRID_BOUNDARY)
                    return grid;
            }
        }

        for (int num = 1; num<=GRID_BOUNDARY; num++)
        {
            if (checkIfSafe(i, j, num, grid)) {
                grid[i][j] = num;
                if (fillRest(i, j+ 1, grid) != null){
                    return grid;
                }
                grid[i][j] = 0;
            }
        }
        return null;
    }

    public static boolean checkIfSafe(int i,int j, int num, int[][] grid)
    {
        return (unUsedInRow(i, num, grid) &&
                unUsedInCol(j, num, grid) &&
                unUsedInMatrix(i-i%SQRT_GRID_BOUNDARY, j-j%SQRT_GRID_BOUNDARY, num, grid));
    }

    public static boolean unUsedInRow(int i,int num, int[][] grid)
    {
        for (int j = 0; j<GRID_BOUNDARY; j++)
            if (grid[i][j] == num)
                return false;
        return true;
    }

    public static boolean unUsedInCol(int j,int num, int[][] grid)
    {
        for (int i = 0; i<GRID_BOUNDARY; i++)
            if (grid[i][j] == num)
                return false;
        return true;
    }

    // Returns false if given 3x3 block contains num
    public static boolean unUsedInMatrix(int rowStart, int colStart, int num, int[][] grid){
        for (int i = 0; i < 3; i ++){
            for (int j = 0; j < 3; j++){
                if (grid[rowStart + i][colStart + j] == num){
                    return false;
                }
            }
        }
        return true;
    }

    public static int randomGenerator (int num){
        return (int) Math.floor((Math.random()*num + 1));
    }
}
