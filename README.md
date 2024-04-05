**Minesweeper Solver (Java)**

**Overview:**

Minesweeper Solver is a Java program that automates Minesweeper gameplay by employing advanced logic and algorithms. It strategically determines the best moves based on the current Minesweeper board and mine count, providing users with an efficient solution to Minesweeper puzzles.

**Features:**

* Automated solving of Minesweeper puzzles using advanced logic and algorithms.
* Integration of Minesweeper game within the program for seamless automation.
* Three-step solving process: statistical analysis, clump statistics derivation, and move determination.
* Iterative solving until game completion, either through victory or exhaustive possibilities.
* Utilization of basic Java for algorithm implementation.

**Usage:**

Upon starting the game, the user will be prompted to enter the board size by specifying the number of rows and columns, as well as the desired number of mines for the board. If desired, modifications to the mechanics of the solver can be made in MinesweeperGame.java. Within the play method, you can switch to allow for user inputs and computer recommendations instead. Additionally, in the botPlays method, you can modify the delay between the solver's moves by adjusting or removing the thread sleep value.
