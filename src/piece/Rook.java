package piece;

import board.Board;
import enums.Color;
import location.Location;

public class Rook extends Piece {

    /**
     * Constructor of the Rook class, child of abstract class Piece
     * calls the constructor of the parent class
     * @param color color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board board reference of the piece
     */
    public Rook(Color color, Location location, Board board) {

        // call the parent class constructor
        super(color, location, board);
    }

    /**
     * Impements a rook move to a new location specified by newLoc
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) {

    }

    /**
     * Returns a string representation of the rook
     * @return uppercase 'R' for white rook and lowercase 'r' for black rook
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "R" : "r";
    }
}
