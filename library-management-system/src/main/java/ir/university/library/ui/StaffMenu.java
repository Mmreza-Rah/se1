package ir.university.library.ui;

import ir.university.library.model.*;
import ir.university.library.service.*;
import ir.university.library.util.ConsoleUtils;
import ir.university.library.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Menu interface for staff users
 */
public class StaffMenu {
    private final Staff staff;
    private final AuthService authService;
    private final BookService bookService;
    private final BorrowService borrowService;
    private final ReportService reportService;
    private final UserRepository userRepository;

    public StaffMenu(Staff staff) {
        this.staff = staff;
        this.authService = new AuthService();
        this.bookService = new BookService();
        this.borrowService = new BorrowService();
        this.reportService = new ReportService();
        this.userRepository = UserRepository.getInstance();
    }

    public void show() {
        while (true) {
            ConsoleUtils.printHeader("Staff Menu - " + staff.getUsername());
            System.out.println("1. Change password");
            System.out.println("2. Register new book");
            System.out.println("3. Search and edit book");
            System.out.println("4. Review borrow requests");
            System.out.println("5. View student history");
            System.out.println("6. Activate/Deactivate student");
            System.out.println("7. Return borrowed book");
            System.out.println("0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    registerBook();
                    break;
                case 3:
                    searchAndEditBook();
                    break;
                case 4:
                    reviewRequests();
                    break;
                case 5:
                    viewStudentHistory();
                    break;
                case 6:
                    toggleStudentStatus();
                    break;
                case 7:
                    returnBook();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtils.printError("Invalid choice!");
            }
            ConsoleUtils.pause();
        }
    }

    private void changePassword() {
        ConsoleUtils.printHeader("Change Password");
        
        String oldPassword = ConsoleUtils.readLine("Enter current password: ");
        String newPassword = ConsoleUtils.readLine("Enter new password: ");
        String confirmPassword = ConsoleUtils.readLine("Confirm new password: ");

        if (!newPassword.equals(confirmPassword)) {
            ConsoleUtils.printError("Passwords do not match!");
            return;
        }

        boolean success = authService.changePassword(staff.getUsername(), oldPassword, newPassword);
        
        if (success) {
            ConsoleUtils.printSuccess("Password changed successfully!");
        } else {
            ConsoleUtils.printError("Current password is incorrect!");
        }
    }

    private void registerBook() {
        ConsoleUtils.printHeader("Register New Book");
        
        String title = ConsoleUtils.readLine("Enter title: ");
        String author = ConsoleUtils.readLine("Enter author: ");
        int year = ConsoleUtils.readInt("Enter publication year: ");
        String isbn = ConsoleUtils.readLine("Enter ISBN: ");

        try {
            Book book = bookService.registerBook(title, author, year, isbn, staff.getUsername());
            ConsoleUtils.printSuccess("Book registered successfully!");
            System.out.println("Book ID: " + book.getBookId());
        } catch (Exception e) {
            ConsoleUtils.printError("Error: " + e.getMessage());
        }
    }

