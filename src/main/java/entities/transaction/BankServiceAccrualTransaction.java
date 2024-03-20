package entities.transaction;

import entities.account.IAccount;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.TransactionNotFoundException;

public class BankServiceAccrualTransaction extends Transaction {
    public BankServiceAccrualTransaction() throws NotPositiveException, AccountNotFoundException {
        super(0);
    }

    @Override
    public void execute(IAccount account) throws AccountNotFoundException, TransactionNotFoundException {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (!alreadyOperated) {
            state = account.getState();
        }

        account.visitor().operateBankServiceAccrual(this);
        alreadyOperated = true;
    }

    @Override
    public String toString() {
        return super.toString() + "\tBSA\t" + String.format("%.2f", amount) + "$";
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
