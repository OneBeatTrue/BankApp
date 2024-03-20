package services.handlers.centralBank;

import entities.centralBank.ICentralBank;
import exceptions.*;
import services.handlers.CentralBankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class BankListHandler extends CentralBankHavingHandler {
    public BankListHandler(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output, centralBank);
        help = "bank_list - show list of all added banks";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "bank_list")) {
            for (String str : centralbank.getBanksList()) {
                output.println(str);
            }
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
