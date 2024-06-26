package services.windows;

import exceptions.*;
import services.windows.exits.Exit;
import services.windows.exits.IExit;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;
import services.handlers.IHandler;

import java.io.PrintStream;
import java.util.*;

public abstract class Window implements IWindow {
    public Window(Scanner input, PrintStream output) {
        this.input = new Scanner(System.in);
        this.output = System.out;
        exit = new Exit();
    }

    protected IHandler handler;
    protected Scanner input;
    protected PrintStream output;
    protected IExit exit;

    @Override
    public void run() throws EmptyStringException, NotPositiveException {
        start();
        if (handler == null) {
            throw new NullPointerException(handler.toString());
        }

        while (true) {
            try {
                handler.handle(Arrays.asList(input.nextLine().split("\\s+")).listIterator());
            } catch (AccountNotFoundException |
                     CustomerNotFound |
                     EmptyStringException |
                     LoggedException |
                     TransactionNotFoundException |
                     WrongPinCodeException |
                     WrongParametersAmountException |
                     UnknownCommandException |
                     NotPositiveException |
                     NotPossibleBalanceChangeException |
                     InputMismatchException |
                     SameAccountTransferException |
                     NotLoggedException |
                     NumberFormatException e) {
                output.println(e);
            }

            if (exit.isOn()) {
                break;
            }
        }
    }

    protected void start() throws EmptyStringException, NotPositiveException {};

}
