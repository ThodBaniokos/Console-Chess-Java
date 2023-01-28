package piece;

import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class Knight extends Piece {

    /**
     * Constructor of the Knight class, child of abstract class Piece
     * calls the constructor of the parent class
     * 
     * @param color    color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board    board reference of the piece
     */
    public Knight(Color color, Location location, Board board) {

        super(color, location, board);
    }

    /**
     * Impements a knight move to a new location specified by newLoc
     * 
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) throws InvalidMoveException {

        if (!(this.board.knightMoveCheck(this.location.getCol(), newLoc.getCol(), this.location.getRow(), newLoc.getRow()))) {
            throw new InvalidMoveException(
                    "Knight can move two squares vertically and one square horizontally, or two squares horizontally and one square vertically, forming an \"L\"");
        }

        if (this.board.getPieceAt(newLoc) != null) {

            this.board.movePieceCapturing(this.location, newLoc);

            return;
        }

        this.board.movePiece(this.location, newLoc);

        return;
    }

    /**
     * Returns a string representation of the knight
     * 
     * @return uppercase 'N' for white knight and lowercase 'n' for black knight
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "N" : "n";
    }
}
