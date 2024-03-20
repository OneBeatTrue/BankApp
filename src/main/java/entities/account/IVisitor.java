package entities.account;

import entities.transaction.*;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.TransactionNotFoundException;

/**
 * Interface to separate visitor part of account.
 * (Visits each type of transaction).
 */
public interface IVisitor {
    void operateWithdrawal(WithdrawalTransaction transaction) throws NotPossibleBalanceChangeException, TransactionNotFoundException;
    void operateDeposit(DepositTransaction transaction) throws TransactionNotFoundException;
    void operateInterestSave(InterestSaveTransaction transaction);
    void operateBankServiceAccrual(BankServiceAccrualTransaction transaction) throws TransactionNotFoundException;
    void operateTransfer(TransferTransaction transaction) throws TransactionNotFoundException;
}
