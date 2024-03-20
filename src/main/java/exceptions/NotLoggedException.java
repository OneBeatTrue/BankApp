package exceptions;

public class NotLoggedException extends Exception {
    public NotLoggedException() {
        super("User not logged in");
    }
}