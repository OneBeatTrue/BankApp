package services.handlers;

import entities.bank.IApplication;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class ApplicationHavingHandler extends Handler {
    protected ApplicationHavingHandler(Scanner input, PrintStream output, IApplication app) {
        super(input, output);
        if (app == null) {
            throw new NullPointerException(app.toString());
        }

        this.app = app;
    }

    protected IApplication app;
}
