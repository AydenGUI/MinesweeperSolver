package cs1302.game;

import cs1302.game.MinesweeperGame;
import cs1302.game.SquareStat;
import cs1302.game.Cord;
import cs1302.game.Clump;

import java.util.List;
import java.util.ArrayList;

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
            //reevaluates when zeros clear
            this.moves.clear();
        }
        if (this.moves.size() == 0) {
            this.evaluate();
        }
        if (this.moves.size() == 0) {
            System.out.println("CREATE CLUMPS:");
            this.createClumps();
            System.out.println("CLUMP MOVE:");
            this.evaluateClumps();
        }
        if (this.moves.size() == 0) {
            System.out.println("RANDOM MOVE:");
            this.bestRandom();
        }
            
        String temp = "" + moves.get(0);
        moves.remove(0);
        return temp;
    }
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
                            if (moves.size()!=0)
                                return;
                        }
                        // if no mines remain
                        if (this.squareStats[i][j].getMines() == 0) {
                            for (int l = 0; l < this.squareStats[i][j].getUntouched().size(); l++)
                                this.moves.add("r " + this.squareStats[i][j].getUntouched().get(l)); 
                            if (moves.size()!=0)
                                return;
                        }

                    }
                } // outmost if   
            } //inner for
        } // for
    } //evaluate

    public void evaluateClumps() {
        for (int i = 0; i < squareStats.length; i++) {
            for (SquareStat stat : squareStats[i]) {
                if (stat != null && stat.getDoConsider()) {
                    stat.statClumps();
                    stat.evaluateMyClumps(this.moves);
                }
            }
        }
    } // evaluateClumps

    //Not best yet
    public void bestRandom() {
        String[][] board = minesweeperGame.getDisplay();
        double minPercent = 1.0;
        String move = "";
        List<Cord> randCords = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null)
                    randCords.add(new Cord(i, j));
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //checks if is a numbered tile
                if (board[i][j] != null && !board[i][j].equals("f") && !board[i][j].equals("?")) {
                    for (int k = -1; k < 2; k++)   
                        if(i + k >= 0 && i + k < board.length) 
                            for (int l = -1; l < 2; l++) 
                                if(j + l >= 0 && j + l < board[0].length)
                                    randCords.remove(new Cord(i + k, j + l));
                                    //if the Cord doesn't exist, nothing happens
                } // if
                else if (this.squareStats[i][j]!=null && (double)this.squareStats[i][j].getSquaresLeft()/this.squareStats[i][j].getMines() < minPercent) {
                    move = "r " + this.squareStats[i][j].getUntouched().get(0).toString();
                    minPercent = (double)this.squareStats[i][j].getSquaresLeft()/this.squareStats[i][j].getMines();
                }
            }
        }
        System.out.println(randCords);
        // if the odds of a nonrevealed square being a mine is less than any other known square, pick a random one
        if ((double)this.minesweeperGame.getBombs()/this.minesweeperGame.getRemaining() < minPercent && randCords.size() > 0) {
            int rand = (int)(Math.random() * randCords.size());
            move = "r " + randCords.get(rand).toString();
            //System.out.println("RMove: " + move);
        }


        this.moves.add(move);
        // } // if
    }

    public void createClumps() {
        int untouchedOverlap;
        for (int i = 0; i < this.squareStats.length; i++) {
            //gets all non-null stats in the array
            for (SquareStat stat : squareStats[i]) {
                if (stat != null && stat.getDoConsider()) {
                    stat.clearClumps();
                    for (Cord neighbor : stat.getNeighbors()) {
                        //So I have a square stat and im iterating through their neighbors
                        untouchedOverlap = 0;
                        List<Cord> untouchedCords1 = stat.getUntouched();
                        List<Cord> untouchedCords2 = this.squareStats[neighbor.getRow()][neighbor.getCol()].getUntouched();   
                        Clump clump = new Clump(stat, this.squareStats[neighbor.getRow()][neighbor.getCol()]);
                        for (Cord cord : untouchedCords1) 
                            for (Cord cord2: untouchedCords2) {
                                if (cord.equals(cord2)) {
                                    untouchedOverlap++;
                                    clump.addToClump(cord);
                                }
                            }
                        if (untouchedOverlap > 0)
                            stat.addClump(clump);
                        //now each stat should be populated with neighboring clumps of > 1
                        
                        //Checks how many untouched overlap
                        //how many more nonoverlap each have and how many remaining mines.
                        //this results in clump having at least mines - nonoverlap = number of mines
                        
                        //this.squareStats[neighbor.getRow()][neighbor.getCol()].getUntouched();
                    } // for 
                } //if  
            } //for 
        }   
    } // createClumps
    public void clumpToClumpCompare() {
        
        for (int i = 0; i < this.squareStats.length; i++) {
            //gets all non-null stats in the array
            for (int j = 0; j < this.squareStats[0].length; j++) {
                SquareStat stat = squareStats[i][j];
                if (stat == null || !stat.getDoConsider()) 
                    continue;
                if (stat.getClumps().size() > 0) {
                    /*for each tile with a clump check all surrounding tiles for overlapping clumps ( > 1 shared tile in clump)
                    include tiles with one gap in between this tile and it, excluding gapped corners 
                    (they can't share >1 tile )
                    actually just need to check bottom half b/c starts from top left and moves down
                    */
                    for (int k = -2; k < 3; k ++)
                        if (i + k >= 0 && i + k < this.squareStats.length)
                            for (int l = 0; l < 3; l++) {
                                if ((l == 0 && k <= 0) || (l == 2 && Math.abs(k) == 2)) //been checked already or are gapped corners
                                    continue;
                                if (j + l < squareStats[0].length && j + l >= 0) // out of squareStat range
                                if (squareStats[i + k][j + l] != null && squareStats[i + k][j + l].getClumps().size() > 0) {
                                    //check for overlap and evaluate according to clump rules in SquareStat
                                } //if
                            }

                } // if
            } // for in
        } // for out
    }
    /*
     * Creates required statistics for squares and relationships between them.
     * @param row Row index
     * @param col Column index
     * @param display Display board
     */
    public SquareStat makeSquareStat(int row, int col) { //, String[][] display){
        String[][] display = minesweeperGame.getDisplay();
        int mineCount = Integer.valueOf(display[row][col]);
        int squaresLeft = 0;
        List cords = new ArrayList<String>();
        List<Cord> numberedNeighbors = new ArrayList<Cord>();
        //checks all surrounding indecies, if square is marked as a bomb mineCount--
        //if square is null (untouched), squaresLeft ++;
        //if square is number (neighbor), add to neighbors
        int numRows = display.length, numCols = display[0].length;
        for (int i = -1; i < 2; i++) {
            if (row + i >= 0 && row + i < numRows) {
                for (int j = -1; j < 2; j++) {
                    if (col + j >= 0 && col + j < numCols) {
                      if (display[row+i][col+j] == null || display[row+i][col+j].equals("?")) {
                            squaresLeft++;
                            Cord tempCord = new Cord((row+i), (col+j));
                            cords.add(tempCord);
                        } 
                        else if (display[row+i][col+j].equals("f"))
                            mineCount--;
                        else if (i != 0 || j != 0)
                            numberedNeighbors.add(new Cord(row + i, col + j));
                    } // if
                } // for
            } // if
    } // for
        //updates existing or return new
        if (squareStats[row][col] != null) {
            squareStats[row][col].update(mineCount, squaresLeft, squaresLeft != 0, cords, numberedNeighbors);
            return squareStats[row][col];
        }
        return new SquareStat(mineCount, squaresLeft, squaresLeft != 0, cords, numberedNeighbors, new Cord(row, col));

} // makeSquareStat       

public String getClumpsAt(int row, int col) {
    List clumps = squareStats[row][col].getClumps();
    return clumps.toString() + "\n" + squareStats[row][col].toString();
}
}