package services.handlers;

import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class HelpHandler extends Handler {
    public HelpHandler(Scanner input, PrintStream output, IHandler startPoint) {
        super(input, output);
        help = "help - to get help";
        this.startPoint = startPoint;
    }

    private IHandler startPoint;

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "help")) {
            startPoint.help();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
