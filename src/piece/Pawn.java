package piece;

import board.Board;
import enums.Color;
import errorMessages.ErrorMessages;
import exceptions.InvalidMoveException;
import location.Location;

public class Pawn extends Piece {

    private final String cannotMoveMoreThanTwoOnFirstMove = "Pawn piece cannot move more than two squares on it's first move";
    private final String cannotMoveMoreThanOneMove = "Pawn piece cannot move more than one square on it's move";
    private final String differentColumnWithoutKill = "Given move of Pawn does not have the same column and there is not another opponents piece on the landing square";

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
    public void moveTo(Location newLoc) throws InvalidMoveException {

        int dist = this.board.chebyshevDistance(this.location, newLoc);

        boolean isFirstPawnMove = (this.board.hasPawnMoved.get(this) == null);

        if (isFirstPawnMove == true && dist > 2)
            throw new InvalidMoveException(this.cannotMoveMoreThanTwoOnFirstMove);

        if (isFirstPawnMove == false && dist > 1)
            throw new InvalidMoveException(this.cannotMoveMoreThanOneMove);

        if (this.location.getCol() != newLoc.getCol() && this.board.getPieceAt(newLoc) == null)
            throw new InvalidMoveException(differentColumnWithoutKill);

        if (this.location.getCol() != newLoc.getCol() && (this.board.getPieceAt(newLoc) != null
                && this.board.getPieceAt(newLoc).color == this.color))
            throw new InvalidMoveException(ErrorMessages.cannotStepOnYourPiecesErrorMessage);

        if (this.board.freeVerticalPath(this.location, newLoc) == false)
            throw new InvalidMoveException("Pawn " + ErrorMessages.cannotStepOverOtherPieces);

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
