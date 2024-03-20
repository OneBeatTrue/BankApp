package entities.account;

import exceptions.NotPositiveException;

import java.util.Map;

public interface IDepositAccount extends IDateDependentAccount {
    void updateInterest(Map<Double, Double> newInterests) throws NotPositiveException;
}
