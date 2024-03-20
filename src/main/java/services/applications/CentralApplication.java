package services.applications;

import entities.centralBank.ICentralBank;
import services.handlers.MainHandler;
import services.handlers.centralBank.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CentralApplication extends UserApplication {
    public CentralApplication(Scanner input, PrintStream output, ICentralBank centralBank) {
        super(input, output);
        if (centralBank == null) {
            throw new NullPointerException(centralBank.toString());
        }

        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
                new BankListHandler(this.input, this.output, centralBank),
                new PassHandler(this.input, this.output, centralBank),
                new AccrueHandler(this.input, this.output, centralBank),
                new CreateBankHandler(this.input, this.output, centralBank)
        )));
    }
}
