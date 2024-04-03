package minesweeper.solver;

import Cord;
import Clump;
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
        for (int i = 0; i < clumps.size(); i ++) {//Clump clump : this.getClumps()) {
            Clump clump = clumps.get(i);
            int minMinesInClump = Math.max (Math.max(
                clump.tile2.getMines() - (clump.tile2.getSquaresLeft() - clump.cords.size()),
                this.getMines() - (this.getSquaresLeft() - clump.cords.size())
            ) , 0);
            int maxMinesInClump = Math.min(Math.min(clump.tile2.getMines(), this.getMines()), 
            clump.size());

            if (minMinesInClump < 1) {
                clumps.remove(i);
                i--;
                continue;
            }
            clump.setMinMines(minMinesInClump);
            if (maxMinesInClump != minMinesInClump) {
                continue;
            }
            clump.setNumMines(maxMinesInClump);
            
            //if new minMines == 0 then that clump will all e revelaed anyways
            //not adding to prevent too many elements
            if (this.minesLeft - minMinesInClump > 0 && clump.getCords().size() < this.untouched.size()) {
                Clump newClump = new Clump(clump.getTile1(), clump.getTile1());
                List temp = new ArrayList<>(this.untouched);
                for (Cord cord: clump.getCords())
                    temp.remove(cord);
                newClump.addToClump(temp);
                newClump.setNumMines(this.minesLeft - minMinesInClump);
                newClump.setMinMines(this.minesLeft - minMinesInClump);
                // System.out.println("GOT HERE with\nNew: " + newClump + "\nOld: " + clump);
                this.clumps.add(0, newClump);
                i++;
            }
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
            //If minNumMines
            if (clumpMines == 0) {
                clumpMines = clump.getMinMines();
                if (this.getMines() - clumpMines == 0) {
                    //Reveal ALL not in clump
                    for (Cord cord : this.getUntouched())
                        if (!clump.isInClump(cord)) {
                            System.out.println("I WAS USEFUL!!!!!\n\n\n");
                            actions.add("r " + cord.toString());
                        }
                }
                continue;
            }
                
        
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
                        //Mark as mine
                        // System.out.println(clump);
                        actions.add("m " + cord.toString());
                    }
            } else if (this.getSquaresLeft() - clump.size() == this.getMines() - clumpMines) {
                //All not in clump are mines 
                for (Cord cord : this.getUntouched())
                    if (!clump.isInClump(cord)) {
                        //Mark as mine
                        // System.out.println(clump);
                        actions.add("m " + cord.toString());
                    }
            }
        } // for
    } // evaluateMyClumps

    public void createNewClumps(List actions) {
        /*
         * Iterates through all clumps. Ideally with no overlap. Same logic as evalClumps
         * but it checks more than one at a time. for instance tile with a 2 has 2 separate clumps which each 
         * have 1 minMine it combines them into one new clump. When this clump is evaluated it will reveal all other tiles
         * If there is overlap numMines will be evaluated as numMines - (clumpTiles - overlapSquares) 
         * Maybe add later
         */
        // if (this.clumps.size() >= 2)
        for (int i = 0; i < this.clumps.size(); i++) {
            Clump clump = this.clumps.get(i);
            if (clump.getClumpMines() > 0)
            for (int j = 0; j < this.clumps.size(); j++) { 
                if (clumps.get(j).getClumpMines() == 0)
                    continue;
                if (clump.getOverlap(clumps.get(j)).size() == 0) {
                    Clump newClump = new Clump(clump, clumps.get(j));
                    clumps.add(newClump);
                }
                // if (clump.getOverlap(clumps.get(j)).size() > 0 && clump.getOverlap(clumps.get(j)).size() != clump.getCords().size()) {
                //     System.out.print("*");
                    // Clump newClump = new Clump(clump, clumps.get(j), clump.getOverlap(clumps.get(j)));
                    // if (newClump.getClumpMines() > 0) {
                    //     System.out.print("*");
                    //     clumps.add(0, newClump);
                    //     j ++;
                    //     i++;
                    // } // if
                // }
            }

        } // for outer      
    } // createNewClumps

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
        return "\nminesLeft: "  + this.minesLeft  + "\nsquaresLeft: " + this.squaresLeft + "\nuntouched: " + untouched + "\nNeighbors: " + this.numberedNeighbors +
        "\ndoConsider: " + this.getDoConsider();
    }
} //SquareStat
