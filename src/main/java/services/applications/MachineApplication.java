package services.applications;

import entities.bank.IBank;
import services.handlers.MainHandler;
import services.handlers.bank.machine.DpsHandler;
import services.handlers.bank.machine.WtdHandler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MachineApplication extends UserApplication {
    public MachineApplication(Scanner input, PrintStream output, IBank bank) {
        super(input, output);
        if (bank == null) {
            throw new NullPointerException(bank.toString());
        }

        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
                new DpsHandler(this.input, this.output, bank),
                new WtdHandler(this.input, this.output, bank)
        )));
    }
}