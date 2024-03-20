package entities.bank;

import exceptions.*;

/**
 * Interface for interaction between the customer and the bank.
 */
public interface IApplication {
    /**
     * Method for providing data for identification.
     */
    void logIn(long phone) throws LoggedException, CustomerNotFound;

    /**
     * Method for logging out from application.
     */
    void logOut() throws NotLoggedException;

    /**
     * Method for transfer from one of customers"s accounts to an another.
     */
    void transfer(long accountNumber, int pinCode, long recipientAccountNumber, double amount) throws AccountNotFoundException, WrongPinCodeException, NotPositiveException, NotPossibleBalanceChangeException, SameAccountTransferException;

    /**
     * Method for updating passport data.
     * (Affects user verification)
     */
    void updatePassport(int series, int number) throws NotPositiveException;

    /**
     * Method for updating address.
     * (Affects user verification)
     */
    void updateAddress(String address);

    /**
     * Get a list of available accounts.
     */
    Iterable<String> getAccountsList() throws NotLoggedException;

    /**
     * Get list of transactions of concrete account.
     */
    Iterable<String> getTransactionsList(long accountNumber) throws NotLoggedException, AccountNotFoundException;

    /**
     * Get list of notifications and mails.
     */
    Iterable<String> getNotifications() throws NotLoggedException;

    /**
     * Receive notifications of changes to the terms and conditions of your existing accounts.
     */
    void enableNotifications(long accountNumber) throws NotLoggedException, AccountNotFoundException;

    /**
     * Not receive notifications of changes to the terms and conditions of your existing accounts.
     */
    void disableNotifications(long accountNumber) throws NotLoggedException, AccountNotFoundException;

    /**
     * Subscribing for bank mailing.
     */
    void subMailing() throws NotLoggedException;

    /**
     * Unsubscribing from bank mailing.
     */
    void unsubMailing() throws NotLoggedException;

    /**
     * Method for creation of credit account.
     */
    void createCreditAccount(int pinCode) throws NotLoggedException, NotPositiveException;

    /**
     * Method for creation of debit account.
     */
    void createDebitAccount(int pinCode) throws NotLoggedException, NotPositiveException;

    /**
     * Method for creation of deposit account.
     */
    void createDepositAccount(int pinCode, double startBalance) throws NotLoggedException, NotPositiveException;
}
