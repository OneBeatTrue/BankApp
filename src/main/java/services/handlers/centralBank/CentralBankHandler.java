package services.handlers.centralBank;

import entities.centralBank.ICentralBank;
import exceptions.*;
import services.windows.CentralWindow;
import services.handlers.CentralBankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class CentralBankHandler extends CentralBankHavingHandler {
    public CentralBankHandler(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output, centralBank);
        help = "central - central bank command";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "central")) {
            new CentralWindow(input, output, centralbank).run();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
