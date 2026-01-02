package ir.university.library.ui;

import ir.university.library.model.*;
import ir.university.library.service.AuthService;
import ir.university.library.util.ConsoleUtils;

import java.util.Optional;

/**
 * Central menu handler that routes to appropriate user menus
 */
public class MenuHandler {
    private final AuthService authService;
    private boolean running;

    public MenuHandler() {
        this.authService = new AuthService();
        this.running = true;
    }

    /**
     * Start the main menu loop
     */
    public void start() {
        displayWelcome();
        
        while (running) {
            showMainMenu();
        }
        
        displayGoodbye();
    }

    /**
     * Display welcome message
     */
    private void displayWelcome() {
        ConsoleUtils.clearScreen();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Library Management System - Version 3.0     ║");
        System.out.println("║   University of Technology                     ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }

    /**
     * Display goodbye message
     */
    private void displayGoodbye() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   Thank you for using Library Management      ║");
        System.out.println("║   System. Have a great day!                   ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    /**
     * Show main menu
     */
    private void showMainMenu() {
        ConsoleUtils.printHeader("Main Menu");
        System.out.println("1. Guest Access");
        System.out.println("2. Student Login");
        System.out.println("3. Student Registration");
        System.out.println("4. Staff Login");
        System.out.println("5. Manager Login");
        System.out.println("0. Exit");
        ConsoleUtils.printSeparator();

        int choice = ConsoleUtils.readInt("Enter your choice: ");

        handleMainMenuChoice(choice);
    }

    /**
     * Handle main menu choice
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                handleGuestAccess();
                break;
            case 2:
                handleStudentLogin();
                break;
            case 3:
                handleStudentRegistration();
                break;
            case 4:
                handleStaffLogin();
                break;
            case 5:
                handleManagerLogin();
                break;
            case 0:
                running = false;
                break;
            default:
                ConsoleUtils.printError("Invalid choice!");
                ConsoleUtils.pause();
        }
    }

    /**
     * Handle guest access
     */
    private void handleGuestAccess() {
        GuestMenu guestMenu = new GuestMenu();
        guestMenu.show();
    }

    /**
     * Handle student login
     */
    private void handleStudentLogin() {
        ConsoleUtils.printHeader("Student Login");
        
        String username = ConsoleUtils.readLine("Username: ");
        String password = ConsoleUtils.readLine("Password: ");

        Optional<User> userOpt = authService.login(username, password);

        if (userOpt.isPresent() && userOpt.get() instanceof Student) {
            Student student = (Student) userOpt.get();
            ConsoleUtils.printSuccess("Login successful! Welcome, " + student.getUsername());
            ConsoleUtils.pause();
            
            StudentMenu studentMenu = new StudentMenu(student);
            studentMenu.show();
            
            ConsoleUtils.printInfo("Logged out successfully.");
        } else {
            ConsoleUtils.printError("Invalid credentials or not a student account!");
        }
        ConsoleUtils.pause();
    }

    /**
     * Handle student registration
     */
    private void handleStudentRegistration() {
        ConsoleUtils.printHeader("Student Registration");
        
        String username = ConsoleUtils.readLine("Choose a username: ");
        String password = ConsoleUtils.readLine("Choose a password: ");
        String confirmPassword = ConsoleUtils.readLine("Confirm password: ");

        if (!password.equals(confirmPassword)) {
            ConsoleUtils.printError("Passwords do not match!");
            ConsoleUtils.pause();
            return;
        }

        String studentId = ConsoleUtils.readLine("Student ID: ");
        String fullName = ConsoleUtils.readLine("Full Name: ");
        String email = ConsoleUtils.readLine("Email: ");

        try {
            boolean success = authService.registerStudent(username, password, studentId, fullName, email);
            
            if (success) {
                ConsoleUtils.printSuccess("Registration successful! You can now login.");
            } else {
                ConsoleUtils.printError("Username already exists!");
            }
        } catch (Exception e) {
            ConsoleUtils.printError("Error: " + e.getMessage());
        }
        
        ConsoleUtils.pause();
    }

    /**
     * Handle staff login
     */
    private void handleStaffLogin() {
        ConsoleUtils.printHeader("Staff Login");
        
        String username = ConsoleUtils.readLine("Username: ");
        String password = ConsoleUtils.readLine("Password: ");

        Optional<User> userOpt = authService.login(username, password);

        if (userOpt.isPresent() && userOpt.get() instanceof Staff) {
            Staff staff = (Staff) userOpt.get();
            ConsoleUtils.printSuccess("Login successful! Welcome, " + staff.getUsername());
            ConsoleUtils.pause();
            
            StaffMenu staffMenu = new StaffMenu(staff);
            staffMenu.show();
            
            ConsoleUtils.printInfo("Logged out successfully.");
        } else {
            ConsoleUtils.printError("Invalid credentials or not a staff account!");
        }
        ConsoleUtils.pause();
    }

    /**
     * Handle manager login
     */
    private void handleManagerLogin() {
        ConsoleUtils.printHeader("Manager Login");
        
        String username = ConsoleUtils.readLine("Username: ");
        String password = ConsoleUtils.readLine("Password: ");

        Optional<User> userOpt = authService.login(username, password);

        if (userOpt.isPresent() && userOpt.get() instanceof Manager) {
            Manager manager = (Manager) userOpt.get();
            ConsoleUtils.printSuccess("Login successful! Welcome, " + manager.getUsername());
            ConsoleUtils.pause();
            
            ManagerMenu managerMenu = new ManagerMenu(manager);
            managerMenu.show();
            
            ConsoleUtils.printInfo("Logged out successfully.");
        } else {
            ConsoleUtils.printError("Invalid credentials or not a manager account!");
        }
        ConsoleUtils.pause();
    }

    /**
     * Stop the menu handler
     */
    public void stop() {
        running = false;
    }

    /**
     * Check if handler is running
     */
    public boolean isRunning() {
        return running;
    }
}