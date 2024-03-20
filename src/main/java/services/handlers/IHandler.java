package services.handlers;

import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.util.Iterator;
import java.util.ListIterator;

public interface IHandler {
    void help();
    void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException;
    IHandler setNext(IHandler next);
}
