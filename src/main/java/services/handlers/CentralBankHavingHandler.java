package services.handlers;

import entities.centralBank.ICentralBank;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class CentralBankHavingHandler extends Handler {
    protected CentralBankHavingHandler(Scanner input, PrintStream output, ICentralBank centralbank) {
        super(input, output);
        if (centralbank == null) {
            throw new NullPointerException(centralbank.toString());
        }

        this.centralbank = centralbank;
    }

    protected ICentralBank centralbank;
}
