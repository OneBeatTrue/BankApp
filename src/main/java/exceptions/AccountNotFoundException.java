package exceptions;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException() {
        super("Account");
    }
}