package services.handlers.bank;

import entities.centralBank.ICentralBank;
import exceptions.*;
import services.applications.BankApplication;
import services.handlers.CentralBankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class BankHandler extends CentralBankHavingHandler {
    public BankHandler(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output, centralBank);
        help = "bank [name] - bank named [name] command";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "bank")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            new BankApplication(input, output, centralbank.getBankByName(iterator.next())).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
