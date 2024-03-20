package services.handlers.bank.application.create;

import entities.bank.IApplication;
import exceptions.*;
import services.windows.CreateWindow;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.ApplicationHavingHandler;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class CreateHandler extends ApplicationHavingHandler {
    public CreateHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "create - create account";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (app == null) {
            throw new NullPointerException(app.toString());
        }

        if (Objects.equals(iterator.next(), "create")) {
            new CreateWindow(input, output, app).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}