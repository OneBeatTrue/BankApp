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

public class MachineTest {
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

    private IBank createBasicBank() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException {
        centralBank = new CentralBank();
        centralBank.createBank("Sber",
                3.65,
                Map.ofEntries(
                        Map.entry(0.0, 3.0),
                        Map.entry(50000.0, 3.5),
                        Map.entry(100000.0, 4.0)
                ),
                3,
                30,
                1000,
                10000
        );

        IBank bank = centralBank.getBankByName("Sber");
        bank.addCustomer(createBasicCustomer());
        return bank;
    }

    @Test
    public void deposit() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, NotPossibleBalanceChangeException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDebitAccount(1235);
        application.createDepositAccount(1236, 0);
        application.createCreditAccount(1237);
        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");

            bank.machine().deposit(Long.parseLong(fields[1]), 500);
        }

        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            Assertions.assertEquals("500,00$", fields[3]);
            System.out.println(str);
        }
    }

    @Test
    public void wrongWithdrawal() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, WrongPinCodeException, NotPossibleBalanceChangeException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDebitAccount(1235);
        application.createDepositAccount(1236, 60000);
        application.createCreditAccount(1237);
        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            int pin = switch (fields[0]) {
                case "DBT" -> 1235;
                case "DPS" -> 1236;
                case "CRD" -> 1237;
                default -> 0;
            };

            Assertions.assertThrowsExactly(NotPossibleBalanceChangeException.class, () -> {
                bank.machine().withdraw(Long.parseLong(fields[1]), pin, 1500);
            });
        }
    }

    @Test
    public void rightWithdrawal() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, WrongPinCodeException, NotPossibleBalanceChangeException {
        IBank bank = createBasicBank();
        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDebitAccount(1235);
        application.createDepositAccount(1236, 60000);
        application.createCreditAccount(1237);

        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            switch (fields[0]) {
                case "DBT":
                    bank.machine().deposit(Long.parseLong(fields[1]), 2000);
                    bank.machine().withdraw(Long.parseLong(fields[1]), 1235, 1500);
                    break;
                case "DPS":
                    centralBank.passDays(3);
                    bank.machine().withdraw(Long.parseLong(fields[1]), 1236, 1500);
                    break;
                case "CRD":
                    bank.machine().deposit(Long.parseLong(fields[1]), 600);
                    bank.machine().withdraw(Long.parseLong(fields[1]), 1237, 1500);
                    break;
            }
        }

        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            switch (fields[0]) {
                case "DBT":
                    Assertions.assertEquals("500,00$", fields[3]);
                    break;
                case "DPS":
                    Assertions.assertEquals("58500,00$", fields[3]);
                    break;
                case "CRD":
                    Assertions.assertEquals("-900,00$", fields[3]);
                    break;
            }
        }

        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            System.out.println(bank.getAccountByNumber(Long.parseLong(fields[1])));
            for (String transaction : application.getTransactionsList(Long.parseLong(fields[1]))) {
                System.out.println("\t" + transaction);
            }
        }
    }
}
