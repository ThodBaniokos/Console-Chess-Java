package piece;

import java.util.ArrayList;
import java.util.List;

import Pair.Pair;
import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class Rook extends Piece {

    private List<Pair<Integer, Integer>> possibleMoves;

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

        this.possibleMoves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {

            possibleMoves.add(new Pair<Integer, Integer>(i, 0));
            possibleMoves.add(new Pair<Integer, Integer>(0, i));
            possibleMoves.add(new Pair<Integer, Integer>(-i, 0));
            possibleMoves.add(new Pair<Integer, Integer>(0, -i));
        }
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
     * Returns a string representation of the rook
     * 
     * @return uppercase 'R' for white rook and lowercase 'r' for black rook
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "R" : "r";
    }
}
