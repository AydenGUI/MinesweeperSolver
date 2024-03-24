package cs1302.game;

import cs1302.game.MinesweeperSolver;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class to make a functional Minesweeper game.
 */
public class MinesweeperGame {
    public int roundsCompleted = 0;
    private String[][] displayBoard;
    private boolean[][] gameBoard;
    private int rows;
    private int cols;
    private int bombs;
    private int totalBombs;
    private int remainingTiles;
    private boolean cheatOn, doContinue;
    private MinesweeperSolver solver = null;
    /**
     * Constructor for Minesweeper Board without a scanner and seedPath.
     * This constructor also randomizes the board.
     * @param rows Total number of rows
     * @param cols Total number of columns
     * @param bombs Total number of bombs on board
     */
    public MinesweeperGame(int rows, int cols, int bombs) {
        this.gameBoard = seedBoard(rows, cols, bombs);
        this.displayBoard = new String[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.bombs = bombs;
        this.totalBombs = bombs;
        this.remainingTiles = rows * cols;
        this.cheatOn = false;
        this.doContinue = true;
        this.printWelcome();
    } // MinesweeperGame

      /**
       * Constructor for Minesweeper Board using a scanner object and
       * a Seed File Path.
       * @param stdIn Scanner object
       * @param seedPath Seed File Path
       */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        File file;
        int i;
        try {
            file = new File(seedPath);
            stdIn = new Scanner(file);
        } catch (FileNotFoundException fnfe) {
            System.err.println("\nSeed File Not Found Error: " + fnfe.getMessage());
            System.exit(2);
        } // try
        int[] nums = new int[102];
        try {
            for (i = 0;i < 102; i++) {
                if (stdIn.hasNextInt()) {
                    nums[i] = stdIn.nextInt();
                } else {
                    nums[i] = -928263;
                } // if
            } // for
            if (stdIn.hasNext()) {
                throw new IllegalArgumentException("Token is not expected of type: int");
            } // if
            this.gameBoard = seedBoard(nums);
        } catch (IllegalArgumentException iae) {
            System.err.println("Seed File Malformed Error: " + iae.getMessage());
            System.exit(3);
        } // try
        int row = nums[0], col = nums[1], bomb = nums[2];
        this.displayBoard = new String[row][col];
        this.rows = row;
        this.cols = col;
        this.bombs = bomb;
        this.cheatOn = false;
        this.doContinue = true;
        this.printWelcome();
    } // MinesweeperGame

    /**
     * Makes a boolean 2D Array(Board) of specified dimensions and places
     * bombs according to seedFile.
     * @param nums Array of integers from seed file
     * @return temp 2D boolean array of size rows by cols with bombs at specified locations
     */
    public boolean[][] seedBoard (int[] nums) {
        int row = nums[0], col = nums[1], bombs = nums[2];
        if (row < 5 || row > 10) {
            throw new IllegalArgumentException("Row token expected range is [5, 10]");
        } // if
        if (col < 5 || col > 10) {
            throw new IllegalArgumentException("Column token expected range is [5, 10]");
        } // if
        if (bombs < 1 || bombs > (row * col)) {
            throw new IllegalArgumentException("numBomb token expected range is [1, " +
                                               (row * col - 1) + "]");
        } // if
        boolean[][] temp = new boolean[row][col];
        try {
            for (int i = 3; i < (3 + 2 * nums[2]); i += 2) {
                if (nums[i] != -928263 && temp[nums[i]][nums[i + 1]] != true) {
                    temp[nums[i]][nums[i + 1]] = true;
                    bombs--;
                } // if
            } // for
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new IllegalArgumentException(aioobe.getMessage());
        } // try
        if (bombs != 0) {
            throw new IllegalArgumentException("Number of bombs are not equal to" +
                                               " number of seeded indexes");
        } // if
        return temp;
    } // seedBoard

