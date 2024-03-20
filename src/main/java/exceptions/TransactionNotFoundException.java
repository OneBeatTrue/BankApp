package exceptions;

public class TransactionNotFoundException extends NotFoundException{
    public TransactionNotFoundException() {
        super("Transaction");
    }
}
