package services.handlers.bank.update;

import entities.bank.IBank;
import exceptions.*;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.*;

public class DpsInterestsHandler extends BankHavingHandler {
    public DpsInterestsHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "dps_interests - set new interests for deposit accounts";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "dps_interests")) {
            output.print("Enter the number of deposit options : ");
            int numberOfOptions = input.nextInt();
            input.nextLine();

            Map<Double, Double> depositInterests = new HashMap<>();

            for (int i = 0; i < numberOfOptions; ++i) {
                output.print("Enter deposit amount for option " + (i + 1) + " : ");
                double depositAmount = input.nextDouble();

                output.print("Enter deposit interest for option " + (i + 1) + " : ");
                double depositInterest = input.nextDouble();

                depositInterests.put(depositAmount, depositInterest);
            }

            bank.updateInterestForDeposit(depositInterests);
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}