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

public class FeeHandler extends BankHavingHandler {
    public FeeHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "fee [fee] - set new fee for credit accounts";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "fee")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            bank.updateFeeForCredit(Double.parseDouble(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}