package minesweeper.solver;

public class Cord {
    private int row;
    private int col;
   
    public Cord(int row, int col) {
       this.row = row;
       this.col = col;
       //clump = new ArrayList<Cord>();
    } // Constructor

    @Override
    public boolean equals(Object cord) {
        // if (cord.getClass()!=this.getClass())
        //     return false;
        Cord other = (Cord) cord;
        return this.row == other.getRow() && this.col == other.getCol();
    }
   
      // Getter for row
      public int getRow() {
       return row;
   }
   
   // Getter for col
   public int getCol() {
       return col;
   }
   
   // Setter for row
   public void setRow(int row) {
       this.row = row;
   }
   
   // Setter for col
   public void setCol(int col) {
       this.col = col;
   }
   
   public String toString() { 
       return this.row + " " + this.col;
   }
   } // Cord