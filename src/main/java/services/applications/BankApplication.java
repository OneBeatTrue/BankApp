package services.applications;

import entities.bank.IBank;
import services.handlers.MainHandler;
import services.handlers.bank.AllTrHandler;
import services.handlers.bank.CancelHandler;
import services.handlers.bank.CreateUserHandler;
import services.handlers.bank.MailHandler;
import services.handlers.bank.application.AppHandler;
import services.handlers.bank.machine.MachineHandler;
import services.handlers.bank.update.UpdateHandler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BankApplication extends UserApplication {
    public BankApplication(Scanner input, PrintStream output, IBank bank) {
        super(input, output);
        if (bank == null) {
            throw new NullPointerException(bank.toString());
        }

        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
                new CreateUserHandler(this.input, this.output, bank),
                new MailHandler(this.input, this.output, bank),
                new CancelHandler(this.input, this.output, bank),
                new UpdateHandler(this.input, this.output, bank),
                new MachineHandler(this.input, this.output, bank),
                new AppHandler(this.input, this.output, bank),
                new AllTrHandler(this.input, this.output, bank)
        )));
    }
}