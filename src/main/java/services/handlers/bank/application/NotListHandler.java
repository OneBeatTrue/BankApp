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

public class NotListHandler extends ApplicationHavingHandler {
    public NotListHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "not_list - show all notifications";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "not_list")) {
            for (String str : app.getNotifications()) {
                output.println(str);
            }
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}