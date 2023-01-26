// import enums.*;
import board.Board;
import game.Game;

public class Main {
    public static void main(String[] args) throws Exception {

        Board gameBoard = new Board();
        Game game = new Game(gameBoard);
        game.play();
    }
}
