import entities.bank.IApplication;
import entities.bank.IBank;
import entities.centralBank.CentralBank;
import entities.centralBank.ICentralBank;
import entities.customer.Customer;
import entities.customer.ICustomer;
import entities.transaction.ITransaction;
import exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

public class BankTest {
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
    public void changeNotifications() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDebitAccount(1234);
        bank.updateInterestForDebit(5.0);
        bank.updateUnverifiedLimit(13500);

        for (String notification : application.getNotifications()) {
            System.out.println(notification);
        }

        Assertions.assertTrue(application.getNotifications().iterator().hasNext());
    }

    @Test
    public void undoTransaction() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, WrongPinCodeException, NotPossibleBalanceChangeException, SameAccountTransferException {
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
        firstBank.machine().deposit(firstId, 300);
        centralBank.passDays(10);
        secondBank.machine().withdraw(secondId, 1235, 400);
        centralBank.accrueInterestAndFees();
        secondApp.transfer(secondId, 1235, firstId, 100);

        System.out.println(firstBank.getAccountByNumber(firstId));
        Assertions.assertEquals("-130,00$", firstBank.getAccountByNumber(firstId).toString().split("\t")[3]);
        System.out.println(secondBank.getAccountByNumber(secondId));
        Assertions.assertEquals("0,53$", secondBank.getAccountByNumber(secondId).toString().split("\t")[3]);
        System.out.println();

        for (ITransaction transaction : firstBank.getAccountByNumber(firstId).getTransactions()) {
            System.out.println(transaction);
            System.out.println();
            secondBank.cancelTransaction(transaction.getId());
            break;
        }

        System.out.println(firstBank.getAccountByNumber(firstId));
        Assertions.assertEquals("400,00$", firstBank.getAccountByNumber(firstId).toString().split("\t")[3]);
        System.out.println(secondBank.getAccountByNumber(secondId));
        Assertions.assertEquals("-500,00$", secondBank.getAccountByNumber(secondId).toString().split("\t")[3]);
        System.out.println();
    }
}
