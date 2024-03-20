package entities.transaction;

import entities.account.IAccount;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import lombok.Getter;
import lombok.Setter;

public class InterestSaveTransaction extends Transaction {
    public InterestSaveTransaction() throws NotPositiveException {
        super(0);
        interest = 0;
    }

    @Getter
    @Setter
    private double interest;

    protected boolean operated;

    @Override
    public void execute(IAccount account) throws AccountNotFoundException {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (!operated) {
            state = account.getState();
        }

        account.visitor().operateInterestSave(this);
        operated = true;
    }

    @Override
    public String toString() {
        return "Bank internal operation";
    }
}
