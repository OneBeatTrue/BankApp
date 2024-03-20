package entities.transaction;

import entities.account.IAccount;
import exceptions.*;
import models.AccountState;

public class TransferTransaction extends Transaction {
    public TransferTransaction(double amount, IAccount sender, IAccount receiver) throws NotPositiveException, AccountNotFoundException, SameAccountTransferException {
        super(amount);
        if (sender == null) {
            throw new AccountNotFoundException();
        }

        if (receiver == null) {
            throw new AccountNotFoundException();
        }

        if (receiver.getNumber() == sender.getNumber()) {
            throw new SameAccountTransferException();
        }

        this.sender = sender;
        this.receiver = receiver;
        this.withdrawal = new WithdrawalTransaction(amount);
        this.deposit = new DepositTransaction(amount);
        this.undoingProcess = false;
    }

    private IAccount sender;
    private IAccount receiver;
    private WithdrawalTransaction withdrawal;
    private DepositTransaction deposit;
    private boolean undoingProcess;

    @Override
    public void execute(IAccount account) throws AccountNotFoundException, NotPossibleBalanceChangeException, TransactionNotFoundException {
        check(account);
        if (!alreadyOperated) {
            withdrawal.execute(sender);
            sender.visitor().operateTransfer(this);
            deposit.execute(receiver);
            receiver.visitor().operateTransfer(this);
            alreadyOperated = true;
        }
        else {
            if (account.getNumber() == sender.getNumber()) {
                withdrawal.execute(sender);
                sender.visitor().operateTransfer(this);
                return;
            }
            else if (account.getNumber() == receiver.getNumber()) {
                deposit.execute(receiver);
                receiver.visitor().operateTransfer(this);
                return;
            }

            throw new AccountNotFoundException();
        }
    }

    @Override
    public AccountState undo(IAccount account) throws NotPossibleBalanceChangeException {
        check(account);
        if (account.getNumber() != sender.getNumber() && account.getNumber() != receiver.getNumber()) {
            throw new AccountNotFoundException();
        }

        if (!undoingProcess) {
            undoingProcess = true;
            if (account == receiver) {
                sender.cancelTransaction(this);
            }
            else {
                receiver.cancelTransaction(this);
            }
        }

        if (account == receiver) {
            return deposit.state;
        }

        return withdrawal.state;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\tTRF\t"
                + String.format("%.2f", amount)
                + "$"
                + "\tFrom:"
                + sender.getNumber()
                + "\tTo:"
                + receiver.getNumber();
    }

    private void check(IAccount account) {
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (sender == null) {
            throw new AccountNotFoundException();
        }

        if (receiver == null) {
            throw new AccountNotFoundException();
        }
    }
}
