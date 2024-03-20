package services.handlers.bank.machine;

import entities.bank.IBank;
import exceptions.*;
import services.applications.MachineApplication;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class MachineHandler extends BankHavingHandler {
    public MachineHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "machine - ask bank machine";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (bank == null) {
            throw new NullPointerException(bank.toString());
        }

        if (Objects.equals(iterator.next(), "machine")) {
            new MachineApplication(input, output, bank).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
