package entities.bank;

import entities.account.*;
import entities.centralBank.ICentralBank;
import entities.customer.ICustomer;
import entities.transaction.*;
import exceptions.*;
import lombok.Getter;
import models.Constants;

import java.util.*;

public class Bank implements IBank {
    public Bank(ICentralBank centralBank,
                String name,
                int bankId,
                double debitInterest,
                Map<Double, Double> depositInterests,
                int depositRemaining,
                double creditFee,
                double creditLimit,
                double unverifiedWithdrawalLimit) throws EmptyStringException {
        if (centralBank == null) {
            throw new NullPointerException("Central bank not exits");
        }

        this.centralBank = centralBank;
        if (name.isEmpty()) {
            throw new EmptyStringException("Name");
        }

        this.name = name;
        this.bankId = bankId;
        this.debitInterest = debitInterest;
        this.depositInterests = depositInterests;
        this.depositRemaining = depositRemaining;
        this.creditFee = creditFee;
        this.creditLimit = creditLimit;
        this.unverifiedWithdrawalLimit = unverifiedWithdrawalLimit;
        this.accountStorage = new HashSet<>();
        this.customerStorage = new HashSet<>();
        this.busyAccountIdStorage = new HashSet<>();
        this.mailingSubscribers = new HashSet<>();
        this.random = new Random();
    }

    private ICentralBank centralBank;
    @Getter
    private String name;
    private int bankId;
    private double debitInterest;
    private Map<Double, Double> depositInterests;
    private int depositRemaining;
    private double creditFee;
    private double creditLimit;
    private double unverifiedWithdrawalLimit;
    private Collection<IAccount> accountStorage;
    private Collection<ICustomer> customerStorage;
    private Collection<Integer> busyAccountIdStorage;
    private Collection<ICustomer> mailingSubscribers;
    private java.util.Random random;

    @Override
    public void updateUnverifiedLimit(double newUnverifiedLimit) throws NotPositiveException {
        if (newUnverifiedLimit < 0) {
            throw new NotPositiveException("Unverified withdrawal limit");
        }

        for (IAccount account : accountStorage) {
            account.updateUnverifiedLimit(newUnverifiedLimit);
        }

        unverifiedWithdrawalLimit = newUnverifiedLimit;
    }

    @Override
    public void updateFeeForCredit(double newFee) throws NotPositiveException {
        if (newFee < 0) {
            throw new NotPositiveException("Fee");
        }

        for (IAccount account : accountStorage) {
            if (account instanceof ICreditAccount) {
                ((ICreditAccount) account).updateFee(newFee);
            }
        }

        creditFee = newFee;
    }

    @Override
    public void updateCreditLimitForCredit(double newCreditLimit) throws NotPositiveException {
        if (newCreditLimit < 0) {
            throw new NotPositiveException("Credit limit");
        }

        for (IAccount account : accountStorage) {
            if (account instanceof ICreditAccount) {
                ((ICreditAccount) account).updateCreditLimit(newCreditLimit);
            }
        }

        creditLimit = newCreditLimit;
    }

    @Override
    public void updateInterestForDebit(double newInterest) throws NotPositiveException {
        if (newInterest < 0) {
            throw new NotPositiveException("Interest");
        }

        for (IAccount account : accountStorage) {
            if (account instanceof IDebitAccount) {
                ((IDebitAccount) account).updateInterest(newInterest);
            }
        }

        debitInterest = newInterest;
    }

    @Override
    public void updateInterestForDeposit(Map<Double, Double> newInterests) throws NotPositiveException {
        for (Map.Entry<Double, Double> entry : newInterests.entrySet()) {
            if (entry.getKey() < 0) {
                throw new NotPositiveException("Interest");
            }

            if (entry.getValue() < 0) {
                throw new NotPositiveException("Start balance");
            }
        }

        for (IAccount account : accountStorage) {
            if (account instanceof IDepositAccount) {
                ((IDepositAccount) account).updateInterest(newInterests);
            }
        }

        depositInterests = newInterests;
    }

    @Override
    public void addCustomer(ICustomer customer) {
        if (customer == null) {
            throw new NullPointerException("Customer not exist");
        }

        customerStorage.add(customer);
    }

