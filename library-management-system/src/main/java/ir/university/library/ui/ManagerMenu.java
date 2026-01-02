package ir.university.library.ui;

import ir.university.library.model.*;
import ir.university.library.service.*;
import ir.university.library.util.ConsoleUtils;
import ir.university.library.repository.UserRepository;

import java.util.List;

/**
 * Menu interface for manager users
 */
public class ManagerMenu {
    private final Manager manager;
    private final UserRepository userRepository;
    private final ReportService reportService;

    public ManagerMenu(Manager manager) {
        this.manager = manager;
        this.userRepository = UserRepository.getInstance();
        this.reportService = new ReportService();
    }

    public void show() {
        while (true) {
            ConsoleUtils.printHeader("Manager Menu - " + manager.getUsername());
            System.out.println("1. Create staff account");
            System.out.println("2. View staff performance");
            System.out.println("3. View borrow statistics");
            System.out.println("4. View student statistics");
            System.out.println("5. View all staff members");
            System.out.println("0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    createStaff();
                    break;
                case 2:
                    viewStaffPerformance();
                    break;
                case 3:
                    viewBorrowStats();
                    break;
                case 4:
                    viewStudentStats();
                    break;
                case 5:
                    viewAllStaff();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtils.printError("Invalid choice!");
            }
            ConsoleUtils.pause();
        }
    }

    private void createStaff() {
        ConsoleUtils.printHeader("Create Staff Account");
        
        String username = ConsoleUtils.readLine("Enter username: ");
        
        if (userRepository.existsByUsername(username)) {
            ConsoleUtils.printError("Username already exists!");
            return;
        }

        String password = ConsoleUtils.readLine("Enter password: ");
        String staffId = ConsoleUtils.readLine("Enter staff ID: ");
        String fullName = ConsoleUtils.readLine("Enter full name: ");

        Staff staff = new Staff(username, password, staffId, fullName);
        userRepository.save(staff);

        ConsoleUtils.printSuccess("Staff account created successfully!");
        System.out.println("Username: " + username);
    }

    private void viewStaffPerformance() {
        ConsoleUtils.printHeader("Staff Performance Report");
        
        List<Staff> staffList = userRepository.findAllStaff();

        if (staffList.isEmpty()) {
            ConsoleUtils.printInfo("No staff members found.");
            return;
        }

        System.out.println("All Staff Performance:");
        ConsoleUtils.printSeparator();

        for (Staff staff : staffList) {
            ReportService.StaffPerformance performance = reportService.getStaffPerformance(staff.getUsername());
            
            if (performance != null) {
                System.out.println("Username: " + staff.getUsername());
                System.out.println("Name: " + staff.getFullName());
                System.out.println("Books Registered: " + performance.booksRegistered);
                System.out.println("Books Lent: " + performance.booksLent);
                System.out.println("Books Received: " + performance.booksReceived);
                ConsoleUtils.printSeparator();
            }
        }
    }

    private void viewBorrowStats() {
        ConsoleUtils.printHeader("Borrow Statistics");
        
        ReportService.BorrowStats stats = reportService.getBorrowStats();

        System.out.println("Total Requests: " + stats.totalRequests);
        System.out.println("Approved Borrows: " + stats.approvedBorrows);
        System.out.printf("Average Borrow Duration: %.2f days%n", stats.avgBorrowDuration);
    }

    private void viewStudentStats() {
        ConsoleUtils.printHeader("Student Statistics");
        
        List<Student> students = userRepository.findAllStudents();

        if (students.isEmpty()) {
            ConsoleUtils.printInfo("No students found.");
            return;
        }

        System.out.println("Total Students: " + students.size());
        ConsoleUtils.printSeparator();

        // Show top 10 students with most delays
        System.out.println("\nTop 10 Students with Most Delays:");
        ConsoleUtils.printSeparator();

        List<ReportService.StudentDelayInfo> topDelayers = reportService.getTop10StudentsWithMostDelays();

        if (topDelayers.isEmpty()) {
            ConsoleUtils.printInfo("No delayed returns recorded.");
        } else {
            int rank = 1;
            for (ReportService.StudentDelayInfo delayInfo : topDelayers) {
                System.out.println(rank + ". " + delayInfo.username + 
                                 " - Total delay: " + delayInfo.totalDelayDays + " days");
                rank++;
            }
        }

        ConsoleUtils.printSeparator();
        System.out.println("\nDetailed Student Statistics:");
        
        String choice = ConsoleUtils.readLine("Enter student username to view details (or press Enter to skip): ");
        
        if (!choice.isEmpty()) {
            viewDetailedStudentStats(choice);
        }
    }

    private void viewDetailedStudentStats(String username) {
        ConsoleUtils.printHeader("Student Details - " + username);
        
        ReportService.StudentStats stats = reportService.getStudentStats(username);

        System.out.println("Total Borrows: " + stats.totalBorrows);
        System.out.println("Not Returned: " + stats.notReturned);
        System.out.println("Late Returns: " + stats.lateReturns);
    }

    private void viewAllStaff() {
        ConsoleUtils.printHeader("All Staff Members");
        
        List<Staff> staffList = userRepository.findAllStaff();

        if (staffList.isEmpty()) {
            ConsoleUtils.printInfo("No staff members found.");
            return;
        }

        System.out.println("Total Staff: " + staffList.size());
        ConsoleUtils.printSeparator();

        for (Staff staff : staffList) {
            System.out.println("Username: " + staff.getUsername());
            System.out.println("Staff ID: " + staff.getStaffId());
            System.out.println("Name: " + staff.getFullName());
            System.out.println("Active: " + (staff.isActive() ? "Yes" : "No"));
            ConsoleUtils.printSeparator();
        }
    }
}