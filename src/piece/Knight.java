package piece;

import board.Board;
import enums.Color;
import location.Location;

public class Knight extends Piece {

    /**
     * Constructor of the Knight class, child of abstract class Piece
     * calls the constructor of the parent class
     * @param color color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board board reference of the piece
     */
    public Knight(Color color, Location location, Board board) {

        super(color, location, board);
    }

    /**
     * Impements a knight move to a new location specified by newLoc
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) {

    }

    /**
     * Returns a string representation of the knight
     * @return uppercase 'N' for white knight and lowercase 'n' for black knight
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "N" : "n";
    }
}