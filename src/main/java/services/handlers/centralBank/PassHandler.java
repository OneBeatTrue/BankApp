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

public class PassHandler extends CentralBankHavingHandler {
    public PassHandler(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output, centralBank);
        help = "pass [days] - pass some days";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "pass")) {
            if (!iterator.hasNext()) {
                throw new WrongParametersAmountException();
            }

            centralbank.passDays(Integer.parseInt(iterator.next()));
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
