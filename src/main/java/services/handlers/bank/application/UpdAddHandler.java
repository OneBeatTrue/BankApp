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

public class UpdAddHandler extends ApplicationHavingHandler {
    public UpdAddHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "upd_add - update address";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "upd_add")) {
            output.print("Enter address : ");
            app.updateAddress(input.nextLine());
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}