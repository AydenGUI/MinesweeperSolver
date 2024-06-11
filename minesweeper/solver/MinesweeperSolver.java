package minesweeper.solver;

import minesweeper.game.MinesweeperGame;
import SquareStat;
import Cord;
import Clump;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class MinesweeperSolver {
    SquareStat[][] squareStats;
    int numMines, numEmpty;
    MinesweeperGame minesweeperGame;
    List moves;

    public MinesweeperSolver(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
        this.numMines = minesweeperGame.getBombs();
        this.numEmpty = minesweeperGame.getCols() * minesweeperGame.getRows();
        this.squareStats = new SquareStat[minesweeperGame.getRows()][minesweeperGame.getCols()];
        this.moves = new ArrayList<String>();
    }

    public String getMoves() {
        if (this.numEmpty - 1 > minesweeperGame.getRemaining()) {
            // reevaluates when zeros clear
            this.moves.clear();
        }
        if (this.moves.size() == 0) {
            this.evaluate();
        }
        if (this.moves.size() == 0) {
            // System.out.println("CREATE CLUMPS:");
            this.createClumps();
            // System.out.println("CLUMP MOVE:");
            this.evaluateClumps();
        }
        if (this.moves.size() == 0) {
            // System.out.println("RANDOM MOVE:");
            this.bestRandom();
        }

        String temp = "" + moves.get(0);
        moves.remove(0);
        return temp;
    }

    /*
     * Performs logic and moves for "completed" squares.
     * For all tiles touching a revealed and noncompleted numbered tile it performs
     * the following: if the remaining # of mines = remaining # of untouched tiles
     * marks all as mines, if remaining mines = 0 it reveals all remaining tiles
     */
    public void evaluate() {
        String[][] board = minesweeperGame.getDisplay();
        this.numMines = minesweeperGame.getBombs();
        this.numEmpty = minesweeperGame.getRemaining();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // if square is a number tile
                if (board[i][j] != null && !board[i][j].equals("f")) {
                    this.squareStats[i][j] = makeSquareStat(i, j);
                    if (squareStats[i][j].getDoConsider()) {
                        // if all remaining squares are mines
                        if (this.squareStats[i][j].getMines() == this.squareStats[i][j].getSquaresLeft()) {
                            // mark all as mines
                            for (int k = 0; k < this.squareStats[i][j].getUntouched().size(); k++)
                                this.moves.add("m " + this.squareStats[i][j].getUntouched().get(k));
                            if (moves.size() != 0)
                                return;
                        }
                        // if no mines remain
                        if (this.squareStats[i][j].getMines() == 0) {
                            for (int l = 0; l < this.squareStats[i][j].getUntouched().size(); l++)
                                this.moves.add("r " + this.squareStats[i][j].getUntouched().get(l));
                            if (moves.size() != 0)
                                return;
                        }

                    }
                } // outmost if
            } // inner for
        } // for
    } // evaluate

    /*
     * Creates clump statistics for each tile and tries to determine possible moves
     */
    public void evaluateClumps() {
        for (int i = 0; i < squareStats.length; i++) {
            for (SquareStat stat : squareStats[i]) {
                if (stat != null && stat.getDoConsider()) {
                    stat.statClumps();
                    stat.createNewClumps(this.moves);
                    stat.evaluateMyClumps(this.moves);
                }
            }
        }
    } // evaluateClumps

    /*
     * Creates a list of all null tiles. First it checks if any squareStats have any
     * moves with better odds than selecting randomly from that list. It iterates
     * over every square tile recording the best random move so far and the odds of it
     * hitting a mine. At the end, if these odds beat the old best, it becomes the
     * new best random move. If the best evaluated random beats randomly selecting 
     * from the list it does that move, else it chooses randomly.
     */
    public void bestRandom() {
        String[][] board = minesweeperGame.getDisplay();
        double minPercent = 1.0;
        String move = "";
        List<Cord> randCords = new ArrayList<>();
        Set<Cord> badGuess = new HashSet<>(); // Used to remove/prevent duplicates
        // Only issue is if badGuess is added after move is set it won't catch it
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null)
                    randCords.add(new Cord(i, j));
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // checks if is a numbered tile
                if (board[i][j] != null && !board[i][j].equals("f") && !board[i][j].equals("?")) {
                    for (int k = -1; k < 2; k++)
                        if (i + k >= 0 && i + k < board.length)
                            for (int l = -1; l < 2; l++)
                                if (j + l >= 0 && j + l < board[0].length)
                                    randCords.remove(new Cord(i + k, j + l));
                    // if the Cord doesn't exist, nothing happens

                    // Here determines best chance random tile to choose
                    if (this.squareStats[i][j] != null) {
                        if ((double) this.squareStats[i][j].getMines()
                                / this.squareStats[i][j].getSquaresLeft() >= minPercent) {
                            // is
                            badGuess.addAll(this.squareStats[i][j].getUntouched());
                        } else
                            minPercent = (double) this.squareStats[i][j].getMines()
                                    / this.squareStats[i][j].getSquaresLeft();
                    } // if
                } // if
            }
        } // for
          // to make sure to not miss any
        minPercent = 1.0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // checks if is a numbered tile
                if (board[i][j] != null && !board[i][j].equals("f") && !board[i][j].equals("?")) {
                    if (this.squareStats[i][j] != null) {
                        if ((double) this.squareStats[i][j].getMines()
                                / this.squareStats[i][j].getSquaresLeft() <= minPercent) {
                            Cord untouched = this.squareStats[i][j].getUntouched()
                                    .get((int) (Math.random() * this.squareStats[i][j].getSquaresLeft()));
                            List<Cord> listOfUntouched = this.squareStats[i][j].getUntouched();
                            listOfUntouched.remove(untouched);
                            int x = 0;
                            // ensures guess is not already considered "bad"
                            while ((badGuess.contains(untouched) && listOfUntouched.size() > 0)) {
                                // System.out.println("Loop with: " + untouched.toString());
                                if (badGuess.contains(untouched)) {
                                    untouched = this.squareStats[i][j].getUntouched()
                                            .get((int) (Math.random() * listOfUntouched.size()));
                                    listOfUntouched.remove(untouched);
                                }
                            } // while
                            if (badGuess.contains(untouched))
                                continue;
                            move = "?r " + untouched.toString();
                            minPercent = (double) this.squareStats[i][j].getMines()
                                    / this.squareStats[i][j].getSquaresLeft();
                        } else
                            badGuess.addAll(this.squareStats[i][j].getUntouched());
                    }
                }
            }
        }
        // System.out.println(randCords);
        // System.out.println(badGuess);

        // if the odds of a nonrevealed square being a mine is less than any other known
        // square, pick a random one
        if ((double) this.minesweeperGame.getBombs() / this.minesweeperGame.getRemaining() < minPercent
                && randCords.size() > 0) {
            // System.out.println("Why R: randBombChance= " +
            // (double)this.minesweeperGame.getBombs()/this.minesweeperGame.getRemaining() +
            // "minFoundPercent= " + minPercent);
            int rand = (int) (Math.random() * randCords.size());
            move = "?r " + randCords.get(rand).toString();
            // System.out.println("RMove: " + move);
        }

        this.moves.add(move);
        // } // if
    }

    /*
     * Generates clumps everywhere that shares more than one unmarked tile between
     * two numbered tiles. This includes directly neighboring number tiles and
     * those with a gap between them.
     */
    public void createClumps() {
        int untouchedOverlap;
        for (int i = 0; i < this.squareStats.length; i++) {
            for (int j = 0; j < this.squareStats[0].length; j++) { // SquareStat stat : squareStats[i]) {
                SquareStat stat = this.squareStats[i][j];
                if (stat != null && stat.getDoConsider()) {
                    stat.clearClumps();
                    List<Cord> untouchedCords1 = stat.getUntouched();
                    for (Cord neighbor : stat.getNeighbors()) {
                        // So I have a square stat and im iterating through their neighboring numbered
                        // tiles
                        untouchedOverlap = 0;
                        List<Cord> untouchedCords2 = this.squareStats[neighbor.getRow()][neighbor.getCol()]
                                .getUntouched();
                        Clump clump = new Clump(stat, this.squareStats[neighbor.getRow()][neighbor.getCol()]);
                        for (Cord cord : untouchedCords1)
                            for (Cord cord2 : untouchedCords2) {
                                if (cord.equals(cord2)) {
                                    untouchedOverlap++;
                                    clump.addToClump(cord);
                                }
                            }
                        if (untouchedOverlap > 1)
                            stat.addClump(clump);
                        // now each stat should be populated with neighboring clumps of > 1

                        // Checks how many untouched overlap
                        // how many more nonoverlap each have and how many remaining mines.
                        // this results in clump having at least mines - nonoverlap = number of mines

                    } // for

                    // Below does the same for "farNeighbors" with a gap between the current stat
                    // and itself;
                    for (int k = -2; k < 3; k++)
                        if (i + k >= 0 && i + k < squareStats.length)
                            for (int l = -2; l < 3; l++) {
                                if (Math.abs(k) + Math.abs(l) < 2 || j + l < 0 || j + l >= squareStats[0].length)
                                    continue;
                                SquareStat farNeighbor = this.squareStats[i + k][j + l];
                                if (farNeighbor != null && farNeighbor.getDoConsider()) {
                                    untouchedOverlap = 0;
                                    List<Cord> farUntouchedCords = farNeighbor.getUntouched();
                                    Clump clump = new Clump(stat, farNeighbor);
                                    for (Cord cord : untouchedCords1)
                                        for (Cord cord2 : farUntouchedCords) {
                                            if (cord.equals(cord2)) {
                                                untouchedOverlap++;
                                                clump.addToClump(cord);
                                            }
                                        }
                                    if (untouchedOverlap > 0)
                                        stat.addClump(clump);
                                }

                            }
                } // if
            } // for
        }
    } // createClumps

    public void clumpToClumpCompare() {

    }

    /*
     * Creates required statistics for squares and relationships between them.
     * 
     * @param row Row index
     * 
     * @param col Column index
     * 
     * @param display Display board
     */
    public SquareStat makeSquareStat(int row, int col) { // , String[][] display){
        String[][] display = minesweeperGame.getDisplay();
        int mineCount = Integer.valueOf(display[row][col]);
        int squaresLeft = 0;
        List cords = new ArrayList<String>();
        List<Cord> numberedNeighbors = new ArrayList<Cord>();
        // checks all surrounding indecies, if square is marked as a bomb mineCount--
        // if square is null (untouched), squaresLeft ++;
        // if square is number (neighbor), add to neighbors
        int numRows = display.length, numCols = display[0].length;
        for (int i = -1; i < 2; i++) {
            if (row + i >= 0 && row + i < numRows) {
                for (int j = -1; j < 2; j++) {
                    if (col + j >= 0 && col + j < numCols) {
                        if (display[row + i][col + j] == null || display[row + i][col + j].equals("?")) {
                            squaresLeft++;
                            Cord tempCord = new Cord((row + i), (col + j));
                            cords.add(tempCord);
                        } else if (display[row + i][col + j].equals("f"))
                            mineCount--;
                        else if (i != 0 || j != 0)
                            numberedNeighbors.add(new Cord(row + i, col + j));
                    } // if
                } // for
            } // if
        } // for
          // updates existing or return new
        if (squareStats[row][col] != null) {
            squareStats[row][col].update(mineCount, squaresLeft, squaresLeft != 0, cords, numberedNeighbors);
            return squareStats[row][col];
        }
        return new SquareStat(mineCount, squaresLeft, squaresLeft != 0, cords, numberedNeighbors, new Cord(row, col));

    } // makeSquareStat

    /*
     * Prints clump statistics for the specifies index. Used for testing.
     */
    public String getClumpsAt(int row, int col) {
        if (squareStats[row][col] == null)
            return "No stats found for the index";
        List clumps = squareStats[row][col].getClumps();
        return clumps.toString() + "\n" + squareStats[row][col].toString();
    }
}