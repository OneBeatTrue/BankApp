import entities.bank.IApplication;
import entities.bank.IBank;
import entities.centralBank.CentralBank;
import entities.centralBank.ICentralBank;
import entities.customer.Customer;
import exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CentralBankTest {

    @Test
    public void bankCreation() throws EmptyStringException {
        ICentralBank centralBank = new CentralBank();
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

        Assertions.assertNotNull(centralBank.getBankByName("Alpha"));
        Assertions.assertNotNull(centralBank.getBankByName("Sber"));
        Assertions.assertNull(centralBank.getBankByName("Tink"));
    }

    @Test
    public void passDays() throws EmptyStringException, NotPositiveException, LoggedException, NotLoggedException, NotPossibleBalanceChangeException {
        ICentralBank centralBank = new CentralBank();
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
        bank.addCustomer(Customer.builder()
                .setPhone(3456789)
                .setName("Ivan")
                .setSurname("Petrov")
                .setPassportSeries(435)
                .setPassportNumber(24542)
                .setAddress("SPB")
                .build());

        IApplication application = bank.application();
        application.logIn(3456789);
        application.createDepositAccount(1236, 60000);

        centralBank.passDays(30);
        centralBank.accrueInterestAndFees();
        for (String str : application.getAccountsList()) {
            String[] fields = str.split("\t");
            Assertions.assertEquals("60172,60$", fields[3]);
            Assertions.assertEquals("0 days till unlock", fields[5]);
            System.out.println(str);
        }
    }
}
