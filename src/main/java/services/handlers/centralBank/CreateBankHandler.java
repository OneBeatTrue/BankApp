package services.handlers.centralBank;

import entities.centralBank.ICentralBank;
import exceptions.*;
import services.handlers.CentralBankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.*;

public class CreateBankHandler extends CentralBankHavingHandler {
    public CreateBankHandler(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output, centralBank);
        help = "create_bank - create bank and add to storage";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "create_bank")) {

            output.print("Enter bank name : ");
            String name = input.nextLine();

            output.print("Enter debit interest : ");
            double debitInterest = input.nextDouble();

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

            output.print("Enter deposit remaining : ");
            int depositRemaining = input.nextInt();

            output.print("Enter credit fee : ");
            double creditFee = input.nextDouble();

            output.print("Enter credit limit : ");
            double creditLimit = input.nextDouble();

            output.print("Enter unverified withdrawal limit : ");
            double unverifiedWithdrawalLimit = input.nextDouble();

            centralbank.createBank(name, debitInterest, depositInterests, depositRemaining, creditFee, creditLimit, unverifiedWithdrawalLimit);
            output.println("Success");
            input.nextLine();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
