package org.example;

import exceptions.EmptyStringException;
import exceptions.NotPositiveException;
import services.applications.MainApplication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws EmptyStringException, NotPositiveException {
        new MainApplication(new Scanner(System.in), System.out).run();
    }
}