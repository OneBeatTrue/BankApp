package entities.customer.builder;

import entities.customer.ICustomer;
import exceptions.EmptyStringException;
import exceptions.NotPositiveException;

/**
 * Last stage of customer builder.
 */
public interface ICustomerBuilder {
    /**
     * @return customer
     */
    ICustomer build();

    /**
     * Address setter.
     * (Optional)
     */
    ICustomerBuilder setAddress(String address) throws EmptyStringException;

    /**
     * Passport number setter.
     * (Optional)
     */
    ICustomerPassportNumberBuilder setPassportSeries(int series) throws NotPositiveException;
}


