package services.handlers.bank.application;

import entities.bank.IApplication;
import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.ApplicationHavingHandler;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class UnsubMailHandler extends ApplicationHavingHandler {
    public UnsubMailHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "unsub_mail - unsubscribe from bank mailing";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "unsub_mail")) {
            app.unsubMailing();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}