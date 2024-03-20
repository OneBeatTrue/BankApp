package services.windows;

import exceptions.EmptyStringException;
import exceptions.NotPositiveException;

public interface IWindow {
    public void run() throws EmptyStringException, NotPositiveException;
}
