package piece;

import java.util.ArrayList;
import java.util.List;

import Pair.Pair;
import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class King extends Piece {

    private final String kingDistanceError = "King cannot move more than one squares per move";
    private List<Pair<Integer, Integer>> possibleMoves;

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

        this.possibleMoves = new ArrayList<>();

        this.possibleMoves.add(new Pair<Integer,Integer>(0, 1));
        this.possibleMoves.add(new Pair<Integer,Integer>(1, 1));
        this.possibleMoves.add(new Pair<Integer,Integer>(1, 0));
        this.possibleMoves.add(new Pair<Integer,Integer>(1, -1));
        this.possibleMoves.add(new Pair<Integer,Integer>(0, -1));
        this.possibleMoves.add(new Pair<Integer,Integer>(-1, -1));
        this.possibleMoves.add(new Pair<Integer,Integer>(-1, 0));
        this.possibleMoves.add(new Pair<Integer,Integer>(-1, 1));
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

    public List<Location> generatePath() {

        List<Location> path = new ArrayList<>();

        int currentRow = this.location.getRow() + 1;
        int currentColumn = this.location.getCol() + 1;

        for (Pair<Integer, Integer> possibleLoc : this.possibleMoves) {

            int newRow = currentRow + possibleLoc.firstObj;
            int newCol = currentColumn + possibleLoc.secondObj;

            if ((newRow >= 1 && newRow <= 8) && ((newCol >= 1 && newCol <= 8))) {
                path.add(new Location(newRow, newCol));
            }
        }

        return path;
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
