package org.example;

import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import services.windows.MainWindow;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws EmptyStringException, NotPositiveException {
        new MainWindow(new Scanner(System.in), System.out).run();
    }
}