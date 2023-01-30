package board;

import location.Location;
import piece.*;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import enums.*;

public class Board {

    private final int boardRows = 8;
    private final int boardColumns = 8;
    public Location whiteKingLocation;
    public Location blackKingLocation;

    public BitSet canAttackWhite;

    public BitSet canAttackBlack;

    public BitSet bitSetBoard;

    public HashMap<Piece, Boolean> hasPawnMoved;

    public Piece[][] board;

    public Board() {

        this.init();
    }

    public void init() {

        this.canAttackWhite = new BitSet(64);

        this.canAttackBlack = new BitSet(64);

        this.bitSetBoard = new BitSet(64);

        this.hasPawnMoved = new HashMap<>();

        // initiaze empty board of pieces
        this.board = new Piece[this.boardRows][this.boardColumns];

        // create the white pieces first
        // ROW * 8 + COLUMN
        for (int i = 0; i < 8; i++) {
            this.bitSetBoard.set((1 * 8) + i);
            this.board[1][i] = new Pawn(Color.WHITE, new Location(2, i + 1), this);
        }

        this.board[0][0] = new Rook(Color.WHITE, new Location(1, 1), this);
        this.bitSetBoard.set(0);
        this.board[0][1] = new Knight(Color.WHITE, new Location(1, 2), this);
        this.bitSetBoard.set(1);
        this.board[0][2] = new Bishop(Color.WHITE, new Location(1, 3), this);
        this.bitSetBoard.set(2);
        this.board[0][3] = new Queen(Color.WHITE, new Location(1, 4), this);
        this.bitSetBoard.set(3);
        this.board[0][4] = new King(Color.WHITE, new Location(1, 5), this);
        this.bitSetBoard.set(4);
        this.board[0][5] = new Bishop(Color.WHITE, new Location(1, 6), this);
        this.bitSetBoard.set(5);
        this.board[0][6] = new Knight(Color.WHITE, new Location(1, 7), this);
        this.bitSetBoard.set(6);
        this.board[0][7] = new Rook(Color.WHITE, new Location(1, 8), this);
        this.bitSetBoard.set(7);

        this.whiteKingLocation = this.board[0][4].location;

        // set the initial attacking positions of white
        for (int i = 0; i < this.boardColumns; i++) {

            this.canAttackWhite.set((2 * 8) + i);
        }

        // create the black pieces
        for (int i = 0; i < 8; i++) {

            this.bitSetBoard.set((6 * 8) + i);
            this.board[6][i] = new Pawn(Color.BLACK, new Location(7, i + 1), this);
        }

        this.board[7][0] = new Rook(Color.BLACK, new Location(8, 1), this);
        this.bitSetBoard.set((7 * 8));
        this.board[7][1] = new Knight(Color.BLACK, new Location(8, 2), this);
        this.bitSetBoard.set((7 * 8) + 1);
        this.board[7][2] = new Bishop(Color.BLACK, new Location(8, 3), this);
        this.bitSetBoard.set((7 * 8) + 2);
        this.board[7][3] = new Queen(Color.BLACK, new Location(8, 4), this);
        this.bitSetBoard.set((7 * 8) + 3);
        this.board[7][4] = new King(Color.BLACK, new Location(8, 5), this);
        this.bitSetBoard.set((7 * 8) + 4);
        this.board[7][5] = new Bishop(Color.BLACK, new Location(8, 6), this);
        this.bitSetBoard.set((7 * 8) + 5);
        this.board[7][6] = new Knight(Color.BLACK, new Location(8, 7), this);
        this.bitSetBoard.set((7 * 8) + 6);
        this.board[7][7] = new Rook(Color.BLACK, new Location(8, 8), this);
        this.bitSetBoard.set((7 * 8) + 7);

        this.blackKingLocation = this.board[7][4].location;

        // set the initial attacking positions of black
        for (int i = 0; i < this.boardColumns; i++) {

            this.canAttackBlack.set((5 * 8) + i);
        }
    }

    public void movePiece(Location from, Location to) {

        Piece movingPiece = this.getPieceAt(from);

        this.board[from.getRow()][from.getCol()] = null;

        this.board[to.getRow()][to.getCol()] = movingPiece;

        movingPiece.location = to;

        // this.bitSetBoard.set((from.getRow() * 8) + from.getCol(), false);
        // this.bitSetBoard.set((to.getRow() * 8) + to.getCol());

        // List<Location> canReach = movingPiece.generatePath();

        // BitSet testBitSet = new BitSet(64);

        // for (Location loc : canReach) {

        //     testBitSet.set((loc.getRow() * 8) + loc.getCol());
        // }

        // System.out.println("Current state of can white attack");
        // this.printBitSet(canAttackWhite);

        // System.out.println("Current state of test bit set");
        // this.printBitSet(testBitSet);

        // BitSet tempBitSet = canAttackWhite;

        // tempBitSet.or(testBitSet);

        // if (tempBitSet.get((to.getRow() * 8) + to.getCol()) == true)
        //     tempBitSet.set((to.getRow() * 8) + to.getCol(), false);

        // if (movingPiece instanceof Pawn) {
        //     if (to.getRow() + 1 <= 7)
        //         tempBitSet.set(((to.getRow() + 1) * 8) + to.getCol(), false);
        // }

        // this.printBitSet(tempBitSet);

        // System.out.println(canReach);
    }

    public void movePieceCapturing(Location from, Location to) {

        this.board[to.getRow()][to.getCol()] = null;

        Piece movingPiece = this.getPieceAt(from);

        this.board[from.getRow()][from.getCol()] = null;

        this.board[to.getRow()][to.getCol()] = movingPiece;

        movingPiece.location = to;
    }

