package piece;

import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class Rook extends Piece {

    /**
     * Constructor of the Rook class, child of abstract class Piece
     * calls the constructor of the parent class
     * 
     * @param color    color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board    board reference of the piece
     */
    public Rook(Color color, Location location, Board board) {

        // call the parent class constructor
        super(color, location, board);
    }

    /**
     * Impements a rook move to a new location specified by newLoc
     * 
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) throws InvalidMoveException {

        if (this.location.getCol() == newLoc.getCol()
                && this.board.freeVerticalPath(this.location, newLoc) == false)
            throw new InvalidMoveException("Rook piece cannot go over other pieces");

        if (this.location.getRow() == newLoc.getRow()
                && this.board.freeHorizontalPath(this.location, newLoc) == false)
            throw new InvalidMoveException("Rook piece cannot go over other pieces");

        if (this.board.getPieceAt(newLoc) != null) {

            this.board.movePieceCapturing(this.location, newLoc);

            return;
        }

        this.board.movePiece(this.location, newLoc);

        return;
    }

    /**
     * Returns a string representation of the rook
     * 
     * @return uppercase 'R' for white rook and lowercase 'r' for black rook
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "R" : "r";
    }
}
