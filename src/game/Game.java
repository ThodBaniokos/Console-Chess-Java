package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Pair.Pair;
import board.Board;
import enums.Color;
import exceptions.GameoverException;
import exceptions.InvalidLocationException;
import exceptions.InvalidMoveException;
import exceptions.NoInputGivenException;
import location.Location;
// import piece.Bishop;
import piece.King;
// import piece.Knight;
// import piece.Pawn;
import piece.Piece;
// import piece.Queen;
// import piece.Rook;
import errorMessages.ErrorMessages;

public class Game {

    private Board board;

    // list to save the moves
    private List<Pair<Location, Location>> movesMade;

    // file containing instruction on how to play the game
    private File helpFile = new File("src/help/help.txt");

    private BufferedReader helpFileReader;

    private BufferedReader inputStream;

    private Color playingColor = Color.WHITE;

    // private boolean check;

    public Game(Board board) throws FileNotFoundException {

        this.board = board;

        this.movesMade = new ArrayList<Pair<Location, Location>>();

        this.helpFileReader = new BufferedReader(new FileReader(this.helpFile));

        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
    }

    public void play() throws IOException {

        String userInput;

        boolean isRunning = true;

        System.out.println("New game started, you can also load a saved game using the command :o");

        boolean printGameState = true;

        // this.check = false;

        while (isRunning) {

            try {

                if (printGameState == true) {

                    System.out.println(board.toString());
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

        Piece movingPiece = this.board.getPieceAt(fLocation);

        if (movingPiece == null) {

            throw new InvalidMoveException(ErrorMessages.noPieceInFromLocationErrorMessage + fLocation.toString());
        }

        Piece landingSquarePiece = this.board.getPieceAt(tLocation);

        if (landingSquarePiece != null && movingPiece.color == landingSquarePiece.color) {

            throw new InvalidMoveException(ErrorMessages.cannotStepOnYourPiecesErrorMessage);
        }

        if (movingPiece.color != this.playingColor) {
            throw new InvalidMoveException(ErrorMessages.movingOpposingPlayersPieceErrorMessage);
        }

        movingPiece.moveTo(tLocation);

        if (movingPiece instanceof King) {

            if (movingPiece.color == Color.WHITE)
                this.board.whiteKingLocation = tLocation;
            else
                this.board.blackKingLocation = tLocation;
        }

        movesMade.add(new Pair<Location, Location>(fLocation, tLocation));

        this.playingColor = this.playingColor.nextColor();

        return;
    }

    private void openGame() throws IOException, InvalidLocationException, InvalidMoveException, NoInputGivenException,
            GameoverException {

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

        for (Pair<Location, Location> locationPair : movesMade) {

            String toWrite = locationPair.firstObj.toString() + ", " + locationPair.secondObj.toString() + "\n";

            fileWriter.write(toWrite);
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

    // private boolean sameDiagonal(Piece piece1, Piece piece2) {

    //     if (Math.abs(piece1.location.getRow() - piece2.location.getRow()) != Math
    //             .abs(piece1.location.getCol() - piece2.location.getCol()))
    //         return false;

    //     return true;
    // }

    // private boolean isCheck(Piece movedPiece) {

    //     Piece kingPiece;

    //     if (this.playingColor == Color.WHITE)
    //         kingPiece = this.board.getPieceAt(this.board.blackKingLocation);
    //     else
    //         kingPiece = this.board.getPieceAt(this.board.whiteKingLocation);

    //     if (movedPiece instanceof Bishop) {

    //         if (sameDiagonal(movedPiece, kingPiece) == false)
    //             return false;

    //         if (movedPiece.location.getCol() < kingPiece.location.getCol())
    //             return this.board.freeDiagonalPath(movedPiece.location, kingPiece.location);
    //         else
    //             return this.board.freeAntidiagonalPath(movedPiece.location, kingPiece.location);

    //     } else if (movedPiece instanceof Knight) {

    //         return this.board.knightMoveCheck(movedPiece.location.getCol(),
    //                 kingPiece.location.getCol(), movedPiece.location.getRow(), kingPiece.location.getRow());

    //     } else if (movedPiece instanceof Pawn) {

    //         if (sameDiagonal(movedPiece, kingPiece) == false)
    //             return false;

    //         if (this.board.chebyshevDistance(movedPiece.location, kingPiece.location) == 1)
    //             return true;

    //     } else if (movedPiece instanceof Queen) {

    //         if (sameDiagonal(movedPiece, kingPiece) == true) {

    //             if (movedPiece.location.getCol() < kingPiece.location.getCol())
    //                 return this.board.freeDiagonalPath(movedPiece.location, kingPiece.location);
    //             else
    //                 return this.board.freeAntidiagonalPath(movedPiece.location, kingPiece.location);
    //         }

    //         if (movedPiece.location.getCol() == kingPiece.location.getCol())
    //             return this.board.freeVerticalPath(movedPiece.location, kingPiece.location);

    //         if (movedPiece.location.getRow() == kingPiece.location.getRow())
    //             return this.board.freeHorizontalPath(movedPiece.location, kingPiece.location);

    //     } else if (movedPiece instanceof Rook) {

    //         if (movedPiece.location.getCol() == kingPiece.location.getCol())
    //             return this.board.freeVerticalPath(movedPiece.location, kingPiece.location);

    //         if (movedPiece.location.getRow() == kingPiece.location.getRow())
    //             return this.board.freeHorizontalPath(movedPiece.location, kingPiece.location);

    //     }

    //     return false;
    // }

    // List<Location> generatePossibleKingMoves(Piece kingPiece) {

    //     int kingPieceRow = kingPiece.location.getRow() + 1;
    //     int kingPieceCol = kingPiece.location.getCol() + 1;

    //     List<Location> possibleMoves = new ArrayList<>();

    //     // down
    //     if (kingPieceRow - 1 >= 1 && kingPieceRow - 1 <= 8)
    //         possibleMoves.add(new Location(kingPieceRow - 1, kingPieceCol));

    //     // up
    //     if (kingPieceRow + 1 >= 1 && kingPieceRow + 1 <= 8)
    //         possibleMoves.add(new Location(kingPieceRow + 1, kingPieceCol));

    //     // left
    //     if (kingPieceCol - 1 >= 1 && kingPieceCol - 1 <= 8)
    //         possibleMoves.add(new Location(kingPieceRow, kingPieceCol - 1));

    //     // right
    //     if (kingPieceCol + 1 >= 1 && kingPieceCol + 1 <= 8)
    //         possibleMoves.add(new Location(kingPieceRow, kingPieceCol + 1));

    //     // diag right up
    //     if ((kingPieceRow + 1 >= 1 && kingPieceRow + 1 <= 8) && (kingPieceCol + 1 >= 1 && kingPieceCol + 1 <= 8))
    //         possibleMoves.add(new Location(kingPieceRow + 1, kingPieceCol + 1));

    //     // diag left up
    //     if ((kingPieceRow + 1 >= 1 && kingPieceRow + 1 <= 8) && (kingPieceCol - 1 >= 1 && kingPieceCol - 1 <= 8))
    //         possibleMoves.add(new Location(kingPieceRow + 1, kingPieceCol - 1));

    //     // diag right down
    //     if ((kingPieceRow - 1 >= 1 && kingPieceRow - 1 <= 8) && (kingPieceCol + 1 >= 1 && kingPieceCol + 1 <= 8))
    //         possibleMoves.add(new Location(kingPieceRow - 1, kingPieceCol + 1));

    //     // diag left down
    //     if ((kingPieceRow - 1 >= 1 && kingPieceRow - 1 <= 8) && (kingPieceCol - 1 >= 1 && kingPieceCol - 1 <= 8))
    //         possibleMoves.add(new Location(kingPieceRow - 1, kingPieceCol - 1));

    //     return possibleMoves;
    // }

    // private boolean isCheckmate(Piece movedPiece) {

    //     Piece kingPiece;

    //     boolean result = true;

    //     if (this.playingColor == Color.WHITE)
    //         kingPiece = this.board.getPieceAt(this.board.blackKingLocation);
    //     else
    //         kingPiece = this.board.getPieceAt(this.board.whiteKingLocation);

    //     List<Location> possibleKingMoves = generatePossibleKingMoves(kingPiece);

    //     Location kingPieceLocation = kingPiece.location;

    //     System.out.println(possibleKingMoves);

    //     for (Location possibleMove : possibleKingMoves) {

    //         Piece pieceAtPosMove = this.board.getPieceAt(possibleMove);

    //         if (pieceAtPosMove != null && pieceAtPosMove.color == kingPiece.color) {
    //             continue;
    //         }

    //         kingPiece.location = possibleMove;

    //         result = result && this.isCheck(movedPiece);

    //         if (result == false)
    //             break;
    //     }

    //     kingPiece.location = kingPieceLocation;

    //     return result;
    // }
}
