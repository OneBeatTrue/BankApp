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

public class WtdHandler extends BankHavingHandler {
    public WtdHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "wtd [amount] - withdrawal money";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "wtd")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            double amount = Double.parseDouble(iterator.next());

            output.println("Enter account number : ");
            long account = input.nextLong();

            output.println("Enter PIN code : ");
            bank.machine().withdraw(account, input.nextInt(), amount);
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}