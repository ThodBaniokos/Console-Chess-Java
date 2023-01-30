package piece;

import java.util.ArrayList;
import java.util.List;

import Pair.Pair;
import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public class Knight extends Piece {

    private List<Pair<Integer, Integer>> possibleMoves;

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

        this.possibleMoves = new ArrayList<>();

        this.possibleMoves.add(new Pair<Integer, Integer>(2, 1));
        this.possibleMoves.add(new Pair<Integer, Integer>(2, -1));
        this.possibleMoves.add(new Pair<Integer, Integer>(1, 2));
        this.possibleMoves.add(new Pair<Integer, Integer>(1, -2));
        this.possibleMoves.add(new Pair<Integer, Integer>(-2, 1));
        this.possibleMoves.add(new Pair<Integer, Integer>(-2, -1));
        this.possibleMoves.add(new Pair<Integer, Integer>(-1, 2));
        this.possibleMoves.add(new Pair<Integer, Integer>(-1, -2));
    }

    /**
     * Impements a knight move to a new location specified by newLoc
     * 
     * @param newLoc end location of the current played move
     */
    public void moveTo(Location newLoc) throws InvalidMoveException {

        if (!(this.board.knightMoveCheck(this.location.getCol(), newLoc.getCol(), this.location.getRow(),
                newLoc.getRow()))) {
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
     * Returns a string representation of the knight
     * 
     * @return uppercase 'N' for white knight and lowercase 'n' for black knight
     */
    @Override
    public String toString() {

        return (this.color == Color.WHITE) ? "N" : "n";
    }
}
