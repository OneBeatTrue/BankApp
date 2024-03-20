package services.windows;

import entities.bank.IApplication;
import services.handlers.MainHandler;
import services.handlers.bank.application.*;
import services.handlers.bank.application.create.CreateHandler;

import java.io.PrintStream;
import java.util.*;

public class ApplicationWindow extends Window {
    public ApplicationWindow(Scanner input, PrintStream output, IApplication application) {
        super(input, output);
        if (application == null) {
            throw new NullPointerException(application.toString());
        }

        Application = application;
        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
            new TransferHandler(this.input, this.output, Application),
                new SubMailHandler(this.input, this.output, Application),
                new UnsubMailHandler(this.input, this.output, Application),
                new EnabNotHandler(this.input, this.output, Application),
                new DisNotHandler(this.input, this.output, Application),
                new UpdAddHandler(this.input, this.output, Application),
                new UpdPassHandler(this.input, this.output, Application),
                new AccListHandler(this.input, this.output, Application),
                new NotListHandler(this.input, this.output, Application),
                new TrListHandler(this.input, this.output, Application),
                new CreateHandler(this.input, this.output, Application)
        )));
    }

    private IApplication Application;
}
