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

public class DebitHandler extends ApplicationHavingHandler {
    public DebitHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "debit [PIN code] - create debit account with [PIN code]";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "debit")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            app.createDebitAccount(Integer.parseInt(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}