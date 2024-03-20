package entities.account;

import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;

public interface IDateDependentAccount extends IAccount {
    void doDaily() throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException;
}
