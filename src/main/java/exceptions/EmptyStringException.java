package exceptions;

public class EmptyStringException extends Exception {
    public EmptyStringException(String name) {
        super(name + " must not be empty");
    }
}
