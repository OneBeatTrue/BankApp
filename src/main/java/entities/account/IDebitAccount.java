package entities.account;

import exceptions.NotPositiveException;

import java.util.Map;

public interface IDebitAccount extends IDateDependentAccount {
    void updateInterest(double newInterest) throws NotPositiveException;
}
