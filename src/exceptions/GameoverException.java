package exceptions;

public class GameoverException extends Exception {

    public GameoverException(String errorMessage) {

        super(errorMessage + "\n");
    }
}