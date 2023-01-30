package location;

/**
 * Class Location, part of package location
 * Represents a chess board point, (i.e. 1 of 64 squares)
 * all coded to coordinates using a row / column system
 * rows are between 1 and 8
 * columns are between a and h
 */
public class Location {

    private int column;
    private int row;
    // private String stringRepresentation;

    private final String stringColumnRepresentation = "abcdefgh";

    /**
     * Constructor accepting two integers between [1, 8]
     * actual values that are stored are between [0, 7]
     * cannot throw exception
     *
     * @param r row of the chess board
     * @param c column of the chess board
     */
    public Location(int r, int c) {

        // set the row
        this.row = (r != 0) ? r - 1 : r;

        // set the column
        this.column = (c != 0) ? c - 1 : c;

    }

    /**
     * Constructor accepting a string of starting location of the form "a1"
     * length is always two but the input string might not be acceptible
     *
     * @param loc represents a location on the chess board
     */
    public Location(String loc) {

        // get the column from the location string
        char column = loc.charAt(0);

        // get the row from the location string
        char row = loc.charAt(1);

        // check if the column character is valid
        if (this.stringColumnRepresentation.indexOf(column) != -1) {

            // System.out.println("valid column");

            this.column = this.stringColumnRepresentation.indexOf(column);
        }

        // check if row is valid
        if (row >= '1' && row <= '8') {

            // System.out.println("valid row");

            this.row = Integer.parseInt("" + row) - 1;
        }
    }

    /**
     * Public getter for the row of the current location
     *
     * @return integer representing the row of the location
     */
    public int getRow() {

        // return the row
        return this.row;
    }

    /**
     * Public getter for the column of the current location
     *
     * @return integer representing the column of the location
     */
    public int getCol() {

        // return the column
        return this.column;
    }

    /**
     * override of toString method for class Location
     *
     * @return the location in the form of "a1"
     */
    @Override
    public String toString() {

        return "" + this.stringColumnRepresentation.charAt(column) + Integer.toString(this.row + 1);
    }
}
