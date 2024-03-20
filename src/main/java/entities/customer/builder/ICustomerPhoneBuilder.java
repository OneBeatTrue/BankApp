package entities.customer.builder;

import exceptions.NotPositiveException;

/**
 * Phone number setter interface stage of customer builder.
 * (Compulsory)
 */
public interface ICustomerPhoneBuilder {
    ICustomerNameBuilder setPhone(long phone) throws NotPositiveException;
}
