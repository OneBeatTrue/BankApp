package services.windows;

import entities.bank.IBank;
import services.handlers.MainHandler;
import services.handlers.bank.update.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UpdateWindow extends Window {
    public UpdateWindow(Scanner input, PrintStream output, IBank bank) {
        super(input, output);
        if (bank == null) {
            throw new NullPointerException(bank.toString());
        }

        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
                new UvLimitHandler(this.input, this.output, bank),
                new DpsInterestsHandler(this.input, this.output, bank),
                new DbtInterestHandler(this.input, this.output, bank),
                new FeeHandler(this.input, this.output, bank),
                new CrLimitHandler(this.input, this.output, bank)
        )));
    }
}