package services.handlers.bank.machine;

import entities.bank.IBank;
import exceptions.*;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class DpsHandler extends BankHavingHandler {
    public DpsHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "dps [amount] - deposit money";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "dps")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            double amount = Double.parseDouble(iterator.next());

            output.println("Enter account number : ");
            bank.machine().deposit(input.nextLong(), amount);
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}