package entities.account;

import entities.customer.ICustomer;
import entities.transaction.InterestSaveTransaction;
import entities.transaction.WithdrawalTransaction;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;
import models.Constants;

public class DebitAccount extends Account implements IDebitAccount {
    public DebitAccount(long number, ICustomer owner, int pinCode, double unverifiedLimit, double interest) throws NotPositiveException {
        super(number, owner, pinCode, unverifiedLimit);
        if (interest < 0) {
            throw new NotPositiveException("Interest");
        }

        this.interest = interest;
    }

    private double interest;

    @Override
    public void doDaily() throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException {
        new InterestSaveTransaction().execute(this);
    }

    @Override
    public void updateInterest(double newInterest) throws NotPositiveException {
        if (newInterest < 0) {
            throw new NotPositiveException("Interest");
        }

        if (newInterest != interest) {
            if (enableNotifications) {
                owner.Notify(number + " : Interest changed from "
                        + String.format("%.2f", interest)
                        + "% to "
                        + String.format("%.2f", newInterest)
                        + "%");
            }

            interest = newInterest;
        }
    }

    @Override
    public String toString() {
        return "DBT"
                + "\t"
                + super.toString()
                + "\t"
                + String.format("%.2f", interest)
                + "%";
    }

    @Override
    public IVisitor visitor() {
        return new DebitAccountVisitor();
    }

    private void verifyWithdrawal(double amount) throws NotPossibleBalanceChangeException {
        if (!owner.isVerified()) {
            if (amount < unverifiedLimit) {
                throw new NotPossibleBalanceChangeException("Attempt to exceed unverified limit");
            }
        }

        if (balance + amount < 0) {
            throw new NotPossibleBalanceChangeException("Not enough balance");
        }
    }

    private class DebitAccountVisitor extends Visitor {
        @Override
        public void operateWithdrawal(WithdrawalTransaction transaction) throws NotPossibleBalanceChangeException {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            if (!transaction.isAlreadyOperated()) {
                verifyWithdrawal(-transaction.getAmount());
            }

            balance -= transaction.getAmount();
            transactions.push(transaction);
        }

        @Override
        public void operateInterestSave(InterestSaveTransaction transaction) {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            if (!transaction.isAlreadyOperated()) {
                transaction.setInterest(interest);
            }

            if (balance > 0) {
                serviceBalance += balance * interest / Constants.FULL_PERCENT / Constants.DAYS_PER_YEAR;
            }

            transactions.push(transaction);
        }
    }
}
