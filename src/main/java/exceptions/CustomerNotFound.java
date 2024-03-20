package exceptions;

public class CustomerNotFound extends NotFoundException {
    public CustomerNotFound() {
        super("Customer");
    }
}