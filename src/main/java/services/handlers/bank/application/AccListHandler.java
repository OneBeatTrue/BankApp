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

public class AccListHandler extends ApplicationHavingHandler {
    public AccListHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "acc_list - show all accounts";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "acc_list")) {
            for (String str : app.getAccountsList()) {
                output.println(str);
            }
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}