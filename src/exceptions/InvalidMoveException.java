package exceptions;

public class InvalidMoveException extends Exception {

    public InvalidMoveException(String errorMessage) {

        super(errorMessage + "\n");
    }
}