    /**
     * Makes a boolean board of specified dimensions and randomly places
     * specified number of bombs.
     * @param rows Number of rows
     * @param cols Number of columns
     * @param bombs Number of bombs
     * @return temp 2D boolean array of size rows by cols with randomly
     * placed bombs
     */
    public boolean[][] seedBoard (int rows, int cols, int bombs) {
            if (rows < 5 || rows > 10) {
            throw new IllegalArgumentException("Row token expected range is [5, 10]");
        } // if
        if (cols < 5 || cols > 10) {
            throw new IllegalArgumentException("Column token expected range is [5, 10]");
        } // if
        if (bombs < 1 || bombs > (rows * cols)) {
            throw new IllegalArgumentException("numBomb token expected range is [1, " +
                                               (rows * cols - 1) + "]");
        } // if
        double numLeft = rows * cols;
        boolean[][] temp = new boolean[rows][cols];
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                if (Math.random () <=  (bombs / numLeft)) {
                    temp[i][j] = true;
                    bombs --;
                } // if
                numLeft--;
            }
        } // for
        // When the random double is less than or equal to the ratio of bombs
        // to number of indexes left
        // Number of bombs decrease for each bomb added to the 2D array and numLeft
        //decreases for every j index you go through
        return temp;
    } // seedBoard

    /**
     * Gives number of columns.
     * @return cols Number of columns
     */
    public int getCols() {
        return this.cols;
    } // getCols

    /**
     * Gives number of rows.
     * @return rows Number of rows
     */
    public int getRows() {
        return this.rows;
    } // getRows

    /**
     * Gives number of remaining bombs.
     * @return bombs Number of bombs
     */
    public int getBombs() {
        return this.bombs;
    } // getBombs

    /**
     * Gives total number of bombs.
     * @return bombs Number of bombs
     */
    public int getTotalBombs() {
        return this.totalBombs;
    } // getTotalBombs

    /**
     * Give # of remaining untouched tiles.
     * @return remainingTiles integer
     */
    public int getRemaining() { 
        return this.remainingTiles;
    }

    /**
     * Gives displayBoard.
     * @return displayBoard 2D array of type String
     */
    public String[][] getDisplay() {
        return this.displayBoard;
    } // getDisplay

    /**
     * Gives gameBoard.
     * @return gameBoard 2D array of type boolean
     */
    public boolean[][] getGame() {
        return this.gameBoard;
    } // getGame

    /**
     * Returns if cheats are on.
     * @return cheatOn True if they are, false if not
     */
    public boolean isCheatOn() {
        return this.cheatOn;
    } // isCheatOn

    /**
     * Calculates and returns player score.
     * @return double Player Score
     */
    public double getScore() {
        double score = 100.0 * getRows() * getCols() / roundsCompleted;
        return score;
    } // getScore

    /**
     * Sets variable cheatOn to false.
     */
    public void setCheatOff() {
        this.cheatOn = false;
    } // setCheatOff

    /**
     * Sets a MinesweeperSolver that recommends move to user.
     * @param minesweeperSolver A MinesweeperSolver object for this game
     */
     public void setSolver(MinesweeperSolver minesweeperSolver) {
        this.solver = minesweeperSolver;
     }

    /**
     * Modifies the displayBoard.
     * @param row Row index
     * @param col Column index
     * @param str New string value for specified index of displayBoard
     */
    public void setDisplay(int row, int col, String str) {
        this.displayBoard[row][col] = str;
        this.remainingTiles--;
        if (remainingTiles <= 0 && isWon()) {
            printWin();
        } // if
    } // setDisplay

    /**
     * Counts number of bombs surrounding the specified sqaure.
     * @param row Row index
     * @param col Column index
     * @return Number of bombs surrounding the specified square.
     */
    public String count(int row, int col) {
        boolean[][] temp = getGame();
        int total = 0, numRows = getRows(), numCols = getCols();
        for (int i = -1; i < 2; i++) {
            if (row + i >= 0 && row + i < numRows) {
                for (int j = -1; j < 2; j++) {
                    if (col + j >= 0 && col + j < numCols && temp[row + i][col + j]) {
                        total++;
                    } // if
                } // for
            } // if
        } // for
        // i's are row indexes and only uses ones with valid indexes
        // j's are column indexes and only checks ones with valid indexes for
        // bombs and counts how many have them
        return "" + total;
    } // count

    /**
     * Performs a 'guess' on a specified square.
     * @param row Row index
     * @param col Column index
     */
    public void guess(int row, int col) {
        setDisplay(row, col, "?");
        roundsCompleted ++;
    } // guess

    /**
     * Reveals the state of the specified square and returns
     * if a bomb was revealed or not.
     * @param row Row index
     * @param col Column index
     * @return boom True if bomb was revealed, false if not
     */
    public boolean reveal(int row, int col) {
        //roundsCompleted ++;
        boolean boom = false;
        if (getGame()[row][col] == true) {
            printLoss();
            boom = true;
        } else {
            if (count(row,col).equals("0")) {
                setDisplay(row, col, count(row,col));
                //reveals all 0's touching
                // System.out.println("Nested Reveal");
                boolean isColGreater = col > 0;
                boolean isColLess = col < this.cols - 1;
                if (row > 0) {
                    reveal_connected_zeros(row-1, col, "u", "");
                    if (isColGreater)
                        reveal_connected_zeros(row-1, col-1, "ul", "");
                    if (isColLess)
                        reveal_connected_zeros(row-1, col+1, "ur", "");
                }
                if (row < this.rows - 1) {
                    reveal_connected_zeros(row+1, col, "d", "");
                    if (isColGreater)
                        reveal_connected_zeros(row+1, col-1, "dl", "");
                    if (isColLess)
                        reveal_connected_zeros(row+1, col+1, "dr", "");
                }
                if (isColGreater)
                    reveal_connected_zeros(row, col-1, "l", "");
                if (isColLess)
                    reveal_connected_zeros(row, col+1, "r", "");
            }
            else setDisplay(row, col, count(row,col));
        } // if
        return boom;
    } // reveal

    /**
     * This method performs the function of revealing every tile surrounding a 
     * 0 numbered title. This is the normal auto clear 0's feature of Minesweeper.
     * @param row Row index
     * @param col Column index
     * @param direction Direction that recursive called moved in table
     * @param oldDirection The previous direction that recursive called moved in table
     */
    public void reveal_connected_zeros(int row, int col, String direction, String oldDirection) {
        if (displayBoard[row][col]!=null) //if already displayed
            return;
        setDisplay(row, col, count(row,col));
        if (!count(row,col).equals("0"))
            return;
        boolean isColGreater = col > 0 && !direction.contains("r")&& !oldDirection.contains("r");
                boolean isColLess = col < this.cols - 1&& !direction.contains("l")&& !oldDirection.contains("l");
                if (row > 0 && !direction.contains("d") && !oldDirection.contains("d")) {
                    reveal_connected_zeros(row-1, col, "u", direction);
                    if (isColGreater)
                        reveal_connected_zeros(row-1, col-1, "ul", direction);
                    if (isColLess)
                        reveal_connected_zeros(row-1, col+1, "ur", direction);
                }
                if (row < this.rows - 1 && !direction.contains("u") && !oldDirection.contains("u")) {
                    reveal_connected_zeros(row+1, col, "d", direction);
                    if (isColGreater)
                        reveal_connected_zeros(row+1, col-1, "dl", direction);
                    if (isColLess)
                        reveal_connected_zeros(row+1, col+1, "dr", direction);
                }
                if (isColGreater)
                    reveal_connected_zeros(row, col-1, "l", direction);
                if (isColLess)
                    reveal_connected_zeros(row, col+1, "r", direction);
    } // reveal_connected_zeros
    
    /**
     * Marks the specified square as potentially having a bomb.
     * @param row Row index
     * @param col Column index
     */
    public void mark(int row, int col) {
        roundsCompleted ++;
        this.bombs--;
        setDisplay(row, col,"f");
    } // mark

    /**
     * Cheat command that shows bomb locations for one round.
     */
    public void unfog() {
        this.cheatOn = true;
        roundsCompleted ++;
    } // unfog

    /**
     * Checks if victory condition is met and ends game printing
     * score and victory message if it is.
     * @return isWon True if victory condition is met false if not
     */
    public boolean isWon() {
        int rows = getRows(), cols = getCols();
        int covered = getTotalBombs();
        String[][] board = getDisplay();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null || board[i][j].equals("?")) {
                    return false;
                } // if
                if (board[i][j].equals("f")) {
                    covered--;
                } // if
            }
        } // for

        // sees if the board has been modified for each index and if they have all been modified
        // and there are no guesses left and if the number of flags equal the number
        // of bombs then the game is deemed Won
        return (covered == 0);
    } // victoryMet

    /**
     * Displays the help message with command list.
     */
    public void getHelp() {
        roundsCompleted ++;
        System.out.println("Commands Available...\n - Reveal: r/reveal row col" +
                           "\n -   Mark: m/mark   row col\n -  Guess: g/guess  row col" +
                           "\n -   Help: h/help\n -   Quit: q/quit");
    }

    /**
     * Displays message and ends the game.
     */
    public void printLoss() {
        roundsCompleted --;
        System.out.println(" Oh no... You revealed a mine!\n  __ _  __ _ _ __ ___   ___" +
                           "    _____   _____ _ __\n / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\" +
                           " \\ / / _ \\ '__|\n| (_| | (_| | | | | | |  __/ | (_) \\" +
                           " V /  __/ |\n \\__, |\\__,_|_| |_| |_|\\___| " +
                           " \\___/ \\_/ \\___|_|\n |___/\n");

    } // printLoss

    /**
     * Displays victory message and score and ends the game.
     */
    public void printWin() {
        System.out.println("\n ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"\n" +
                           " ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░\n ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░" +
                           " \"Such Score\"\n ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░\n░░░" +
                           " ░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░" +
                           " \"Much Minesweeping\"" +
                           "\n ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░\n" +
                           " ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"" +
                           "\n ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░\n ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░" +
                           "\n ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░\n ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░" +
                           "\n ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌\n ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░" +
                           "\n ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░\n ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░" +
                           "\n ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░\n ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░" +
                           "CONGRATULATIONS!\n ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!" +
                           "\n ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " +
                           this.getScore() + "\n");
        System.exit(0);
    } // printWin

    /**
     * Displays a welcome screen.
     */
    public void printWelcome() {
        System.out.println("        _\n  /\\/\\ (F)_ __   ___  _____      __" +
                           "___  ___ _ __   ___ _ _" +
                           "_\n /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _" +
                           " \\ '__|\n/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  " +
                           "__/ |_) |  __/ |\n\\/    \\/_|_| |_|\\___||_" +
                           "__/ \\_/\\_/ \\___|\\___| .__" +
                           "/ \\___|_|\n                             ALPHA EDITION |_| v2022.sp");

    } // welcome

    /**
     * Creates a game board string showing player progress.
     */
    public void printMineField() {
        boolean cheatOn = isCheatOn();
        if (cheatOn) {
            setCheatOff();
        } // if
        // if cheats (fog cheat) are on then instead of normal print it has < > around bombs
        boolean[][] gameBoard = getGame();
        String[][] temp = getDisplay();
        String str = "\nRounds Completed: " + roundsCompleted + "\n";
        for (int i = 0; i < rows;i++) {
            str += "\n" + i + " |";
            for (int j = 0; j < cols; j++) {
                if (temp[i][j] == null) {
                    if (cheatOn && gameBoard[i][j]) {
                        str += "< >|";
                    } else {
                        str += "   |";
                    } // if
                } else if (cheatOn && gameBoard[i][j]) {
                    str += "<" + temp[i][j] + ">|";
                } else {
                    str += " " + temp[i][j] + " |";
                } // if
            } // for (j)
        } // for (i)
        // starts each row with its index then goes through display array and
        // prints its content between '| ' and ' |'
        // if cheats are on it does the same but if a bomb is at a certain index it
        // surrounds its contents with '|<' and '>|'
        // if the display array content is null then it is displayed as ' '
        str += "\n   ";
        for (int i = 0; i < cols;i++) {
            str += " " + i + "  ";
        } // for
        str += "\n";
        System.out.println(str);
    } // printMineField

    /**
     * Method which runs game until it reaches an end state.
     * @param stdIn Scanner object to get user input
     */
    public void play(Scanner stdIn) {
        this.getHelp();
        while (doContinue) {
            this.promptUser(stdIn);
            if (this.isWon()) {
                this.printWin();
                doContinue = false;
            } // if
        } // while
    } // play

    /**
     * This method prints the game prompt to standard output and
     * interprets user input from standard input.
     * @param stdIn Scanner object to get user input
     */
    public void promptUser(Scanner stdIn) {
        int row = 0, col = 0;
        this.printMineField();
        System.out.print("minesweeper-alpha: ");
        if (solver != null)
            System.out.println("Computer Recommendation: " + solver.getMoves());
        String stin = stdIn.nextLine();
        try {
            String[] form = format(stin);
            stin = form[0];
            if (form[1] != null && form[2] != null) {
                row = Integer.parseInt(form[1]);
                col = Integer.parseInt(form[2]);
            } // if
            if (row < 0 || col < 0 || row >= this.rows || col >= this.cols)
                throw new InputMismatchException("Coordinates out of range.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid index");
            stin = "a";
        } catch (InputMismatchException ime) {
            System.out.println(ime.getMessage());
            stin = "a";
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
            stin = "a";
        } // try
        switch (stin) {
        case "c":  
        System.out.println(this.solver.getClumpsAt(row, col));
            break;
        case "m": case "mark":
            this.mark(row,col);
            break;
        case "r": case "reveal":
            roundsCompleted ++;
            if (this.reveal(row,col)) {
                doContinue = false;
            } // if
            break;
        case "g": case "guess":
            this.guess(row,col);
            break;
        case "q": case "quit":
            System.out.println("Quitting the game...\nBye!");
            System.exit(0);
            break;
        case "h": case "help":
            this.getHelp();
            break;
        case "nofog":
            this.unfog();
            break;
        case "a":
            break;
        default:
            System.out.println("Invalid Command: You typed " + stin);
        } // switch
    } // promptUser

    /**
     * Formats student input into an array.
     * @param input Student input
     * @return array of the formatted input
     */
    public String[] format(String input) {
        String[] arr = new String[3];
        int index = 1, i2;
        String temp = "";
        boolean boo = true;
        for (int i = 0; i < input.length();i++) {
            if (input.charAt(i) != ' ') {
                i2 = i;
                while ((i2 < input.length() && input.charAt(i2) != ' ') && boo) {
                    //sets arr[0] to the argument before first space or end of string
                    if (input.charAt(i) == ' ') {
                        boo = false;
                    } // if
                    temp += input.charAt(i2);
                    i2 ++;
                } // for
                arr[0] = temp;
                if (input.length() <= i2) {
                    input = "";
                } else {
                    input = input.substring(i2 + 1);
                } // if
                break;
            } // if
        } // for
        Scanner reader = new Scanner(input);
        while (reader.hasNextInt()) {
            //grabs integer inputs after initial argument
            if (index == 3)
                throw new InputMismatchException("Expected 3 or less arguments.");
            arr[index] = "" + reader.nextInt();
            index++;
        } // while
        if (reader.hasNext()) {
            throw new InputMismatchException("Expected integer value.");
        } // if
        return arr;
    } // format

} // MinesweeperGame
