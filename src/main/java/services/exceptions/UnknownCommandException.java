package services.exceptions;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String command) {
        super("Unknown command : " + command);
    }
}