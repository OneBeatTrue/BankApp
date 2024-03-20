package entities.account;

import exceptions.NotPositiveException;

public interface ICreditAccount extends IFeeAccount {
    void updateFee(double newFee) throws NotPositiveException;
    void updateCreditLimit(double newCreditLimit) throws NotPositiveException;
}
