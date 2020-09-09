# SudokuGame

This is a sudoku game java application. 

UI, and game framework are referenced from @BracketCove JavaDesktopSudoku project. 

I have refactored the computation logic to create a solved sudoku game (which will get unsolved for the user to solve).
Originally, a value in range 1 to 9 was chosen and allocated 9 times based on constraints.
I have improved the logic by first randomly assigning elements (range 1 to 9) to 3 diagonally positioned 3x3 matrices. Because these matrices do not overlap rows/cols with each other, their assigned values are independent to the empty cells.
Also implemented a recursive approach, to fill in the rest of the empty cells. 
 

References
BracketCove (2020) JavaDesktopSudoku [Source code]
https://github.com/BracketCove/JavaDesktopSudoku.

