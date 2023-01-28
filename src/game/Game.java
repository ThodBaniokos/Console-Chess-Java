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
import exceptions.GameoverException;
import exceptions.InvalidLocationException;
import exceptions.InvalidMoveException;
import exceptions.NoInputGivenException;
import location.Location;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;
import errorMessages.ErrorMessages;

public class Game {

    private Board gameBoard;

    // linked hash map to preserve the order of moves made
    private LinkedHashMap<Color, List<Pair<Location, Location>>> movesMade;

    // file containing instruction on how to play the game
    private File helpFile = new File("src/help/help.txt");

    private BufferedReader helpFileReader;

    private BufferedReader inputStream;

    private Color playingColor = Color.WHITE;

    private boolean check;

    public Game(Board board) throws FileNotFoundException {

        this.gameBoard = board;

        this.movesMade = new LinkedHashMap<>();

        this.movesMade.put(Color.WHITE, new ArrayList<Pair<Location, Location>>());
        this.movesMade.put(Color.BLACK, new ArrayList<Pair<Location, Location>>());

        this.helpFileReader = new BufferedReader(new FileReader(this.helpFile));

        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
    }

    public void play() throws IOException {

        this.gameBoard.init();

        String userInput;

        boolean isRunning = true;

        System.out.println("New game started, you can also load a saved game using the command :o");

        boolean printGameState = true;

        this.check = false;

        while (isRunning) {

            try {

                if (printGameState == true) {

                    System.out.println(gameBoard.toString());
                    System.out.println("Currently playing: " + this.playingColor);
                    System.out.println("Type your next move or a command, for a list of commands type :h");
                }

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
                        this.handleMove(userInput);
                        break;
                }
            } catch (IOException ioException) {

                System.out.println(ioException.getMessage());
            } catch (InvalidLocationException invalidLocException) {

                System.out.println(invalidLocException.getMessage());
            } catch (InvalidMoveException invalidmoveException) {

                System.out.println(invalidmoveException.getMessage());
            } catch (NoInputGivenException noInputException) {

                System.out.println(noInputException.getMessage());
            } catch (GameoverException gameOver) {

                System.out.println(gameOver.getMessage());
                isRunning = false;
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

    private void handleMove(String moveString)
            throws InvalidLocationException, InvalidMoveException, NoInputGivenException, GameoverException {

        if (moveString.length() == 0) {

            throw new NoInputGivenException(ErrorMessages.noInputGivenErrorMessage);
        }

        if (this.isValidInputMove(moveString) == false) {

            throw new InvalidLocationException(moveString + ErrorMessages.invalidLocationErrorMessage);
        }

        Location fLocation = new Location(moveString.substring(0, 2));
        Location tLocation = new Location(moveString.substring(2, 4));

        if (fLocation.toString().equals(tLocation.toString())) {

            throw new InvalidLocationException(moveString + ErrorMessages.sameLocationErrorMessage);
        }

        Piece movingPiece = this.gameBoard.getPieceAt(fLocation);

        if (movingPiece == null) {

            throw new InvalidMoveException(ErrorMessages.noPieceInFromLocationErrorMessage + fLocation.toString());
        }

        Piece landingSquarePiece = this.gameBoard.getPieceAt(tLocation);

        if (landingSquarePiece != null && movingPiece.color == landingSquarePiece.color) {

            throw new InvalidMoveException(ErrorMessages.cannotStepOnYourPiecesErrorMessage);
        }

        if (movingPiece.color != this.playingColor) {
            throw new InvalidMoveException(ErrorMessages.movingOpposingPlayersPieceErrorMessage);
        }

        movingPiece.moveTo(tLocation);

        if (movingPiece instanceof King) {

            if (movingPiece.color == Color.WHITE)
                this.gameBoard.whiteKingLocation = tLocation;
            else
                this.gameBoard.blackKingLocation = tLocation;
        }

        this.check = isCheck(movingPiece);

        System.out.println(this.check);

        if (this.check == true) {

            if (isCheckmate(movingPiece)) {
                throw new GameoverException("Game over, winner is : " + this.playingColor);
            }
        }

        movesMade.get(this.playingColor).add(new Pair<Location, Location>(fLocation, tLocation));

        this.playingColor = this.playingColor.nextColor();

        return;
    }

    private void openGame() throws IOException, InvalidLocationException, InvalidMoveException, NoInputGivenException, GameoverException {

        System.out.println("New game stopped, do you want to load a saved game? (y/n)");

        String userInput;

        do {
            userInput = this.inputStream.readLine();

            if (userInput.equals("y") == false && userInput.equals("n") == false) {

                System.out.println("Not acceptable answer, type 'y' for yes and 'n' for no");
            }

        } while (userInput.equals("y") == false && userInput.equals("n") == false);

        if (userInput.equals("n")) {

            return;
        }

        System.out.println("Type the name of the save file you want to load (without the file extension)");

        userInput = this.inputStream.readLine();

        BufferedReader savefileReader = new BufferedReader(new FileReader("src/saves/" + userInput + ".txt"));
        String fileLine = savefileReader.readLine();

        while (fileLine != null) {

            String aString[] = fileLine.split(", ", 2);

            this.handleMove(aString[0] + aString[1]);

            fileLine = savefileReader.readLine();
        }

        savefileReader.close();

        System.out.println("Loaded file: " + userInput + ".txt");

        return;
    }

    private void saveGame() throws IOException {

        System.out.println("Game stopped, do you want to save the game? (y/n)");

        String userInput;

        do {
            userInput = this.inputStream.readLine();

            if (userInput.equals("y") == false && userInput.equals("n") == false) {

                System.out.println("Not acceptable answer, type 'y' for yes and 'n' for no");
            }

        } while (userInput.equals("y") == false && userInput.equals("n") == false);

        if (userInput.equals("n")) {

            return;
        }

        System.out.println(
                "Type the name of the save file (without the file extension)\nIf you give an existing file the old one will be overwritten");

        userInput = this.inputStream.readLine();

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

            String toWrite = locationPair.firstObj.toString() + ", " + locationPair.secondObj.toString() + "\n";

            fileWriter.write(toWrite);

            color = color.nextColor();

            if (color == Color.WHITE)
                moveNumber++;
        }

        fileWriter.close();

        return;
    }

    private boolean exitGame() throws IOException {

        System.out.println("Do you want to exit the game? (y/n)");

        String userInput;

        do {
            userInput = this.inputStream.readLine();

            if (userInput.equals("y") == false && userInput.equals("n") == false) {

                System.out.println("Not acceptable answer, type 'y' for yes and 'n' for no");
            }

        } while (userInput.equals("y") == false && userInput.equals("n") == false);

        if (userInput.toLowerCase().equals("n"))
            return true;

        return false;
    }

    private void printHelp() throws IOException {

        System.out.println();

        String fileLine = helpFileReader.readLine();

        while (fileLine != null) {

            System.out.println(fileLine);

            fileLine = helpFileReader.readLine();
        }

        System.out.println();

        return;
    }

    private boolean sameDiagonal(Piece piece1, Piece piece2) {

        if (Math.abs(piece1.location.getRow() - piece2.location.getRow()) != Math
                .abs(piece1.location.getCol() - piece2.location.getCol()))
            return false;

        return true;
    }

    private boolean isCheck(Piece movedPiece) {

        Piece kingPiece;

        if (this.playingColor == Color.WHITE)
            kingPiece = this.gameBoard.getPieceAt(this.gameBoard.blackKingLocation);
        else
            kingPiece = this.gameBoard.getPieceAt(this.gameBoard.whiteKingLocation);

        if (movedPiece instanceof Bishop) {

            if (sameDiagonal(movedPiece, kingPiece) == false)
                return false;

            if (movedPiece.location.getCol() < kingPiece.location.getCol())
                return this.gameBoard.freeDiagonalPath(movedPiece.location, kingPiece.location);
            else
                return this.gameBoard.freeAntidiagonalPath(movedPiece.location, kingPiece.location);

        } else if (movedPiece instanceof Knight) {

            return this.gameBoard.knightMoveCheck(movedPiece.location.getCol(),
                    kingPiece.location.getCol(), movedPiece.location.getRow(), kingPiece.location.getRow());

        } else if (movedPiece instanceof Pawn) {

            if (sameDiagonal(movedPiece, kingPiece) == false)
                return false;

            if (this.gameBoard.chebyshevDistance(movedPiece.location, kingPiece.location) == 1)
                return true;

        } else if (movedPiece instanceof Queen) {

            if (sameDiagonal(movedPiece, kingPiece) == true) {

                if (movedPiece.location.getCol() < kingPiece.location.getCol())
                    return this.gameBoard.freeDiagonalPath(movedPiece.location, kingPiece.location);
                else
                    return this.gameBoard.freeAntidiagonalPath(movedPiece.location, kingPiece.location);
            }

            if (movedPiece.location.getCol() == kingPiece.location.getCol())
                return this.gameBoard.freeVerticalPath(movedPiece.location, kingPiece.location);

            if (movedPiece.location.getRow() == kingPiece.location.getRow())
                return this.gameBoard.freeHorizontalPath(movedPiece.location, kingPiece.location);

        } else if (movedPiece instanceof Rook) {

            if (movedPiece.location.getCol() == kingPiece.location.getCol())
                return this.gameBoard.freeVerticalPath(movedPiece.location, kingPiece.location);

            if (movedPiece.location.getRow() == kingPiece.location.getRow())
                return this.gameBoard.freeHorizontalPath(movedPiece.location, kingPiece.location);

        }

        return false;
    }

    private boolean isCheckmate(Piece movedPiece) {

        return false;
    }
}
