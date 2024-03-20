package entities.account;

import entities.customer.ICustomer;
import entities.transaction.*;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;
import models.AccountState;

import java.util.UUID;

/**
 * Interface for bank account
 */
public interface IAccount {
    /**
     * Retrieves the owner of the account.
     * @return The owner of the account.
     */
    ICustomer getOwner();

    /**
     * Retrieves the number of the account.
     * @return The number of the account.
     */
    long getNumber();

    /**
     * Retrieves transactions associated with the account.
     * @return An iterable collection of transactions.
     */
    Iterable<ITransaction> getTransactions();

    /**
     * Checks if the provided PIN code matches the account's PIN.
     * @param pinCode The PIN code to be checked.
     * @return true if the PIN code is correct, otherwise false.
     */
    boolean checkPin(int pinCode);

    /**
     * Updates the unverified withdrawal limit of the account.
     * @param newUnverifiedLimit The new unverified withdrawal limit.
     */
    void updateUnverifiedLimit(double newUnverifiedLimit) throws NotPositiveException;

    /**
     * Enables notifications for the account.
     */
    void enableNotifications();

    /**
     * Disables notifications for the account.
     */
    void disableNotifications();

    /**
     * Cancels a transaction associated with the account.
     * @param transaction The transaction to be cancelled.
     */
    void cancelTransaction(ITransaction transaction) throws TransactionNotFoundException, AccountNotFoundException, NotPossibleBalanceChangeException;

    /**
     * Finds a transaction by its ID.
     * @param id The ID of the transaction to be found.
     * @return The transaction with the specified ID, or null if not found.
     */
    ITransaction findTransactionById(UUID id);

    /**
     * Retrieves the state of the account.
     * @return The state of the account.
     */
    AccountState getState();

    /**
     * Provides access to a visitor for the account.
     * @see IVisitor
     * @return A visitor for the account.
     */
    IVisitor visitor();
}
