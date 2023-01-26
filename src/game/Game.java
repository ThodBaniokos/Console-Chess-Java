package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    private LinkedHashMap<Color, List<Pair<Location, Location>>> movesMade;

    // file containing instruction on how to play the game
    private File helpFile = new File("src/help/help.txt");

    private BufferedReader loadedReader;

    private BufferedReader inputStream;

    private Color playingColor = Color.WHITE;

    private LinkedHashMap<Piece, Boolean> hasPawnMoved;

    private final String invalidLocationErrorMessage = ": Not a valid move\nA valid move is four characters long, with a column range between [a, h] and a row range between [1, 8] (ex. a2a3)";
    private final String noPieceInFromLocationErrorMessage = "There is not a piece present in the selected starting location : ";
    private final String movingOpposingPlayersPiece = "Trying to move a " + this.playingColor.nextColor()
            + " piece when it is " + this.playingColor + "'s turn";

    public Game(Board board) throws FileNotFoundException {

        this.gameBoard = board;

        this.movesMade = new LinkedHashMap<>();

        this.movesMade.put(Color.WHITE, new ArrayList<Pair<Location,Location>>());
        this.movesMade.put(Color.BLACK, new ArrayList<Pair<Location,Location>>());

        this.hasPawnMoved = new LinkedHashMap<>();

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

                userInput = userInput.trim();

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

    private int chebyshevDistance(Location from, Location to) {

        return Math.max(Math.abs(from.getRow() - to.getRow()), Math.abs(from.getCol() - to.getCol()));
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

        Piece movingPiece = this.gameBoard.getPieceAt(new Location(fromLocation));

        if (movingPiece == null) {

            throw new InvalidMoveException(this.noPieceInFromLocationErrorMessage + fromLocation);
        }

        if (movingPiece.color != this.playingColor) {
            throw new InvalidMoveException(this.movingOpposingPlayersPiece);
        }

        Location fLocation = new Location(fromLocation);
        Location tLocation = new Location(toLocation);

        int dist = this.chebyshevDistance(fLocation, tLocation);

        if (movingPiece instanceof Pawn) {

            boolean isFirstPawnMove = (hasPawnMoved.get(movingPiece) == null);

            if (isFirstPawnMove == true && dist > 2)
                throw new InvalidMoveException("Pawn piece cannot move more than two points on it's first move");

            if (isFirstPawnMove == false && dist > 1)
                throw new InvalidMoveException("Pawn piece cannot move more than one point on it's move");

            if (fLocation.getCol() != tLocation.getCol() && this.gameBoard.getPieceAt(tLocation) == null)
                throw new InvalidMoveException("Given move of Pawn piece do not have the same column");

            if (this.gameBoard.freeVerticalPath(fLocation, tLocation) == false)
                throw new InvalidMoveException("Pawn piece cannot go over other pieces");

            hasPawnMoved.put(movingPiece, true);
        }

        movingPiece.moveTo(tLocation);

        movesMade.get(this.playingColor).add(new Pair<Location,Location>(fLocation, tLocation));

        this.playingColor = this.playingColor.nextColor();

        return;
    }

    private void openGame() throws IOException, InvalidLocationException, InvalidMoveException {

        System.out.println("Type the name of the save file you want to load (without the file extension)");

        String userInput = this.inputStream.readLine();

        BufferedReader savefileReader = new BufferedReader(new FileReader("src/saves/" + userInput + ".txt"));
        String fileLine = savefileReader.readLine();

        while (fileLine != null) {

            String aString[] = fileLine.split(", ", 2);

            this.handleInput(aString[0] + aString[1]);

            fileLine = savefileReader.readLine();
        }

        savefileReader.close();

        return;
    }

    private void saveGame() throws IOException {

        System.out.println("Type the name of the save file (without the file extension)");

        String userInput = this.inputStream.readLine();

        File saveFile = new File("src/saves/" + userInput + ".txt");

        saveFile.createNewFile();

        FileWriter fileWriter = new FileWriter(saveFile);

        Color color = Color.WHITE;

        int moveNumber = 0;

        while (moveNumber < Math.max(this.movesMade.get(Color.WHITE).size(), this.movesMade.get(Color.BLACK).size())) {


            if (this.movesMade.get(Color.BLACK).size() <= moveNumber && color == Color.BLACK) {
                moveNumber++;
                continue;
            }

            Pair<Location, Location> locationPair = this.movesMade.get(color).get(moveNumber);

            String toWrite = locationPair.firstObj.toString() + locationPair.secondObj.toString() + "\n";

            fileWriter.write(toWrite);

            color = color.nextColor();

            if (color == Color.WHITE) moveNumber++;
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
