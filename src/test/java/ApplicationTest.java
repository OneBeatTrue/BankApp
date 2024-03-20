import entities.bank.IApplication;
import entities.bank.IBank;
import entities.centralBank.CentralBank;
import entities.centralBank.ICentralBank;
import entities.customer.Customer;
import entities.customer.ICustomer;
import exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ApplicationTest {
    private ICentralBank centralBank;
    private ICustomer createBasicCustomer() throws NotPositiveException, EmptyStringException {
        return Customer.builder()
                .setPhone(3456789)
                .setName("Ivan")
                .setSurname("Petrov")
                .setPassportSeries(435)
                .setPassportNumber(24542)
                .setAddress("SPB")
                .build();
    }

    private IBank createBasicBank() throws EmptyStringException, NotPositiveException {
        centralBank = new CentralBank();
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

        IBank bank = centralBank.getBankByName("Sber");
        bank.addCustomer(createBasicCustomer());
        return bank;
    }

    @Test
    public void notLogged() throws EmptyStringException, NotPositiveException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        Assertions.assertThrowsExactly(NotLoggedException.class, () -> {
            application.createCreditAccount(32432);
        });
    }

    @Test
    public void accountCreation() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDebitAccount(1234);
        application.createDebitAccount(1235);
        application.createDepositAccount(1236, 60000);
        application.createCreditAccount(1237);
        for (String str : application.getAccountsList()) {
            System.out.println(str);
        }

        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            Assertions.assertNotNull(bank.getAccountByNumber(Long.parseLong(fields[1])));
        }
    }

    @Test
    public void transfer() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, WrongPinCodeException, NotPossibleBalanceChangeException, SameAccountTransferException {
        IBank firstBank = createBasicBank();
        IApplication firstApp = firstBank.application();
        firstApp.logIn(3456789);
        firstApp.createCreditAccount(1234);
        long firstId = 0;
        for (String str : firstApp.getAccountsList()) {
            String[] fields = str.split("\t");
            firstId = Long.parseLong(fields[1]);
        }

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
        IBank secondBank = centralBank.getBankByName("Alpha");
        secondBank.addCustomer(Customer.builder()
                .setPhone(2456789)
                .setName("Petr")
                .setSurname("Ivanov")
                .setPassportSeries(436)
                .setPassportNumber(24541)
                .setAddress("MSK")
                .build());
        IApplication secondApp = secondBank.application();
        secondApp.logIn(2456789);
        secondApp.createDebitAccount(1235);
        long secondId = 0;
        for (String str : secondApp.getAccountsList()) {
            String[] fields = str.split("\t");
            secondId = Long.parseLong(fields[1]);
        }

        firstApp.transfer(firstId, 1234, secondId, 500);

        for (String str : firstApp.getAccountsList()) {
            System.out.println(str);
            String[] fields = str.split("\t");
            Assertions.assertEquals("-500,00$", fields[3]);
        }

        for (String str : secondApp.getAccountsList()) {
            System.out.println(str);
            String[] fields = str.split("\t");
            Assertions.assertEquals("500,00$", fields[3]);
        }
    }
}
