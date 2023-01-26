// import enums.*;
import board.Board;
import game.Game;
import location.*;

public class Main {
    public static void main(String[] args) throws Exception {

        Location loc = new Location(1, 1);
        Location loc2 = new Location("a2");
        Board gameBoard = new Board();
        Game game = new Game(gameBoard);

        // System.out.println(loc.toString());
        // System.out.println(loc2.toString());
        // System.out.println(gameBoard.toString());

        // System.out.println(gameBoard.getPieceAt(new Location(1, 1)).toString());
        // System.out.println(gameBoard.freeHorizontalPath(new Location("a3"), new Location("e3")));
        // System.out.println(gameBoard.freeVerticalPath(new Location("a2"), new Location("a6")));
        // System.out.println(gameBoard.freeDiagonalPath(new Location("a2"), new Location("c4")));
        // System.out.println(gameBoard.freeDiagonalPath(new Location("e7"), new Location("b4")));
        // System.out.println(gameBoard.freeAntidiagonalPath(new Location("b6"), new Location("f2")));
        // System.out.println(gameBoard.freeAntidiagonalPath(new Location("d3"), new Location("a6")));
        game.play();
    }
}
