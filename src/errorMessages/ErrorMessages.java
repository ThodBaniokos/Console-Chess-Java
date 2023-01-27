package errorMessages;

public class ErrorMessages {
    public static final String invalidLocationErrorMessage = ": Not a valid move\nA valid move is four characters long, with a column range between [a, h] and a row range between [1, 8] (ex. a2a3)";
    public static final String noPieceInFromLocationErrorMessage = "There is not a piece present in the selected starting location : ";
    public static final String movingOpposingPlayersPieceErrorMessage = "Trying to move an opposing player's piece";
    public static final String cannotStepOnYourPiecesErrorMessage = "Cannot move your piece on top of another one of your pieces";
    public static final String noInputGivenErrorMessage = "No input text given, please type a move or a command\nFor help type the command :h";
    public static final String cannotStepOverOtherPieces = " cannot step over other pieces";
    public static final String sameLocationErrorMessage = " : starting and ending location are the same, please retry with a valid move";
}
