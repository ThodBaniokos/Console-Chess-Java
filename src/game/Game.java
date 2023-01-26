package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import Pair.Pair;
import board.Board;
import enums.Color;
import exceptions.InvalidLocationException;
import exceptions.InvalidMoveException;
import location.Location;
import piece.Pawn;
import piece.Piece;

public class Game {

    private Board gameBoard;

    // linked hash map to preserve the order of moves made
    private LinkedHashMap<Color, Pair<Location, Location>> movesMade;

    // file containing instruction on how to play the game
    private File helpFile = new File("src/help/help.txt");

    private BufferedReader loadedReader;

    private BufferedReader inputStream;

    private Color playingColor = Color.WHITE;

    private final String invalidLocationErrorMessage = ": Not a valid move\nA valid move is four characters long, with a column range between [a, h] and a row range between [1, 8] (ex. a2a3)";
    private final String noPieceInFromLocationErrorMessage = "There is not a piece present in the selected starting location : ";
    private final String movingOpposingPlayersPiece = "Trying to move a " + this.playingColor.nextColor() + " piece when it is " + this.playingColor + "'s turn";

    public Game(Board board) throws FileNotFoundException {

        this.gameBoard = board;

        this.movesMade = new LinkedHashMap<>();

        this.loadedReader = new BufferedReader(new FileReader(this.helpFile));

        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
    }

    public void play() throws IOException {

        this.gameBoard.init();

        String userInput;

        boolean isRunning = true;

        while (isRunning) {

            try {
                System.out.println(gameBoard.toString());

                System.out.println("Type your next move or a command, for a list of commands type :h");
                userInput = this.inputStream.readLine();

                switch (userInput) {

                    case ":x":
                        isRunning = this.exitGame();
                        break;
                    case ":h":
                        this.printHelp();
                        break;
                    case ":s":
                        this.saveGame();
                        break;
                    case ":o":
                        this.openGame();
                        break;
                    default:
                        this.handleInput(userInput);
                        break;
                }
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                // ioException.printStackTrace();
            } catch (InvalidLocationException invalidLocException) {
                System.out.println();
                System.out.println(invalidLocException.getMessage());
                System.out.println();
                // invalidLocException.printStackTrace();
            } catch (InvalidMoveException invalidmoveException) {
                System.out.println();
                System.out.println(invalidmoveException.getMessage());
                System.out.println();
                // invalidmoveException.printStackTrace();
            }
        }
    }

    private boolean isInBoundsLetter(char c) {

        if (c >= 'a' && c <= 'h')
            return true;

        return false;
    }

    private boolean isInBoundsNumber(char c) {

        if (c >= '1' && c <= '8')
            return true;

        return false;
    }

    private boolean isValidInputMove(String moveString) {

        if (moveString.length() != 4)
            return false;

        if (this.isInBoundsLetter(moveString.charAt(0)) == false)
            return false;

        if (this.isInBoundsLetter(moveString.charAt(2)) == false)
            return false;

        if (this.isInBoundsNumber(moveString.charAt(1)) == false)
            return false;

        if (this.isInBoundsNumber(moveString.charAt(3)) == false)
            return false;

        return true;
    }

    private void handleInput(String moveString) throws InvalidLocationException, InvalidMoveException {

        if (this.isValidInputMove(moveString) == false) {

            throw new InvalidLocationException(moveString + this.invalidLocationErrorMessage);
        }

        String fromLocation = moveString.substring(0, 2);
        String toLocation = moveString.substring(2, 4);

        Piece fromPiece = this.gameBoard.getPieceAt(new Location(fromLocation));

        if (fromPiece == null) {

            throw new InvalidMoveException(this.noPieceInFromLocationErrorMessage + fromLocation);
        }

        if (fromPiece.color != this.playingColor) {
            throw new InvalidMoveException(this.movingOpposingPlayersPiece);
        }

        // if (fromPiece instanceof Pawn) {

        // }

        return;
    }

    private void openGame() {

        return;
    }

    private void saveGame() throws IOException {

        System.out.println("Type the name of the save file");

        String userInput = this.inputStream.readLine();

        File saveFile = new File("src/saves/" + userInput + ".txt");

        saveFile.createNewFile();

        FileWriter fileWriter = new FileWriter(saveFile);

        for (Entry<Color, Pair<Location, Location>> move : this.movesMade.entrySet()) {

            fileWriter.write(move.getKey().toString() + " " + move.getValue().toString() + "\n");
        }

        fileWriter.close();

        return;
    }

    private boolean exitGame() throws IOException {

        System.out.println("Do you want to exit the game? (y/n)");

        String userInput = this.inputStream.readLine();

        if (userInput.toLowerCase().equals("n"))
            return true;

        return false;
    }

    private void printHelp() throws IOException {

        System.out.println();

        String fileLine = loadedReader.readLine();

        while (fileLine != null) {

            System.out.println(fileLine);

            fileLine = loadedReader.readLine();
        }

        System.out.println();

        return;
    }
}
