package piece;

import board.Board;
import enums.Color;
import location.Location;

public class Pawn extends Piece {

    /**
     * Constructor of the Pawn class, child of abstract class Piece
     * calls the constructor of the parent class
     * @param color color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board board reference of the piece
     */
    public Pawn(Color color, Location location, Board board) {

        // call the parent class constructor
        super(color, location, board);
    }

    /**
     * Impements a pawn move to a new location specified by newLoc
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) {

        if (this.board.getPieceAt(newLoc) != null) {

            this.board.movePieceCapturing(this.location, newLoc);

            return;
        }

        this.board.movePiece(this.location, newLoc);

        return;
    }

    public Color getColor() {

        return this.color;
    }

    /**
     * Returns a string representation of the pawn
     * @return uppercase 'P' for white pawn and lowercase 'p' for black pawn
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "P" : "p";
    }
}
