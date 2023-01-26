package exceptions;

public class InvalidLocationException extends Exception {

    public InvalidLocationException(String errorMessage) {
        super(errorMessage);
    }
}
