package services.handlers.bank.update;

import entities.bank.IBank;
import exceptions.*;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class DbtInterestHandler extends BankHavingHandler {
    public DbtInterestHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "dbt_interest [interest] - set new interest for debit accounts";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "dbt_interest")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            bank.updateInterestForDebit(Double.parseDouble(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}