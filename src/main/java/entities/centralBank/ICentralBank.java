package entities.centralBank;

import entities.account.IAccount;
import entities.bank.IBank;
import exceptions.AccountNotFoundException;
import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;

import java.util.Map;
import java.util.Set;

/**
 * Interface for central bank.
 * Used for setting up conventional banks and interfacing for transfers.
 */
public interface ICentralBank {
    /**
     * Method for creation of bank.
     */
    void createBank(String name,
                    double debitInterest,
                    Map<Double, Double> depositInterests,
                    int depositRemaining,
                    double creditFee,
                    double creditLimit,
                    double unverifiedWithdrawalLimit) throws EmptyStringException;

    /**
     * Method for skipping the day.
     * (Notifies date dependent accounts)
     */
    void passDays(int days) throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException;

    /**
     * Notifies accounts to accrue for bank services.
     */
    void accrueInterestAndFees() throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException;

    /**
     * Method that helps to provide transfer between two accounts.
     */
    IAccount getAccountByNumber(long number);

    /**
     * @return Bank by name
     */
    IBank getBankByName(String name);

    /**
     * @return List of banks
     */
    Iterable<String> getBanksList();
}
