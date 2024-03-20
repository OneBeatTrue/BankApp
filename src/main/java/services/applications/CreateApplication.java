package services.applications;

import entities.bank.IApplication;
import services.handlers.MainHandler;
import services.handlers.bank.application.create.CreditHandler;
import services.handlers.bank.application.create.DebitHandler;
import services.handlers.bank.application.create.DepositHandler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CreateApplication extends UserApplication {
    public CreateApplication(Scanner input, PrintStream output, IApplication application) {
        super(input, output);
        if (application == null) {
            throw new NullPointerException(application.toString());
        }

        this.application = application;
        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
            new CreditHandler(this.input, this.output, this.application),
                new DebitHandler(this.input, this.output, this.application),
                new DepositHandler(this.input, this.output, this.application)
        )));
    }

    private IApplication application;
}