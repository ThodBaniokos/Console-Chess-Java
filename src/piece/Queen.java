package piece;

import board.Board;
import enums.Color;
import location.Location;

public class Queen extends Piece {

    /**
     * Constructor of the Queen class, child of abstract class Piece
     * calls the constructor of the parent class
     * @param color color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board board reference of the piece
     */
    public Queen(Color color, Location location, Board board) {

        // call the parent class constructor
        super(color, location, board);
    }

    /**
     * Impements a queen move to a new location specified by newLoc
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) {

    }

    /**
     * Returns a string representation of the queen
     * @return uppercase 'Q' for white queen and lowercase 'q' for black queen
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "Q" : "q";
    }
}
