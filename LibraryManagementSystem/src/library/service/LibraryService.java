package library.service;

import library.model.*;
import library.util.FileUtils;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class LibraryService {
    private List<Book> books;
    private List<Student> students;
    private List<Librarian> librarians;
    private List<BorrowRequest> requests;

    private final String bookFile = "data/books.dat";
    private final String studentFile = "data/students.dat";
    private final String librarianFile = "data/librarians.dat";
    private final String requestFile = "data/borrowRequests.dat";

    public LibraryService() {
        books = FileUtils.loadList(bookFile);
        students = FileUtils.loadList(studentFile);
        librarians = FileUtils.loadList(librarianFile);
        requests = FileUtils.loadList(requestFile);
        // پیش‌فرض یک مدیر ساخته می‌شود
        if (librarians.isEmpty()) librarians.add(new Librarian("admin", "admin"));
    }

    // ---------- کتاب ----------
    public void addBook(Book book) { books.add(book); saveBooks(); }
    public void editBook(String id, String title, String author, int year) {
        Book b = getBookById(id);
        if (b != null) { b.setAvailable(true); /* بازگرداندن دسترسی */ }
        saveBooks();
    }
    public List<Book> searchBooks(String title, String author, Integer year) {
        return books.stream()
                .filter(b -> (title == null || b.getTitle().toLowerCase().contains(title.toLowerCase()))
                        && (author == null || b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                        && (year == null || b.getYear() == year))
                .collect(Collectors.toList());
    }
    public Book getBookById(String id) { return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null); }

    // ---------- دانشجو ----------
    public boolean registerStudent(String username, String password) {
        if (students.stream().anyMatch(s -> s.getUsername().equals(username))) return false;
        students.add(new Student(username, password));
        saveStudents(); return true;
    }
    public Student loginStudent(String username, String password) {
        return students.stream().filter(s -> s.getUsername().equals(username) && s.getPassword().equals(password)).findFirst().orElse(null);
    }

    // ---------- کارمند ----------
    public Librarian loginLibrarian(String username, String password) {
        return librarians.stream().filter(l -> l.getUsername().equals(username) && l.getPassword().equals(password)).findFirst().orElse(null);
    }
    public void addLibrarian(String username, String password) {
        librarians.add(new Librarian(username,password)); saveLibrarians();
    }

    // ---------- درخواست ----------
    public void requestBorrow(String studentUsername, String bookId, LocalDate start, LocalDate end) {
        requests.add(new BorrowRequest(studentUsername, bookId, start, end)); saveRequests();
    }
    public List<BorrowRequest> getRequestsForApproval() {
        LocalDate today = LocalDate.now();
        return requests.stream()
                .filter(r -> !r.isApproved() && !r.isReturned() &&
                        (r.getStartDate().equals(today) || r.getStartDate().isBefore(today)))
                .collect(Collectors.toList());
    }
    public void approveRequest(BorrowRequest r) {
        r.setApproved(true); getBookById(r.getBookId()).setAvailable(false); saveRequests(); saveBooks();
    }
    public void returnBook(BorrowRequest r) {
        r.setReturned(true); getBookById(r.getBookId()).setAvailable(true); saveRequests(); saveBooks();
    }

    // ---------- ذخیره داده‌ها ----------
    public void saveBooks() { FileUtils.saveList(books, bookFile); }
    public void saveStudents() { FileUtils.saveList(students, studentFile); }
    public void saveLibrarians() { FileUtils.saveList(librarians, librarianFile); }
    public void saveRequests() { FileUtils.saveList(requests, requestFile); }

    // ---------- آمار ----------
    public int totalStudents() { return students.size(); }
    public int totalBooks() { return books.size(); }
    public int totalBorrowed() { return (int) requests.stream().filter(BorrowRequest::isApproved).count(); }
    public List<Student> topLateStudents() { return students; } // ساده شده
}
