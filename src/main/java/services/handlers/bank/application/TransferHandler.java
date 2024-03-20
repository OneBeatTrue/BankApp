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

public class TransferHandler extends ApplicationHavingHandler {
    public TransferHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output, app);
        help = "transfer [amount] - transfer money";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "transfer")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            double amount = Double.parseDouble(iterator.next());

            output.println("Enter account number : ");
            long account = input.nextLong();

            output.println("Enter PIN code : ");
            int pin = input.nextInt();

            output.println("Enter receiver account number : ");
            long accountReceiver = input.nextLong();

            app.transfer(account, pin, accountReceiver, amount);
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}