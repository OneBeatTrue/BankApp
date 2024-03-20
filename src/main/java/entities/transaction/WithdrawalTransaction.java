package entities.transaction;

import entities.account.IAccount;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import lombok.Getter;
import lombok.Setter;

public class WithdrawalTransaction extends Transaction {
    public WithdrawalTransaction(double amount) throws NotPositiveException, AccountNotFoundException {
        super(amount);
    }

    @Getter
    @Setter
    private double fee;

    @Override
    public void execute(IAccount account) throws AccountNotFoundException, NotPossibleBalanceChangeException {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (!alreadyOperated) {
            state = account.getState();
        }

        account.visitor().operateWithdrawal(this);
        alreadyOperated = true;
    }


    @Override
    public String toString() {
        return super.toString() + "\tWTD\t" + String.format("%.2f", -amount) + "$";
    }
}
