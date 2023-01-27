package exceptions;

public class NoInputGivenException extends Exception {

    public NoInputGivenException(String errorMessage) {

        super(errorMessage + "\n");
    }
}
