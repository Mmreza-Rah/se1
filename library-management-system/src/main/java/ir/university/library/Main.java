package ir.university.library;

import ir.university.library.ui.MenuHandler;

/**
 * Main entry point for Library Management System
 */
public class Main {
    public static void main(String[] args) {
        MenuHandler menuHandler = new MenuHandler();
        menuHandler.start();
    }
}