package cs1302.game;

import cs1302.game.SquareStat;
import cs1302.game.Cord;
import java.util.List;
import java.util.ArrayList;

public class Clump {
    List<Cord> cords;
    SquareStat tile1;
    SquareStat tile2;
    private int clumpMines = 0;

    public Clump(SquareStat cord1, SquareStat cord2) {
        this.tile1 = cord1;
        this.tile2 = cord2;
        this.cords = new ArrayList<Cord>();
    }

    public void addToClump(Cord cord) {
        this.cords.add(cord);
     }

     public void setMinMines(int num) {
        this.clumpMines = num;
     }

     public int getClumpMines() {
        return this.clumpMines;
     }

     public int size() {
        return this.cords.size();
     }

     public boolean isInClump(Cord searchCord) {
        for (Cord cord : cords)
            if (cord.equals(searchCord))
                return true;
        return false;
     }

     @Override
     public String toString() { 
        return "Cords: " + this.cords + "\nTile1: "  + this.tile1.getCords() + "\nTile2: " + this.tile2.getCords() + "\nMinMines: "  + this.clumpMines;
     }
} // Clump
