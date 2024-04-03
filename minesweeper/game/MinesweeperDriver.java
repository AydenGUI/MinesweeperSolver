package minesweeper.game;

import MinesweeperGame;
import minesweeper.solver.MinesweeperSolver;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Driver class of MinesweeperGame.
 */
public class MinesweeperDriver {
    public static void main(String[] args) throws java.io.FileNotFoundException {
        if (args.length > 0 ) {
            Scanner stdIn = new Scanner(System.in);
            String seedPath = args[0];
            MinesweeperGame game1 = new MinesweeperGame(stdIn, seedPath);
            game1.play(stdIn);
            
        } else {
            int rows, cols, bombs;
            Scanner scan = new Scanner(System.in);
            System.out.println("Set the Board size and number of bombs\nNumber of rows: ");
            rows = scan.nextInt();
            System.out.println("Number of columns: ");
            cols = scan.nextInt();
            System.out.println("Number of bombs: ");
            bombs = scan.nextInt();
            scan.nextLine();
            MinesweeperGame game1 = new MinesweeperGame(rows, cols, bombs);
            MinesweeperSolver solver1 = new MinesweeperSolver(game1);
            game1.setSolver(solver1);
            game1.play(scan);
        }



    } // main
} // MineSweeperDriver
