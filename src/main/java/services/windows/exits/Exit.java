package services.windows.exits;

public class Exit implements IExit {
    public Exit() {
        exit = false;
    }

    private boolean exit;

    @Override
    public boolean isOn() {
        return exit;
    }

    @Override
    public void use() {
        exit = true;
    }
}