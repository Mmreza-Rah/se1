package ir.university.library.ui;

import ir.university.library.model.*;
import ir.university.library.service.*;
import ir.university.library.util.ConsoleUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Menu interface for student users
 */
public class StudentMenu {
    private final Student student;
    private final BookService bookService;
    private final BorrowService borrowService;

    public StudentMenu(Student student) {
        this.student = student;
        this.bookService = new BookService();
        this.borrowService = new BorrowService();
    }

    public void show() {
        while (true) {
            ConsoleUtils.printHeader("Student Menu - " + student.getUsername());
            System.out.println("1. Search books");
            System.out.println("2. Request borrow");
            System.out.println("3. View my borrow history");
            System.out.println("0. Logout");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchBooks();
                    break;
                case 2:
                    requestBorrow();
                    break;
                case 3:
                    viewBorrowHistory();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtils.printError("Invalid choice!");
            }
            ConsoleUtils.pause();
        }
    }

    private void searchBooks() {
        ConsoleUtils.printHeader("Search Books");
        
        String title = ConsoleUtils.readLine("Enter title (or press Enter to skip): ");
        String yearStr = ConsoleUtils.readLine("Enter year (or press Enter to skip): ");
        String author = ConsoleUtils.readLine("Enter author (or press Enter to skip): ");
        
        Integer year = null;
        if (!yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                ConsoleUtils.printError("Invalid year format!");
                return;
            }
        }

        List<Book> books = bookService.searchBooks(
            title.isEmpty() ? null : title,
            year,
            author.isEmpty() ? null : author
        );

        if (books.isEmpty()) {
            ConsoleUtils.printInfo("No books found.");
        } else {
            System.out.println("\nFound " + books.size() + " book(s):");
            ConsoleUtils.printSeparator();
            for (Book book : books) {
                System.out.println("ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Year: " + book.getPublicationYear());
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println("Status: " + (book.isAvailable() ? "Available" : "Not Available"));
                ConsoleUtils.printSeparator();
            }
        }
    }

    private void requestBorrow() {
        if (!student.isActive()) {
            ConsoleUtils.printError("Your account is inactive. You cannot borrow books.");
            return;
        }

        ConsoleUtils.printHeader("Request Book Borrow");
        
        String bookId = ConsoleUtils.readLine("Enter book ID: ");
        
        // Verify book exists and is available
        var bookOpt = bookService.getBookById(bookId);
        if (bookOpt.isEmpty()) {
            ConsoleUtils.printError("Book not found!");
            return;
        }
        if (!bookOpt.get().isAvailable()) {
            ConsoleUtils.printError("Book is not available!");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            String startStr = ConsoleUtils.readLine("Enter start date (yyyy-MM-dd): ");
            String endStr = ConsoleUtils.readLine("Enter end date (yyyy-MM-dd): ");
            
            LocalDate startDate = LocalDate.parse(startStr, formatter);
            LocalDate endDate = LocalDate.parse(endStr, formatter);

            BorrowRequest request = borrowService.createBorrowRequest(
                student.getUsername(), bookId, startDate, endDate
            );

            ConsoleUtils.printSuccess("Borrow request created successfully!");
            System.out.println("Request ID: " + request.getRequestId());
            System.out.println("Status: Pending approval");
            
        } catch (DateTimeParseException e) {
            ConsoleUtils.printError("Invalid date format! Use yyyy-MM-dd");
        } catch (Exception e) {
            ConsoleUtils.printError("Error: " + e.getMessage());
        }
    }

    private void viewBorrowHistory() {
        ConsoleUtils.printHeader("My Borrow History");
        
        List<BorrowRecord> records = borrowService.getStudentBorrowHistory(student.getUsername());
        
        if (records.isEmpty()) {
            ConsoleUtils.printInfo("No borrow history found.");
        } else {
            System.out.println("Total borrows: " + records.size());
            ConsoleUtils.printSeparator();
            
            for (BorrowRecord record : records) {
                System.out.println("Record ID: " + record.getRecordId());
                System.out.println("Book ID: " + record.getBookId());
                System.out.println("Start Date: " + record.getStartDate());
                System.out.println("Expected Return: " + record.getExpectedReturnDate());
                System.out.println("Status: " + (record.isReturned() ? "Returned" : "Active"));
                
                if (record.isReturned()) {
                    System.out.println("Returned On: " + record.getActualReturnDate());
                    if (record.isReturnedLate()) {
                        System.out.println("⚠ Returned late by " + record.getDelayDays() + " days");
                    }
                }
                ConsoleUtils.printSeparator();
            }
        }
    }
}