package exceptions;

public class WrongPinCodeException extends Exception {
    public WrongPinCodeException() {
        super("Wrong PIN-code");
    }
}