package entities.account;

import entities.customer.ICustomer;
import entities.transaction.InterestSaveTransaction;
import entities.transaction.WithdrawalTransaction;
import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;
import models.Constants;

import java.util.Map;

public class DepositAccount extends Account implements IDepositAccount {
    public DepositAccount(long number, ICustomer owner, int pinCode, double unverifiedLimit, double startBalance, Map<Double, Double> interests, int daysRemaining) throws NotPositiveException {
        super(number, owner, pinCode, unverifiedLimit);
        if (startBalance < 0) {
            throw new NotPositiveException("Start balance");
        }

        this.startBalance = startBalance;
        balance = this.startBalance;
        checkInterests(interests);
        interest = findInterest(interests);
        if (interest < 0) {
            throw new NotPositiveException("Interest");
        }

        if (daysRemaining < 0) {
            throw new NotPositiveException("Days remaining");
        }

        this.daysRemaining = daysRemaining;
    }

    private double interest;
    private double startBalance;
    private int daysRemaining;

    @Override
    public void doDaily() throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException {
        new InterestSaveTransaction().execute(this);

        if (daysRemaining > 0) {
            --daysRemaining;
        }
    }

    @Override
    public void updateInterest(Map<Double, Double> newInterests) throws NotPositiveException {
        checkInterests(newInterests);
        double newInterest = findInterest(newInterests);
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
        return "DPS"
                + "\t"
                + super.toString()
                + "\t"
                + String.format("%.2f", interest)
                + "%\t"
                + daysRemaining
                + " days till unlock";
    }

    @Override
    public IVisitor visitor() {
        return new DepositAccountVisitor();
    }

    private void verifyWithdrawal(double amount) throws NotPossibleBalanceChangeException {
        if (!owner.isVerified()) {
            if (amount < unverifiedLimit) {
                throw new NotPossibleBalanceChangeException("Attempt to exceed unverified limit");
            }
        }

        if (daysRemaining > 0 && amount < 0) {
            throw new NotPossibleBalanceChangeException("Account locked for withdrawals for another " + daysRemaining + " days");
        }

        if (balance + amount < 0) {
            throw new NotPossibleBalanceChangeException("Not enough balance");
        }
    }

    private double findInterest(Map<Double, Double> newInterests) {
        double floorKey = Double.MIN_VALUE;
        for (double key : newInterests.keySet()) {
            if (key <= startBalance && key > floorKey) {
                floorKey = key;
            }
        }

        if (floorKey == Double.MIN_VALUE) {
            return 0;
        }

        return newInterests.get(floorKey);
    }

    private void checkInterests(Map<Double, Double> newInterests) throws NotPositiveException {
        for (Map.Entry<Double, Double> entry : newInterests.entrySet()) {
            if (entry.getKey() < 0) {
                throw new NotPositiveException("Interest");
            }

            if (entry.getValue() < 0) {
                throw new NotPositiveException("Start balance");
            }
        }
    }

    private class DepositAccountVisitor extends Visitor {
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
    }
}
