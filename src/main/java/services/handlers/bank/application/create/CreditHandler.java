package services.handlers.bank.application.create;

import entities.bank.IApplication;
import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.ApplicationHavingHandler;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class CreditHandler extends ApplicationHavingHandler {
    public CreditHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "credit [PIN code] - create credit account with [PIN code]";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "credit")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            app.createCreditAccount(Integer.parseInt(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}