    public Piece getPieceAt(Location loc) {

        // return the piece in the given board location
        return this.board[loc.getRow()][loc.getCol()];
    }

    public boolean freeHorizontalPath(Location from, Location to) {

        int row = from.getRow();

        int smallestColumn, biggerColumn;

        if (from.getCol() < to.getCol()) {
            smallestColumn = from.getCol() + 1;
            biggerColumn = to.getCol() - 1;
        } else {
            smallestColumn = to.getCol() + 1;
            biggerColumn = from.getCol() - 1;
        }

        for (int i = smallestColumn; i <= biggerColumn; i++) {

            if (this.board[row][i] != null) {
                return false;
            }
        }

        return true;
    }

    public boolean freeVerticalPath(Location from, Location to) {

        int column = from.getCol();

        int smallestRow, biggerRow;

        if (from.getRow() < to.getRow()) {
            smallestRow = from.getRow() + 1;
            biggerRow = to.getRow() - 1;
        } else {
            smallestRow = to.getRow() + 1;
            biggerRow = from.getRow() - 1;
        }

        for (int i = smallestRow; i <= biggerRow; i++) {

            if (this.board[i][column] != null) {
                return false;
            }
        }

        return true;
    }

    public boolean freeDiagonalPath(Location from, Location to) {

        if (from.toString().compareTo(to.toString()) > 0) {

            int tempStartRow = from.getRow() - 1;
            int tempStartCol = from.getCol() - 1;

            int tempEndRow = to.getRow();
            int tempEndCol = to.getCol();

            while (tempStartRow > tempEndRow && tempStartCol > tempEndCol) {

                if (this.board[tempStartRow--][tempStartCol--] != null) {
                    return false;
                }
            }
        } else {

            int tempStartRow = from.getRow() + 1;
            int tempStartCol = from.getCol() + 1;

            int tempEndRow = to.getRow();
            int tempEndCol = to.getCol();

            while (tempStartRow < tempEndRow && tempStartCol < tempEndCol) {

                if (this.board[tempStartRow++][tempStartCol++] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean freeAntidiagonalPath(Location from, Location to) {

        if (from.toString().compareTo(to.toString()) > 0) {

            int tempStartRow = from.getRow() + 1;
            int tempStartCol = from.getCol() - 1;

            int tempEndRow = to.getRow();
            int tempEndCol = to.getCol();

            while (tempStartRow < tempEndRow && tempStartCol > tempEndCol) {

                if (this.board[tempStartRow++][tempStartCol--] != null) {
                    System.out.println();
                    return false;
                }
            }
        } else {

            int tempStartRow = from.getRow() - 1;
            int tempStartCol = from.getCol() + 1;

            int tempEndRow = to.getRow();
            int tempEndCol = to.getCol();

            while (tempStartRow > tempEndRow && tempStartCol < tempEndCol) {

                if (this.board[tempStartRow--][tempStartCol++] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {

        String gameBoard = " abcdefgh \n";

        for (int i = this.boardRows - 1; i >= 0; i--) {

            // concat the row number
            gameBoard = gameBoard.concat(Integer.toString(i + 1));

            for (int j = 0; j < this.boardColumns; j++) {

                gameBoard += ((this.board[i][j] != null) ? this.board[i][j].toString() : " ");
            }

            // concat row number and newline
            gameBoard = gameBoard.concat(Integer.toString(i + 1) + "\n");
        }

        gameBoard = gameBoard.concat(" abcdefgh \n");

        return gameBoard;
    }

    public int chebyshevDistance(Location from, Location to) {

        return Math.max(Math.abs(from.getRow() - to.getRow()), Math.abs(from.getCol() - to.getCol()));
    }

    public void printBitSet(BitSet toManipulate) {

        StringBuilder setToPrint = new StringBuilder();

        for (int i = 0; i < this.boardRows; i++) {

            for (int j = 0; j < this.boardColumns; j++) {

                setToPrint.append((toManipulate.get((i * 8) + j) == true) ? "1" : ".");
            }
            setToPrint.append("\n");
        }

        System.out.println(setToPrint);
    }

    public void printCanAttack(Color givenColor) {

        BitSet toManipulate;

        if (givenColor == Color.WHITE)
            toManipulate = this.canAttackWhite;
        else
            toManipulate = this.canAttackBlack;

        StringBuilder setToPrint = new StringBuilder();

        for (int i = 0; i < this.boardRows; i++) {

            for (int j = 0; j < this.boardColumns; j++) {

                setToPrint.append((toManipulate.get((i * 8) + j) == true) ? "1" : ".");
            }
            setToPrint.append("\n");
        }
    }

    public boolean sameDiagonal(Piece piece1, Piece piece2) {

        if (Math.abs(piece1.location.getRow() - piece2.location.getRow()) != Math
                .abs(piece1.location.getCol() - piece2.location.getCol()))
            return false;

        return true;
    }

    public boolean knightMoveCheck(int startingColumn, int endingColumn, int startingRow, int endingRow) {

        if ((startingColumn + 1 == endingColumn || startingColumn - 1 == endingColumn)
                && (startingRow + 2 == endingRow || startingRow - 2 == endingRow))
            return true;

        if ((startingColumn + 2 == endingColumn || startingColumn - 2 == endingColumn)
                && (startingRow + 1 == endingRow || startingRow - 1 == endingRow))
            return true;

        return false;
    }
}
