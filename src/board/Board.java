package board;

import location.Location;
import piece.*;

import enums.*;

public class Board {

    private final int boardRows = 8;
    private final int boardColumns = 8;

    public Piece[][] board;

    public Board() {

        this.init();
    }

    public void init() {

        // initiaze empty board of pieces
        this.board = new Piece[this.boardRows][this.boardColumns];

        // create the white pieces first
        for (int i = 0; i < 8; i++) {
            this.board[1][i] = new Pawn(Color.WHITE, new Location(2, i + 1), this);
        }

        this.board[0][0] = new Rook(Color.WHITE, new Location(1, 1), this);
        this.board[0][1] = new Knight(Color.WHITE, new Location(1, 2), this);
        this.board[0][2] = new Bishop(Color.WHITE, new Location(1, 3), this);
        this.board[0][3] = new Queen(Color.WHITE, new Location(1, 4), this);
        this.board[0][4] = new King(Color.WHITE, new Location(1, 5), this);
        this.board[0][5] = new Bishop(Color.WHITE, new Location(1, 6), this);
        this.board[0][6] = new Knight(Color.WHITE, new Location(1, 7), this);
        this.board[0][7] = new Rook(Color.WHITE, new Location(1, 8), this);

        // create the black pieces
        for (int i = 0; i < 8; i++) {

            this.board[6][i] = new Pawn(Color.BLACK, new Location(7, i + 1), this);
        }

        this.board[7][0] = new Rook(Color.BLACK, new Location(1, 1), this);
        this.board[7][1] = new Knight(Color.BLACK, new Location(1, 2), this);
        this.board[7][2] = new Bishop(Color.BLACK, new Location(1, 3), this);
        this.board[7][3] = new Queen(Color.BLACK, new Location(1, 4), this);
        this.board[7][4] = new King(Color.BLACK, new Location(1, 5), this);
        this.board[7][5] = new Bishop(Color.BLACK, new Location(1, 6), this);
        this.board[7][6] = new Knight(Color.BLACK, new Location(1, 7), this);
        this.board[7][7] = new Rook(Color.BLACK, new Location(1, 8), this);
    }

    public void movePiece(Location from, Location to) {

        Piece movingPiece = this.getPieceAt(from);

        this.board[from.getRow()][from.getCol()] = null;

        this.board[to.getRow()][to.getCol()] = movingPiece;

        movingPiece.location = to;
    }

    public void movePieceCapturing(Location from, Location to) {

        this.board[to.getRow()][to.getCol()] = null;

        Piece movingPiece = this.getPieceAt(from);

        this.board[from.getRow()][from.getCol()] = null;

        this.board[to.getRow()][to.getCol()] = movingPiece;
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
            biggerColumn = to.getCol();
        } else {
            smallestColumn = to.getCol();
            biggerColumn = from.getCol() + 1;
        }

        for (int i = smallestColumn; i < biggerColumn; i++) {

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
            biggerRow = to.getRow();
        } else {
            smallestRow = to.getRow();
            biggerRow = from.getRow() - 1;
        }

        for (int i = smallestRow; i < biggerRow; i++) {

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

            while (tempStartRow >= tempEndRow && tempStartCol >= tempEndCol) {

                if (this.board[tempStartRow--][tempStartCol--] != null) {
                    return false;
                }
            }
        } else {

            int tempStartRow = from.getRow() + 1;
            int tempStartCol = from.getCol() + 1;

            int tempEndRow = to.getRow();
            int tempEndCol = to.getCol();

            while (tempStartRow <= tempEndRow && tempStartCol <= tempEndCol) {

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

            while (tempStartRow <= tempEndRow && tempStartCol >= tempEndCol) {

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

            while (tempStartRow >= tempEndRow && tempStartCol <= tempEndCol) {

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
}
