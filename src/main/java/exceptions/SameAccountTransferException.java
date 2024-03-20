package exceptions;

public class SameAccountTransferException extends Exception {
    public SameAccountTransferException() {
        super("Unable to transfer between same account");
    }
}