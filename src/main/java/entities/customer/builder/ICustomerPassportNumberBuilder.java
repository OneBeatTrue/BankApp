package entities.customer.builder;

import exceptions.NotPositiveException;

/**
 * Passport number setter interface stage of customer builder.
 * (Optional, compulsory after storing of series)
 */
public interface ICustomerPassportNumberBuilder {
    ICustomerBuilder setPassportNumber(int number) throws NotPositiveException;
}
