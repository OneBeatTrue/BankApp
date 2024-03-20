package services.applications;

import exceptions.EmptyStringException;
import exceptions.NotPositiveException;

public interface IUserApplication {
    public void run() throws EmptyStringException, NotPositiveException;
}
