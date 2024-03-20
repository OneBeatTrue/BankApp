package services.handlers.bank;

import entities.bank.IBank;
import exceptions.*;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class CancelHandler extends BankHavingHandler {
    public CancelHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "cancel [transaction_id] - cancel transaction";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "cancel")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            try {
                bank.cancelTransaction(UUID.fromString(iterator.next()));
                output.print("Cancelled");
                input.nextLine();
            } catch (IllegalArgumentException e) {
                output.println("Incorrect UUID");
            }
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}