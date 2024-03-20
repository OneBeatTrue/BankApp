package entities.customer.builder;

import exceptions.EmptyStringException;

/**
 * Name setter interface stage of customer builder.
 * (Compulsory)
 */
public interface ICustomerNameBuilder {
    ICustomerSurnameBuilder setName(String name) throws EmptyStringException;
}
