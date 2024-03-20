package entities.transaction;

import entities.account.*;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import lombok.Getter;
import models.AccountState;

import java.util.UUID;

public abstract class Transaction implements ITransaction {
    public Transaction(double amount) throws NotPositiveException, AccountNotFoundException {
        id = UUID.randomUUID();
        if (amount < 0) {
            throw new NotPositiveException("Transaction amount");
        }

        this.amount = amount;
        this.state = new AccountState(0, 0);
        this.alreadyOperated = false;
    }

    @Getter
    private UUID id;

    @Getter
    protected double amount;

    protected boolean alreadyOperated;

    protected AccountState state;

    public void saveState(IAccount account) {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        state = account.getState();
    }

    @Override
    public AccountState undo(IAccount account) throws NotPossibleBalanceChangeException {
        return state;
    }

    public boolean isAlreadyOperated() {
        return alreadyOperated;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
