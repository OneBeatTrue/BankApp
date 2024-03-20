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

public class UpdPassHandler extends ApplicationHavingHandler {
    public UpdPassHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "upd_pass - update passport data";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "upd_pass")) {
            output.print("Enter passport series : ");
            int series = input.nextInt();

            output.print("Enter passport number : ");
            int number = input.nextInt();

            app.updatePassport(series, number);
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}