package services.handlers.bank.application;

import entities.bank.IApplication;
import entities.bank.IBank;
import exceptions.*;
import services.windows.ApplicationWindow;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.BankHavingHandler;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class AppHandler extends BankHavingHandler {
    public AppHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "app [phone] - ask bank application and log in";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "app")) {
            if (bank == null) {
                throw new NullPointerException(bank.toString());
            }

            IApplication app = bank.application();
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            app.logIn(Long.parseLong(iterator.next()));
            new ApplicationWindow(input, output, app).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
