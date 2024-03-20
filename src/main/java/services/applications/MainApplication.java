package services.applications;

import entities.centralBank.CentralBank;
import entities.centralBank.ICentralBank;
import entities.customer.Customer;
import exceptions.*;
import services.handlers.MainHandler;
import services.handlers.bank.BankHandler;
import services.handlers.centralBank.CentralBankHandler;

import java.io.PrintStream;
import java.util.*;

public class MainApplication extends UserApplication {
    public MainApplication(Scanner input, PrintStream output) {
        super(input, output);

        centralBank = new CentralBank();
        handler = new MainHandler(this.input, this.output, exit, new ArrayList<>(Arrays.asList(
                new CentralBankHandler(this.input, this.output, centralBank),
                new BankHandler(this.input, this.output, centralBank)
        )));
    }

    private ICentralBank centralBank;

    @Override
    protected void start() throws EmptyStringException, NotPositiveException {
        centralBank.createBank("Sber",
                3.65,
                Map.ofEntries(
                        Map.entry(0.0, 3.0),
                        Map.entry(50000.0, 3.5),
                        Map.entry(100000.0, 4.0)
                ),
                15,
                30,
                1000,
                10000
        );
        centralBank.getBankByName("Sber").addCustomer(Customer.builder()
                .setPhone(123)
                .setName("Nikita")
                .setSurname("Kozlov")
                .setPassportSeries(435)
                .setPassportNumber(24542)
                .setAddress("SPB")
                .build());
        centralBank.createBank("Alpha",
                3.85,
                Map.ofEntries(
                        Map.entry(0.0, 2.0),
                        Map.entry(50000.0, 3.67),
                        Map.entry(120000.0, 4.2)
                ),
                10,
                25,
                1200,
                12000
        );
        centralBank.getBankByName("Alpha").addCustomer(Customer.builder()
                .setPhone(321)
                .setName("Petr")
                .setSurname("Ivanov")
                .setPassportSeries(436)
                .setPassportNumber(24541)
                .setAddress("MSK")
                .build());
    }
}
