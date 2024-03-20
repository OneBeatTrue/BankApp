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

public class DepositHandler extends ApplicationHavingHandler {
    public DepositHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "deposit [PIN code] - create deposit account with [PIN code]";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "deposit")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            int pinCode = Integer.parseInt(iterator.next());
            output.print("Enter start balance : ");
            app.createDepositAccount(pinCode, input.nextDouble());
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}