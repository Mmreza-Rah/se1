package ir.university.library.ui;

import ir.university.library.model.Book;
import ir.university.library.service.BookService;
import ir.university.library.service.ReportService;
import ir.university.library.util.ConsoleUtils;
import ir.university.library.repository.UserRepository;

import java.util.List;

/**
 * Menu interface for guest users
 */
public class GuestMenu {
    private final BookService bookService;
    private final ReportService reportService;
    private final UserRepository userRepository;

    public GuestMenu() {
        this.bookService = new BookService();
        this.reportService = new ReportService();
        this.userRepository = UserRepository.getInstance();
    }

    public void show() {
        while (true) {
            ConsoleUtils.printHeader("Guest Menu");
            System.out.println("1. View registered students count");
            System.out.println("2. Search books by title");
            System.out.println("3. View statistics");
            System.out.println("0. Exit");
            ConsoleUtils.printSeparator();

            int choice = ConsoleUtils.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewStudentCount();
                    break;
                case 2:
                    searchBooks();
                    break;
                case 3:
                    viewStatistics();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtils.printError("Invalid choice!");
            }
            ConsoleUtils.pause();
        }
    }

    private void viewStudentCount() {
        ConsoleUtils.printHeader("Registered Students");
        long count = userRepository.countStudents();
        System.out.println("Total registered students: " + count);
    }

    private void searchBooks() {
        ConsoleUtils.printHeader("Search Books");
        String title = ConsoleUtils.readLine("Enter book title: ");
        
        List<Book> books = bookService.searchByTitle(title);
        
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
                ConsoleUtils.printSeparator();
            }
        }
    }

    private void viewStatistics() {
        ConsoleUtils.printHeader("Library Statistics");
        ReportService.GeneralStats stats = reportService.getGeneralStats();
        
        System.out.println("Total Students: " + stats.totalStudents);
        System.out.println("Total Books: " + stats.totalBooks);
        System.out.println("Total Borrows: " + stats.totalBorrows);
        System.out.println("Currently Borrowed Books: " + stats.activeBorrows);
    }
}