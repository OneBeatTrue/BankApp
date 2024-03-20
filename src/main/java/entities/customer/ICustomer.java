package entities.customer;

import exceptions.NotPositiveException;

/**
 * Interface of bank customer.
 * (Observer)
 */
public interface ICustomer {
    /**
     * @return Phone number of the customer.
     */
    long getPhone();

    /**
     * @return Name of the customer.
     */
    String getName();

    /**
     * @return Surname of the customer.
     */
    String getSurname();

    /**
     * Says whether customer is verified.
     */
    boolean isVerified();

    /**
     * Update passport data.
     * (Affects on verification)
     * @param series Passport series.
     * @param number Passport number.
     */
    void updatePassport(int series, int number) throws NotPositiveException;

    /**
     * Update the address of the customer.
     */
    void updateAddress(String address);

    /**
     * Get list of notifications and mails.
     */
    Iterable<String> getNotifications();

    /**
     * Send a notification to the customer.
     */
    void Notify(String message);
}
