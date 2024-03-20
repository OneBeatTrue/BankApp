package entities.bank;

import exceptions.AccountNotFoundException;
import exceptions.NotPositiveException;
import exceptions.NotPossibleBalanceChangeException;
import exceptions.WrongPinCodeException;

/**
 * Interface that simulates real bank machine.
 */
public interface IMachine {
    /**
     * Withdraw operation.
     * (PIN code is needed)
     */
    void withdraw(long accountNumber, int pinCode, double amount) throws AccountNotFoundException, WrongPinCodeException, NotPositiveException, NotPossibleBalanceChangeException;
    /**
     * Deposit operation.
     * (PIN code is not needed)
     */
    void deposit(long accountNumber, double amount) throws AccountNotFoundException, NotPositiveException, NotPossibleBalanceChangeException;
}
