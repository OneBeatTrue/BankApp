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

public class TrListHandler extends ApplicationHavingHandler {
    public TrListHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "tr_list [account] - show all transactions of account";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "tr_list")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            app.getTransactionsList(Long.parseLong(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}