    private void searchAndEditBook() {
        ConsoleUtils.printHeader("Search and Edit Book");
        
        String title = ConsoleUtils.readLine("Enter book title to search: ");
        List<Book> books = bookService.searchByTitle(title);

        if (books.isEmpty()) {
            ConsoleUtils.printInfo("No books found.");
            return;
        }

        System.out.println("\nFound " + books.size() + " book(s):");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " by " + book.getAuthor() + 
                             " (" + book.getBookId() + ")");
        }

        int selection = ConsoleUtils.readInt("\nSelect book to edit (0 to cancel): ");
        
        if (selection < 1 || selection > books.size()) {
            return;
        }

        Book selectedBook = books.get(selection - 1);
        editBook(selectedBook);
    }

    private void editBook(Book book) {
        ConsoleUtils.printHeader("Edit Book - " + book.getTitle());
        
        System.out.println("Leave empty to keep current value");
        String newTitle = ConsoleUtils.readLine("New title [" + book.getTitle() + "]: ");
        String newAuthor = ConsoleUtils.readLine("New author [" + book.getAuthor() + "]: ");
        String yearStr = ConsoleUtils.readLine("New year [" + book.getPublicationYear() + "]: ");
        String newIsbn = ConsoleUtils.readLine("New ISBN [" + book.getIsbn() + "]: ");

        int newYear = book.getPublicationYear();
        if (!yearStr.isEmpty()) {
            try {
                newYear = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                ConsoleUtils.printError("Invalid year!");
                return;
            }
        }

        boolean success = bookService.updateBook(
            book.getBookId(),
            newTitle.isEmpty() ? null : newTitle,
            newAuthor.isEmpty() ? null : newAuthor,
            newYear,
            newIsbn.isEmpty() ? null : newIsbn
        );

        if (success) {
            ConsoleUtils.printSuccess("Book updated successfully!");
        } else {
            ConsoleUtils.printError("Failed to update book!");
        }
    }

    private void reviewRequests() {
        ConsoleUtils.printHeader("Pending Borrow Requests");
        
        List<BorrowRequest> requests = borrowService.getPendingRequestsForReview();

        if (requests.isEmpty()) {
            ConsoleUtils.printInfo("No pending requests for today or yesterday.");
            return;
        }

        System.out.println("Found " + requests.size() + " request(s):");
        ConsoleUtils.printSeparator();

        for (BorrowRequest request : requests) {
            Optional<Book> bookOpt = bookService.getBookById(request.getBookId());
            String bookTitle = bookOpt.map(Book::getTitle).orElse("Unknown");

            System.out.println("Request ID: " + request.getRequestId());
            System.out.println("Student: " + request.getStudentUsername());
            System.out.println("Book: " + bookTitle + " (" + request.getBookId() + ")");
            System.out.println("Period: " + request.getStartDate() + " to " + request.getEndDate());
            ConsoleUtils.printSeparator();
        }

        String requestId = ConsoleUtils.readLine("\nEnter Request ID to approve (or 'cancel'): ");
        
        if (requestId.equalsIgnoreCase("cancel")) {
            return;
        }

        boolean success = borrowService.approveRequest(requestId, staff.getUsername());
        
        if (success) {
            ConsoleUtils.printSuccess("Request approved successfully!");
        } else {
            ConsoleUtils.printError("Failed to approve request!");
        }
    }

    private void viewStudentHistory() {
        ConsoleUtils.printHeader("Student Borrow History");
        
        String username = ConsoleUtils.readLine("Enter student username: ");
        
        List<BorrowRecord> records = borrowService.getStudentBorrowHistory(username);
        
        if (records.isEmpty()) {
            ConsoleUtils.printInfo("No borrow history found for this student.");
            return;
        }

        ReportService.StudentStats stats = reportService.getStudentStats(username);

        System.out.println("\nStatistics:");
        System.out.println("Total Borrows: " + stats.totalBorrows);
        System.out.println("Not Returned: " + stats.notReturned);
        System.out.println("Late Returns: " + stats.lateReturns);
        ConsoleUtils.printSeparator();

        System.out.println("\nBorrow History:");
        for (BorrowRecord record : records) {
            System.out.println("Book ID: " + record.getBookId());
            System.out.println("Start: " + record.getStartDate() + " | Expected: " + record.getExpectedReturnDate());
            System.out.println("Status: " + (record.isReturned() ? "Returned" : "Active"));
            if (record.isReturnedLate()) {
                System.out.println("⚠ Late by " + record.getDelayDays() + " days");
            }
            ConsoleUtils.printSeparator();
        }
    }

    private void toggleStudentStatus() {
        ConsoleUtils.printHeader("Activate/Deactivate Student");
        
        String username = ConsoleUtils.readLine("Enter student username: ");
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            ConsoleUtils.printError("Student not found!");
            return;
        }

        Student student = (Student) userOpt.get();
        boolean newStatus = !student.isActive();
        student.setActive(newStatus);
        userRepository.save(student);

        ConsoleUtils.printSuccess("Student " + (newStatus ? "activated" : "deactivated") + " successfully!");
    }

    private void returnBook() {
        ConsoleUtils.printHeader("Return Borrowed Book");
        
        List<BorrowRecord> activeRecords = borrowService.getActiveBorrows();

        if (activeRecords.isEmpty()) {
            ConsoleUtils.printInfo("No active borrows.");
            return;
        }

        System.out.println("Active Borrows:");
        ConsoleUtils.printSeparator();

        for (BorrowRecord record : activeRecords) {
            Optional<Book> bookOpt = bookService.getBookById(record.getBookId());
            String bookTitle = bookOpt.map(Book::getTitle).orElse("Unknown");

            System.out.println("Record ID: " + record.getRecordId());
            System.out.println("Student: " + record.getStudentUsername());
            System.out.println("Book: " + bookTitle);
            System.out.println("Expected Return: " + record.getExpectedReturnDate());
            ConsoleUtils.printSeparator();
        }

        String recordId = ConsoleUtils.readLine("\nEnter Record ID to return (or 'cancel'): ");
        
        if (recordId.equalsIgnoreCase("cancel")) {
            return;
        }

        boolean success = borrowService.returnBook(recordId, staff.getUsername());
        
        if (success) {
            ConsoleUtils.printSuccess("Book returned successfully!");
        } else {
            ConsoleUtils.printError("Failed to process return!");
        }
    }
}