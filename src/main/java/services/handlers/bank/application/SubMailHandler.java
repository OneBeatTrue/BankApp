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

public class SubMailHandler extends ApplicationHavingHandler {
    public SubMailHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "sub_mail - subscribe to bank mailing";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "sub_mail")) {
            app.subMailing();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}