package entities.centralBank;

import entities.account.IAccount;
import entities.bank.Bank;
import entities.bank.IBank;
import exceptions.AccountNotFoundException;
import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import models.Constants;

import java.util.*;

public class CentralBank implements ICentralBank {
    public CentralBank() {
        bankStorage = new HashSet<>();
        busyBankIdStorage = new HashSet<>();
        random = new Random();
    }

    private Collection<IBank> bankStorage;
    private Collection<Integer> busyBankIdStorage;
    private Random random;

    @Override
    public void createBank(String name,
                           double debitInterest,
                           Map<Double, Double> depositInterests,
                           int depositRemaining,
                           double creditFee,
                           double creditLimit,
                           double unverifiedWithdrawalLimit) throws EmptyStringException {
        int bankId = random.nextInt(Constants.SIX_DIGIT_BOUND);
        while (busyBankIdStorage.contains(bankId)){
            bankId = random.nextInt(Constants.SIX_DIGIT_BOUND);
        }

        bankStorage.add(new Bank(this,
                name,
                bankId,
                debitInterest,
                depositInterests,
                depositRemaining,
                creditFee,
                creditLimit,
                unverifiedWithdrawalLimit));

        busyBankIdStorage.add(bankId);
    }

    @Override
    public void passDays(int days) throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException {
        if (days <= 0) {
            throw new NotPositiveException("Skip days");
        }

        for (int currentDay = 0; currentDay < days; ++currentDay) {
            for (IBank bank : bankStorage) {
                bank.passDay();
            }
        }
    }

    @Override
    public void accrueInterestAndFees() throws NotPositiveException, AccountNotFoundException, NotPossibleBalanceChangeException {
        for (IBank bank : bankStorage) {
            bank.accrueForServices();
        }
    }

    @Override
    public IAccount getAccountByNumber(long number) {
        IAccount account = null;
        for (IBank bank : bankStorage) {
            account = bank.getAccountByNumber(number);
            if (account != null) {
                return account;
            }
        }

        return null;
    }

    @Override
    public IBank getBankByName(String name) {
        for (IBank bank : bankStorage){
            if (Objects.equals(bank.getName(), name)){
                return bank;
            }
        }

        return null;
    }

    @Override
    public Iterable<String> getBanksList() {
        Collection<String> banks = new ArrayList<>();
        for (IBank bank : bankStorage) {
            banks.add(bank.toString());
        }

        return banks;
    }
}
