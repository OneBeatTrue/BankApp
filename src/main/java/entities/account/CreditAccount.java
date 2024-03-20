package entities.account;

import entities.customer.ICustomer;
import entities.transaction.*;
import exceptions.CustomerNotFound;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;

public class CreditAccount extends Account implements ICreditAccount {
    public CreditAccount(long number, ICustomer owner, int pinCode, double unverifiedLimit, double fee, double creditLimit) throws NotPositiveException, CustomerNotFound {
        super(number, owner, pinCode, unverifiedLimit);
        if (fee < 0) {
            throw new NotPositiveException("Fee");
        }

        this.fee = fee;
        if (creditLimit < 0) {
            throw new NotPositiveException("Credit limit");
        }

        this.creditLimit = -creditLimit;
    }


    private double fee;
    private double creditLimit;

    @Override
    public double getFee() {
        return fee;
    }

    @Override
    public void updateFee(double newFee) throws NotPositiveException {
        if (newFee < 0) {
            throw new NotPositiveException("Fee");
        }

        if (newFee != fee) {
            if (enableNotifications) {
                owner.Notify(number + " : Fee changed from "
                        + String.format("%.2f", -fee)
                        + "$ to "
                        + String.format("%.2f", newFee)
                        + "$");
            }

            fee = newFee;
        }
    }

    @Override
    public void updateCreditLimit(double newCreditLimit) throws NotPositiveException {
        if (newCreditLimit < 0) {
            throw new NotPositiveException("Credit limit");
        }

        if (newCreditLimit != -creditLimit) {
            if (enableNotifications) {
                owner.Notify(number + " : Credit limit changed from "
                        + String.format("%.2f", -creditLimit)
                        + "$ to "
                        + String.format("%.2f", newCreditLimit)
                        + "$");
            }

            creditLimit = -newCreditLimit;
        }
    }

    @Override
    public String toString() {
        return "CRD"
                + "\t"
                + super.toString()
                + "\t"
                + String.format("%.2f", fee)
                + "$ fee\t"
                + -creditLimit
                + "$ limit";
    }

    @Override
    public IVisitor visitor() {
        return new CreditAccountVisitor();
    }

    private void verifyWithdrawal(double amount, double fee) throws NotPossibleBalanceChangeException {
        if (!owner.isVerified()) {
            if (amount < unverifiedLimit) {
                throw new NotPossibleBalanceChangeException("Attempt to exceed unverified limit");
            }
        }

        if (amount < 0) {
            if (balance + amount < 0) {
                if (balance + amount - fee < creditLimit) {
                    throw new NotPossibleBalanceChangeException("Attempt to exceed credit limit");
                }
            }
        }
    }

    private class CreditAccountVisitor extends Visitor {
        @Override
        public void operateInterestSave(InterestSaveTransaction transaction) {}

        @Override
        public void operateWithdrawal(WithdrawalTransaction transaction) throws NotPossibleBalanceChangeException, TransactionNotFoundException {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            if (!transaction.isAlreadyOperated()) {
                transaction.setFee(fee);
                verifyWithdrawal(-transaction.getAmount(), transaction.getFee());
            }

            balance -= transaction.getAmount();
            if (balance < 0) {
                serviceBalance -= transaction.getFee();
            }

            transactions.push(transaction);
        }
    }
}
