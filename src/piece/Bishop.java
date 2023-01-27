package piece;

import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class Bishop extends Piece {

    private final String cannotGoOverPieces = "Bishop cannot go over other pieces";

    /**
     * Constructor of the Bishop class, child of abstract class Piece
     * calls the constructor of the parent class
     * 
     * @param color    color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board    board reference of the piece
     */
    public Bishop(Color color, Location location, Board board) {

        super(color, location, board);
    }

    /**
     * Impements a bishop move to a new location specified by newLoc
     * 
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) throws InvalidMoveException {

        int currentColumn = this.location.getCol();

        if (newLoc.getCol() > currentColumn) {

            if (this.board.freeDiagonalPath(this.location, newLoc) == false)
                throw new InvalidMoveException(this.cannotGoOverPieces);
        } else {

            if (this.board.freeAntidiagonalPath(this.location, newLoc) == false)
                throw new InvalidMoveException(this.cannotGoOverPieces);
        }

        if (this.board.getPieceAt(newLoc) != null) {

            this.board.movePieceCapturing(this.location, newLoc);

            return;
        }

        this.board.movePiece(this.location, newLoc);

        return;
    }

    /**
     * Returns a string representation of the bishop
     * 
     * @return uppercase 'B' for white bishop and lowercase 'b' for black bishop
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "B" : "b";
    }
}
