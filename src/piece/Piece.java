package piece;

import java.util.List;

import board.Board;
import enums.Color;
import exceptions.InvalidMoveException;
import location.Location;

public abstract class Piece {

    public Color color;
    public Location location;
    public Board board;

    /**
     * Constructor of abstract class Piece
     * @param color color of the piece, from the Color enumeration
     * @param location initial location of the piece
     * @param board board reference of the piece
     */
    public Piece(Color color, Location location, Board board) {

        this.color = color;
        this.location = location;
        this.board = board;
    }

    /**
     * Impements a move to a new location specified by newLoc
     * @param newLoc end location of the current played move
     * @throws InvalidMoveException
     */
    public abstract void moveTo(Location newLoc) throws InvalidMoveException;

    public abstract List<Location> generatePath();
}
