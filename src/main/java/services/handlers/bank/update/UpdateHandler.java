package services.handlers.bank.update;

import entities.bank.IBank;
import exceptions.*;
import services.windows.UpdateWindow;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class UpdateHandler extends BankHavingHandler {
    public UpdateHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "update - update terms for accounts";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "update")) {
            if (bank == null) {
                throw new NullPointerException(bank.toString());
            }

            new UpdateWindow(input, output, bank).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}