package exceptions;

public class NotFoundException extends NullPointerException {
    public NotFoundException(String name) {
        super(name + " not found");
    }
}
