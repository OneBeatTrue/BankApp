package exceptions;

public class NotPositiveException extends Exception {
    public NotPositiveException(String name) {
        super(name + " value must be positive");
    }
}
