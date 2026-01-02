package ir.university.library.util;

import java.util.Scanner;

/**
 * Utility methods for console operations
 */
public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    public static void printSeparator() {
        System.out.println("-".repeat(50));
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public static void printError(String message) {
        System.out.println("✗ " + message);
    }

    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }

    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}