    @Override
    public void passDay() throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException {
        for (IAccount account : accountStorage) {
            if (account instanceof IDateDependentAccount) {
                ((IDateDependentAccount) account).doDaily();
            }
        }
    }

    @Override
    public void accrueForServices() throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException {
        for (IAccount account : accountStorage) {
            new BankServiceAccrualTransaction().execute(account);
        }
    }

    @Override
    public void mailSubs(String message) {
        for (ICustomer customer : mailingSubscribers) {
            customer.Notify(message);
        }
    }

    @Override
    public void cancelTransaction(UUID id) throws TransactionNotFoundException, AccountNotFoundException, NotPossibleBalanceChangeException {
        for (IAccount account : accountStorage) {
            ITransaction transaction = account.findTransactionById(id);
            if (transaction != null) {
                account.cancelTransaction(transaction);
                return;
            }
        }

        throw new TransactionNotFoundException();
    }

    @Override
    public IApplication application() {
        return new Application();
    }

    @Override
    public IAccount getAccountByNumber(long number) {
        for (IAccount account : accountStorage){
            if (account.getNumber() == number){
                return account;
            }
        }

        return null;
    }

    @Override
    public Iterable<ITransaction> getAllTransactions() {
        Collection<ITransaction> transactions = new HashSet<>();
        for (IAccount account : accountStorage) {
            for (ITransaction transaction : account.getTransactions()) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    @Override
    public IMachine machine() {
        return new Machine();
    }

    @Override
    public String toString() {
        return bankId
                + "\t"
                + name;
    }

    private long getNewId() {
        int accountId = random.nextInt(Constants.NINE_DIGIT_BOUND);;
        while (busyAccountIdStorage.contains(accountId)){
            accountId = random.nextInt(Constants.NINE_DIGIT_BOUND);
        }

        busyAccountIdStorage.add(accountId);
        return algorithmLuhn(accountId);
    }

    private long algorithmLuhn(int accountId) {
        String tempNumber = String.format("%06d", bankId) + String.format("%09d", accountId);
        int sum = 0;
        for (int position = 0; position < tempNumber.length(); ++position) {
            int digit = Character.getNumericValue(tempNumber.charAt(position));
            if (position % Constants.TWO_RADIX == 0) {
                digit <<= 1;
                if (digit > Constants.TEN_RADIX - 1) {
                    digit -= Constants.TEN_RADIX - 1;
                }
            }

            sum += digit;
        }

        int lastDigit = (Constants.TEN_RADIX - sum % Constants.TEN_RADIX) % Constants.TEN_RADIX;
        tempNumber = tempNumber + lastDigit;
        return Long.parseLong(tempNumber);
    }

    private Iterable<IAccount> getAccountsByCustomer(ICustomer customer) {
        if (customer == null) {
            throw new CustomerNotFound();
        }

        Collection<IAccount> accounts = new ArrayList<IAccount>();
        for (IAccount account : accountStorage){
            if (account.getOwner() == customer){
                accounts.add(account);
            }
        }

        return accounts;
    }

    private ICustomer getCustomerByPhone(long phone) {
        for (ICustomer customer : customerStorage) {
            if (customer.getPhone() == phone) {
                return customer;
            }
        }

        return null;
    }

    private class Application implements IApplication {
        private ICustomer Customer;

        @Override
        public void transfer(long accountNumber, int pinCode, long recipientAccountNumber, double amount) throws AccountNotFoundException, WrongPinCodeException, NotPositiveException, NotPossibleBalanceChangeException, SameAccountTransferException {
            IAccount account = getAccountByNumber(accountNumber);
            IAccount recipient = centralBank.getAccountByNumber(recipientAccountNumber);
            if (account == null) {
                throw new AccountNotFoundException();
            }

            if (recipient == null) {
                throw new AccountNotFoundException();
            }

            if (!account.checkPin(pinCode)){
                throw new WrongPinCodeException();
            }

            new TransferTransaction(amount, account, recipient).execute(account);
        }

        @Override
        public void createCreditAccount(int pinCode) throws NotLoggedException, NotPositiveException {
            checkCustomer(Customer);
            accountStorage.add(new CreditAccount(getNewId(), Customer, pinCode, unverifiedWithdrawalLimit, creditFee, creditLimit));
        }

        @Override
        public void createDebitAccount(int pinCode) throws NotLoggedException, NotPositiveException {
            checkCustomer(Customer);
            accountStorage.add(new DebitAccount(getNewId(), Customer, pinCode, unverifiedWithdrawalLimit, debitInterest));
        }

        @Override
        public void createDepositAccount(int pinCode, double startBalance) throws NotLoggedException, NotPositiveException {
            checkCustomer(Customer);
            accountStorage.add(new DepositAccount(getNewId(), Customer, pinCode, unverifiedWithdrawalLimit, startBalance, depositInterests, depositRemaining));
        }

        @Override
        public void updatePassport(int series, int number) throws NotPositiveException {
            Customer.updatePassport(series, number);
        }

        @Override
        public void updateAddress(String address) {
            Customer.updateAddress(address);
        }

        @Override
        public Iterable<String> getAccountsList() throws NotLoggedException {
            checkCustomer(Customer);
            Collection<String> accounts = new ArrayList<String>();
            for (IAccount account : getAccountsByCustomer(Customer)) {
                accounts.add(account.toString());
            }

            return accounts;
        }

        @Override
        public Iterable<String> getTransactionsList(long accountNumber) throws NotLoggedException, AccountNotFoundException {
            checkCustomer(Customer);
            IAccount account = getAccountByNumber(accountNumber);
            if (account == null || account.getOwner() != Customer) {
                throw new AccountNotFoundException();
            }

            Collection<String> transactions = new ArrayList<>();
            for (ITransaction transaction : account.getTransactions()) {
                transactions.add(transaction.toString());
            }

            return transactions;
        }

        @Override
        public Iterable<String> getNotifications() throws NotLoggedException {
            checkCustomer(Customer);
            return Customer.getNotifications();
        }

        @Override
        public void enableNotifications(long accountNumber) throws NotLoggedException, AccountNotFoundException {
            checkCustomer(Customer);
            IAccount account = getAccountByNumber(accountNumber);
            if (account == null || account.getOwner() != Customer) {
                throw new AccountNotFoundException();
            }

            account.enableNotifications();
        }

        @Override
        public void disableNotifications(long accountNumber) throws NotLoggedException, AccountNotFoundException {
            checkCustomer(Customer);
            IAccount account = getAccountByNumber(accountNumber);
            if (account == null || account.getOwner() != Customer) {
                throw new AccountNotFoundException();
            }

            account.disableNotifications();
        }

        @Override
        public void subMailing() throws NotLoggedException {
            checkCustomer(Customer);
            mailingSubscribers.add(Customer);
        }

        @Override
        public void unsubMailing() throws NotLoggedException {
            checkCustomer(Customer);
            mailingSubscribers.remove(Customer);
        }

        @Override
        public void logOut() throws NotLoggedException {
            checkCustomer(Customer);
            Customer = null;
        }

        @Override
        public void logIn(long phone) throws LoggedException, CustomerNotFound {
            if (Customer != null){
                throw new LoggedException();
            }

            Customer = getCustomerByPhone(phone);
            if (Customer == null){
                throw new CustomerNotFound();
            }
        }

        private  void checkCustomer(ICustomer Customer) throws NotLoggedException {
            if (Customer == null){
                throw new NotLoggedException();
            }
        }
    }

    private class Machine implements IMachine {
        @Override
        public void withdraw(long accountNumber, int pinCode, double amount) throws AccountNotFoundException, WrongPinCodeException, NotPositiveException, NotPossibleBalanceChangeException {
            IAccount account = getAccountByNumber(accountNumber);
            if (account == null) {
                throw new AccountNotFoundException();
            }
            if (!account.checkPin(pinCode)){
                throw new WrongPinCodeException();
            }

            new WithdrawalTransaction(amount).execute(account);
        }

        @Override
        public void deposit(long accountNumber, double amount) throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException {
            IAccount account = getAccountByNumber(accountNumber);
            if (account == null) {
                throw new AccountNotFoundException();
            }

            new DepositTransaction(amount).execute(account);
        }
    }
}
