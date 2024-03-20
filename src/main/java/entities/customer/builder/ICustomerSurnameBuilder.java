package entities.customer.builder;

import exceptions.EmptyStringException;

/**
 * Surname setter interface stage of customer builder.
 * (Compulsory)
 */
public interface ICustomerSurnameBuilder {
    ICustomerBuilder setSurname(String surname) throws EmptyStringException;
}
