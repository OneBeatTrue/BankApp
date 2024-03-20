package entities.transaction;

import entities.account.IAccount;
import exceptions.AccountNotFoundException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;
import models.AccountState;

import java.util.UUID;

/**
 * Interface for transaction.
 * (Performs the role of a command, caretaker, acceptor)
 */
public interface ITransaction {
    /**
     * @return Unique transaction identifier.
     */
    UUID getId();

    /**
     * Method for transaction executing.
     * (Also accepts visitor)
     */
    void execute(IAccount account) throws AccountNotFoundException, NotPossibleBalanceChangeException, TransactionNotFoundException;

    /**
     * Saves snapshot of an account.
     */
    void saveState(IAccount account);

    /**
     * Method for transaction undoing operation.
     * @return Snapshot of a snapshot that was before executing.
     */
    AccountState undo(IAccount account) throws NotPossibleBalanceChangeException;

    /**
     * @return Amount of balance to transact.
     */
    double getAmount();

    /**
     * Says whether transaction was already operated.
     */
    boolean isAlreadyOperated();
}
