package services.handlers;

import exceptions.*;
import services.windows.exits.IExit;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class QuitHandler extends Handler {
    public QuitHandler(Scanner input, PrintStream output, IExit exit) {
        super(input, output);
        help = "quit - to leave";
        this.exit = exit;
    }

    private IExit exit;

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "quit")) {
            output.println("Goodbye!");
            exit.use();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}