package piece;

import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class King extends Piece {

    private final String kingDistanceError = "King cannot move more than one squares per move";

    /**
     * Constructor of the King class, child of abstract class Piece
     * calls the constructor of the parent class
     *
     * @param color    color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board    board reference of the piece
     */
    public King(Color color, Location location, Board board) {

        super(color, location, board);
    }

    /**
     * Impements a king move to a new location specified by newLoc
     *
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) throws InvalidMoveException {

        int dist = this.board.chebyshevDistance(this.location, newLoc);

        if (dist > 1)
            throw new InvalidMoveException(this.kingDistanceError);

        if (this.board.getPieceAt(newLoc) != null) {

            this.board.movePieceCapturing(this.location, newLoc);

            return;
        }

        this.board.movePiece(this.location, newLoc);

    }

    /**
     * Returns a string representation of the king
     *
     * @return uppercase 'K' for white king and lowercase 'k' for black king
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "K" : "k";
    }
}
