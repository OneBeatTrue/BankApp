package services.handlers.bank;

import entities.bank.IBank;
import entities.transaction.ITransaction;
import exceptions.*;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.BankHavingHandler;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class AllTrHandler extends BankHavingHandler {
    public AllTrHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "all_tr - show all bank transactions";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "all_tr")) {
            for (ITransaction transaction : bank.getAllTransactions()) {
                output.println(transaction);
            }
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
