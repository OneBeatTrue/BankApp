package entities.account;

import entities.customer.ICustomer;
import entities.transaction.*;
import exceptions.*;
import models.AccountState;
import java.util.Stack;
import java.util.UUID;

public abstract class Account implements IAccount {
    public Account(long number, ICustomer owner, int pinCode, double unverifiedLimit) throws NotPositiveException, CustomerNotFound {
        this.number = number;
        if (owner == null) {
            throw new CustomerNotFound();
        }

        this.owner = owner;
        if (pinCode < 0) {
            throw new NotPositiveException("PIN code");
        }

        this.pinCode = Integer.hashCode(pinCode);
        enableNotifications = true;
        transactions = new Stack<>();
        balance = 0;
        serviceBalance = 0;
        if (unverifiedLimit < 0) {
            throw new NotPositiveException("Unverified limit");
        }

        this.unverifiedLimit = -unverifiedLimit;
    }

    protected ICustomer owner;
    protected boolean enableNotifications;
    protected double balance;
    protected double serviceBalance;
    protected long number;
    protected Stack<ITransaction> transactions;
    protected double unverifiedLimit;
    private long pinCode;

    @Override
    public ICustomer getOwner() {
        return owner;
    }

    @Override
    public long getNumber() {
        return number;
    }

    @Override
    public Iterable<ITransaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean checkPin(int pinCode) {
        return Integer.hashCode(pinCode) == this.pinCode;
    }

    @Override
    public void updateUnverifiedLimit(double newUnverifiedLimit) throws NotPositiveException {
        if (newUnverifiedLimit < 0) {
            throw new NotPositiveException("Unverified limit");
        }

        if (newUnverifiedLimit != -unverifiedLimit) {
            if (enableNotifications && !owner.isVerified()) {
                owner.Notify(number + " : Unverified customer transaction limit changed from "
                        + String.format("%.2f", -unverifiedLimit)
                        + "$ to "
                        + String.format("%.2f", newUnverifiedLimit)
                        + "$");
            }

            unverifiedLimit = -newUnverifiedLimit;
        }
    }

    @Override
    public void enableNotifications() {
        enableNotifications = true;
    }

    @Override
    public void disableNotifications() {
        enableNotifications = false;
    }

    @Override
    public String toString() {
        return number
                + "\t"
                + owner.getName()
                + " " + owner.getSurname()
                + "\t"
                + String.format("%.2f", balance)
                + "$";
    }

    @Override
    public void cancelTransaction(ITransaction transaction) throws TransactionNotFoundException, AccountNotFoundException, NotPossibleBalanceChangeException {
        if (transaction == null) {
            throw new TransactionNotFoundException();
        }

        Stack<ITransaction> temporaryStack = new Stack<>();
        ITransaction currentTransaction = transactions.pop();
        while (currentTransaction != transaction) {
            temporaryStack.push(currentTransaction);
            currentTransaction = transactions.pop();
        }

        restoreState(transaction.undo(this));
        while (!temporaryStack.empty()) {
            temporaryStack.pop().execute(this);
        }
    }

    @Override
    public ITransaction findTransactionById(UUID id) {
        for (ITransaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }

        return null;
    }

    @Override
    public AccountState getState() {
        return new AccountState(balance, serviceBalance);
    }

    protected void restoreState(AccountState state) {
        balance = state.Balance();
        serviceBalance = state.ServiceBalance();
    }

    protected abstract class Visitor implements IVisitor {
        @Override
        public void operateTransfer(TransferTransaction transaction) throws TransactionNotFoundException {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            transactions.pop();
            transactions.push(transaction);
        }

        @Override
        public void operateDeposit(DepositTransaction transaction) throws TransactionNotFoundException {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            balance += transaction.getAmount();
            transactions.push(transaction);
        }

        @Override
        public void operateBankServiceAccrual(BankServiceAccrualTransaction transaction) throws TransactionNotFoundException {
            if (transaction == null) {
                throw new TransactionNotFoundException();
            }

            transaction.setAmount(serviceBalance);
            balance += serviceBalance;
            serviceBalance = 0;
            transactions.push(transaction);
        }
    }
}
