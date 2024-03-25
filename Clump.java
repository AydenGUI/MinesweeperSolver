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
    private int minMines = 0;

    public Clump(SquareStat cord1, SquareStat cord2) {
        this.tile1 = cord1;
        this.tile2 = cord2;
        this.cords = new ArrayList<Cord>();
    }

    public Clump(Clump clump1, Clump clump2) {
         this.tile1 = clump1.getTile1();
         this.tile2 = clump1.getTile2();
         this.cords = new ArrayList<Cord>();
         this.cords.addAll(clump1.getCords());
         this.cords.addAll(clump2.getCords());
         if (clump1.getClumpMines() == 0|| clump2.getClumpMines() == 0) {
            this.clumpMines = 0;
         } else this.clumpMines = clump1.getClumpMines() + clump2.getClumpMines();
         this.minMines = clump1.getMinMines() + clump2.getMinMines();
         
    }

   //  public Clump(Clump clump1, Clump clump2, List<Cord> overlap) {
   //    if (clump1.getCords().size() < clump2.getCords().size()) {
   //       Clump temp = clump2;
   //       clump2 = clump1;
   //       clump1 = temp;
   //    }
   //    this.tile1 = clump2.getTile1();
   //    this.tile2 = clump2.getTile2();
   //    this.cords = overlap;
   //    this.clumpMines = clump1.getClumpMines() + clump2.getClumpMines() - (clump1.getCords().size() - overlap.size());
   //    this.clumpMines = clumpMines < 0 ? 0 : clumpMines;
   //  }

    public void addToClump(Cord cord) {
        this.cords.add(cord);
     }

     public void addToClump(List<Cord> cords) {
      this.cords.addAll(cords);
   }

     public void setNumMines(int num) {
        this.clumpMines = num;
     }

     public void setMinMines(int num) {
      this.minMines = num;
     }

     public int getMinMines() {
      return this.minMines;
   }

     public int getClumpMines() {
        return this.clumpMines;
     }

     public SquareStat getTile1() {
      return this.tile1;
     }

     public SquareStat getTile2() {
      return this.tile2;
     }

     public List<Cord> getCords() {
      return this.cords;
     } // getCords

     public List<Cord> getOverlap(Clump otherClump) {
      List<Cord> overlapCords = new ArrayList();
      for (Cord cord : this.cords)
         for (Cord otherCord: otherClump.getCords()) {
            if (cord.equals(otherCord)) {
               overlapCords.add(cord);
               break;
            }
         }
      return overlapCords;
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
        return "Cords: " + this.cords + "\nTile1: "  + this.tile1.getCords() + "\nTile2: " + this.tile2.getCords() + "\nMinMines: " + this.minMines + "\nGuaranteedMines: "  + this.clumpMines;
     }
} // Clump
