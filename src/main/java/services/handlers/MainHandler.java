package services.handlers;

import exceptions.*;
import services.applications.exits.IExit;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Scanner;

public class MainHandler  extends Handler {
    public MainHandler(Scanner input, PrintStream output, IExit exit, Iterable<IHandler> handlers) {
        super(input, output);
        IHandler help = new HelpHandler(input, output, this);
        IHandler current = help;
        for (IHandler handler : handlers) {
            if (handler == null) {
                throw new NullPointerException(handler.toString());
            }

            current.setNext(handler);
            current = handler;
        }

        current.setNext(new QuitHandler(this.input, this.output, exit));
        commandHandler = help;
    }

    private IHandler commandHandler;

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        commandHandler.handle(iterator);
    }

    @Override
    public void help() {
        if (commandHandler != null) {
            commandHandler.help();
        }
    }
}
