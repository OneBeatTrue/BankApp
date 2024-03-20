package entities.transaction;

import entities.account.IAccount;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.TransactionNotFoundException;

public class DepositTransaction extends Transaction {
    public DepositTransaction(double amount) throws NotPositiveException, AccountNotFoundException {
        super(amount);
    }

    @Override
    public void execute(IAccount account) throws AccountNotFoundException, TransactionNotFoundException {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (!alreadyOperated) {
            state = account.getState();
        }

        account.visitor().operateDeposit(this);
        alreadyOperated = true;
    }

    @Override
    public String toString() {
        return super.toString() + "\tDPS\t" + String.format("%.2f", amount) + "$";
    }
}
