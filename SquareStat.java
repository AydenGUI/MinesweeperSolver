package cs1302.game;

import cs1302.game.Cord;
import cs1302.game.Clump;
import java.util.List;
import java.util.ArrayList;

public class SquareStat {
    
    private boolean doConsider;
    private int minesLeft;
    private int squaresLeft;
    private Cord cords;
    private List<Cord> untouched;
    private List<Cord> numberedNeighbors;   
    private List<Clump> clumps;

    public SquareStat(int mines, int squares, boolean doEvaluate, List<Cord> untouched, List<Cord> numberedNeighbors, Cord cord) {
        this.minesLeft = mines;
        this.squaresLeft = squares;
        this.doConsider = doEvaluate;
        this.untouched = untouched;
        this.numberedNeighbors = numberedNeighbors;
        this.clumps = new ArrayList<Clump>();
        this.cords = cord;
    }

    public void update(int mines, int squares, boolean doEvaluate, List<Cord> untouched, List<Cord> numberedNeighbors) {
        this.minesLeft = mines;
        this.squaresLeft = squares;
        this.doConsider = doEvaluate;
        this.untouched = untouched;
        this.numberedNeighbors = numberedNeighbors;
    }

    public void statClumps() { 
        //List actions = new ArrayList<String>();
        for (Clump clump : this.getClumps()) {
            int minMinesInClump = Math.max (Math.max(
                clump.tile2.getMines() - (clump.tile2.getSquaresLeft() - clump.cords.size()),
                this.getMines() - (this.getSquaresLeft() - clump.cords.size())
            ) , 0);
            int maxMinesInClump = Math.min(Math.min(clump.tile2.getMines(), this.getMines()), 
            clump.size());
            if (maxMinesInClump != minMinesInClump)
                minMinesInClump = 0;
            clump.setMinMines(minMinesInClump);

        } // for
    }


    /*
     * ClumpOverlap if for 2 clumps, all characters of one or both are within the other 
check for surrounging clumps

Then for each squareStat check if while no overlap between clumps. 
Check if the squaresLeft - sum clumpMines == 0, if do remove all, if it equals numMines not in clumps flag them. 
     */
    public void evaluateMyClumps(List actions) { 
        for (Clump clump : this.getClumps()) {
            int clumpMines = clump.getClumpMines();
            if (clumpMines == 0)
                break;
        
            if (this.getMines() - clumpMines == 0) {
                //Reveal ALL not in clump
                for (Cord cord : this.getUntouched())
                    if (!clump.isInClump(cord)) {
                        actions.add("r " + cord.toString());
                    }
            } else if (clump.tile2.getMines() - clumpMines == 0) {
                //Reveal ALL not in clump
                for (Cord cord : clump.tile2.getUntouched())
                    if (!clump.isInClump(cord)) {
                        actions.add("r " + cord.toString());
                    }
            }
            if (clump.tile2.getSquaresLeft() - clump.size() == clump.tile2.getMines() - clumpMines) {
                //All not in clump are mines 
                for (Cord cord : clump.tile2.getUntouched())
                    if (!clump.isInClump(cord)) {
                        //Remove this
                        System.out.println(clump);
                        actions.add("m " + cord.toString());
                    }
            } else if (this.getSquaresLeft() - clump.size() == this.getMines() - clumpMines) {
                //All not in clump are mines 
                for (Cord cord : this.getUntouched())
                    if (!clump.isInClump(cord)) {
                        //Remove this
                        System.out.println(clump);
                        actions.add("m " + cord.toString());
                    }
            }
        } // for
    } // evaluateMyClumps

    public List<Cord> getUntouched() {
        return this.untouched;
    } 

    public int getMines() { 
        return this.minesLeft;
    }
     
    public int getSquaresLeft() { 
        return this.squaresLeft;
    }
    public Cord getCords() {
        return this.cords;
    }

    public List<Cord> getNeighbors() {
        return this.numberedNeighbors;
    }

    public boolean getDoConsider() {
        return this.doConsider;
    } 

    public void addClump(Clump clump) { 
        this.clumps.add(clump);
    }

    public List<Clump> getClumps() {
        return this.clumps;
    }

    public void clearClumps() {
        this.clumps.clear();
    }

    public void setDoConsider(boolean bool) {
        this.doConsider = bool;
    }

    @Override
    public String toString() {
        return "\nminesLeft: "  + this.minesLeft  + "\nsquaresLeft: " + this.squaresLeft + "\nuntouched: " + untouched + "\nNeighbors: " + this.numberedNeighbors;
    }
} //SquareStat
