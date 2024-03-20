package services.handlers;

import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;

public abstract class Handler implements IHandler {
    protected Handler(Scanner input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    protected PrintStream output;
    protected Scanner input;
    protected IHandler next;
    protected String help;

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (next == null) {
            throw new UnknownCommandException(iterator.next());
        }

        next.handle(iterator);
    }

    @Override
    public IHandler setNext(IHandler next) {
        this.next = next;
        return this;
    }

    @Override
    public void help() {
        if (help != null && !help.isEmpty()) {
            output.println(help);
        }

        if (next != null) {
            next.help();
        }
    }
}
