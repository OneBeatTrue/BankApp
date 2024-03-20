package services.handlers;

import entities.bank.IBank;
import java.io.PrintStream;
import java.util.Scanner;

public abstract class BankHavingHandler extends Handler {
    protected BankHavingHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output);
        if (bank == null) {
            throw new NullPointerException(bank.toString());
        }

        this.bank = bank;
    }

    protected IBank bank;
}
