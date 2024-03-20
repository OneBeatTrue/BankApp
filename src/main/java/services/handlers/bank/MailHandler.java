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

public class MailHandler extends BankHavingHandler {
    public MailHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "mail - send message to all subscribed customers";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "mail")) {
            output.println("Enter mail : ");
            StringBuilder result = new StringBuilder();
            while (true) {
                String line = input.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                result.append(line).append("\n");
            }
            bank.mailSubs(result.toString());
            output.print("Success");
            input.nextLine();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}
