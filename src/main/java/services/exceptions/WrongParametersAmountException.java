package services.exceptions;

public class WrongParametersAmountException extends Exception {
    public WrongParametersAmountException() {
        super("Wrong amount of parameters was given");
    }
}
