import entities.centralBank.CentralBank;
import entities.centralBank.ICentralBank;
import entities.customer.Customer;
import entities.customer.ICustomer;
import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CustomerTest {
    @Test
    public void customerCreation() throws NotPositiveException, EmptyStringException {
        ICustomer user = Customer.builder()
                .setPhone(3456789)
                .setName("Ivan")
                .setSurname("Petrov")
                .setPassportSeries(435)
                .setPassportNumber(24542)
                .build();
        Assertions.assertFalse(user.isVerified());
        user.updateAddress("SPB");
        Assertions.assertTrue(user.isVerified());
    }
}
