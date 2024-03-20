package services.handlers.bank;

import entities.bank.IBank;
import entities.customer.Customer;
import entities.customer.builder.ICustomerBuilder;
import exceptions.*;
import services.handlers.BankHavingHandler;
import services.exceptions.UnknownCommandException;
import services.exceptions.WrongParametersAmountException;

import java.io.PrintStream;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;

public class CreateUserHandler extends BankHavingHandler {
    public CreateUserHandler(Scanner input, PrintStream output, IBank bank) {
        super(input, output, bank);
        help = "create_usr - create customer";
    }

    @Override
    public void handle(ListIterator<String> iterator) throws UnknownCommandException, WrongParametersAmountException, NotPositiveException, NotPossibleBalanceChangeException, EmptyStringException, WrongPinCodeException, LoggedException, SameAccountTransferException, NotLoggedException {
        if (!iterator.hasNext()) {
            throw new WrongParametersAmountException();
        }

        if (Objects.equals(iterator.next(), "create_usr")) {
            output.print("Enter phone number : ");
            long phone = input.nextLong();

            output.print("Enter name : ");
            input.nextLine();
            String name = input.nextLine();


            output.print("Enter surname : ");
            String surname = input.nextLine();

            ICustomerBuilder builder = Customer.builder()
                    .setPhone(phone)
                    .setName(name)
                    .setSurname(surname);

            String answer = "";
            while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n")) {
                output.println("Do you want to add address? [y/n]");
                answer = input.nextLine();
            }

            if (Objects.equals(answer, "y")) {
                output.print("Enter address : ");
                builder.setAddress(input.nextLine());
            }

            answer = "";
            while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n")) {
                output.println("Do you want to add passport? [y/n]");
                answer = input.nextLine();
            }

            if (Objects.equals(answer, "y")) {
                output.print("Enter passport series : ");
                int series = input.nextInt();

                output.print("Enter passport number : ");
                int number = input.nextInt();

                builder.setPassportSeries(series).setPassportNumber(number);
            }

            bank.addCustomer(builder.build());
            output.println("Success");
            input.nextLine();
        }
        else {
            iterator.previous();
            super.handle(iterator);
        }
    }
}