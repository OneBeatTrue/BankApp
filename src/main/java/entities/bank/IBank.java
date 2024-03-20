package entities.bank;

import entities.account.IAccount;
import entities.customer.ICustomer;
import entities.transaction.ITransaction;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for conventional bank
 * (Observable)
 */
public interface IBank {
    /**
     * Updates the unverified withdrawal limit for the account.
     * @param newUnverifiedLimit The new unverified withdrawal limit to be set.
     */
    void updateUnverifiedLimit(double newUnverifiedLimit) throws NotPositiveException;

    /**
     * Updates the fee for credit transactions.
     * @param newFee The new fee for credit transactions to be set.
     */
    void updateFeeForCredit(double newFee) throws NotPositiveException;

    /**
     * Updates the credit limit for credit transactions.
     * @param newCreditLimit The new credit limit to be set.
     */
    void updateCreditLimitForCredit(double newCreditLimit) throws NotPositiveException;

    /**
     * Updates the interest rate for debit transactions.
     * @param newInterest The new interest rate for debit transactions to be set.
     */
    void updateInterestForDebit(double newInterest) throws NotPositiveException;

    /**
     * Updates the interest rates for deposit transactions.
     * @param newInterests The new interest rates for deposit transactions to be set.
     */
    void updateInterestForDeposit(Map<Double, Double> newInterests) throws NotPositiveException;

    /**
     * Adds a customer to the bank.
     * @param customer The customer to be added (verified of not).
     */
    void addCustomer(ICustomer customer);

    /**
     * @return The name of the bank.
     */
    String getName();

    /**
     * @return Account by its number.
     */
    IAccount getAccountByNumber(long number);

    /**
     * @return Transactions from all accounts.
     */
    Iterable<ITransaction> getAllTransactions();

    /**
     * Moves forward by one day in the bank's operation, processing daily activities for accounts.
     */
    void passDay() throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException;

    /**
     * Accrues fees and interest for banking services.
     */
    void accrueForServices() throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException;

    /**
     * Notify all mail subscribers (observers).
     */
    void mailSubs(String message);

    /**
     * Cancel transaction.
     */
    void cancelTransaction(UUID id) throws TransactionNotFoundException, AccountNotFoundException, NotPossibleBalanceChangeException;

    /**
     * Need for customer to use bank's application.
     * @see IApplication
     */
    IApplication application();

    /**
     * Need for customer to withdraw and deposit.
     * @see IMachine
     */
    IMachine machine();
}
