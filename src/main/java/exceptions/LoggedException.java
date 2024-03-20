package exceptions;

public class LoggedException extends Exception {
    public LoggedException() {
        super("User already logged in");
    }
}