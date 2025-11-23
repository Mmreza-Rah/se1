package library.service;

import library.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service for in-memory library management.
 */
public class LibraryService {
    private final List<Book> books = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Librarian> librarians = new ArrayList<>();
    public final Admin admin; // single admin
    private final List<BorrowRequest> requests = new ArrayList<>();

    public LibraryService() {
        // seed sample data
        admin = new Admin("admin", "admin");
        librarians.add(new Librarian("lib1","libpass"));
        books.add(new Book("B001","Java Programming","John Doe",2020));
        books.add(new Book("B002","Data Structures","Jane Smith",2018));
        students.add(new Student("ali","1234"));
    }

    // Book methods
    public void addBook(Book book, Librarian by) {
        books.add(book);
        if (by != null) by.incBooksAdded();
    }

    public Book getBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Book> searchBooks(String title, String author, Integer year) {
        return books.stream().filter(b -> {
            boolean ok = true;
            if (title != null && !title.trim().isEmpty())
                ok &= b.getTitle().toLowerCase().contains(title.toLowerCase());
            if (author != null && !author.trim().isEmpty())
                ok &= b.getAuthor().toLowerCase().contains(author.toLowerCase());
            if (year != null)
                ok &= b.getYear() == year;
            return ok;
        }).collect(Collectors.toList());
    }

    // Student methods
    public boolean registerStudent(String username, String password) {
        if (students.stream().anyMatch(s -> s.getUsername().equals(username))) return false;
        students.add(new Student(username,password));
        return true;
    }

    public Student loginStudent(String username, String password) {
        return students.stream().filter(s -> s.login(username,password)).findFirst().orElse(null);
    }

    // Librarian methods
    public void addLibrarian(String username, String password) {
        librarians.add(new Librarian(username,password));
    }

    public Librarian loginLibrarian(String username, String password) {
        return librarians.stream().filter(l -> l.login(username,password)).findFirst().orElse(null);
    }

    public void changeLibrarianPassword(Librarian l, String newPass) {
        if (l != null) l.setPassword(newPass);
    }

    // Borrow request methods
    public void requestBorrow(String studentUsername, String bookId, LocalDate start, LocalDate end) {
        requests.add(new BorrowRequest(studentUsername, bookId, start, end));
    }

    public List<BorrowRequest> getRequestsForApproval() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        return requests.stream()
                .filter(r -> !r.isApproved() && !r.isReturned())
                .filter(r -> r.getStartDate().equals(today) || r.getStartDate().equals(yesterday))
                .collect(Collectors.toList());
    }

    public boolean approveRequest(BorrowRequest r, Librarian l) {
        if (r == null) return false;
        Book b = getBookById(r.getBookId());
        Student s = students.stream().filter(st -> st.getUsername().equals(r.getStudentUsername())).findFirst().orElse(null);
        if (b == null || s == null) return false;
        if (!s.isActive()) return false;
        if (!b.isAvailable()) return false;
        r.setApproved(true);
        if (l != null) l.incBooksLent();
        b.setAvailable(false);
        return true;
    }

    public boolean recordReceiving(BorrowRequest r, Librarian l, LocalDate receiveDate) {
        if (r == null || !r.isApproved() || r.isReturned()) return false;
        r.setReturned(false); // receiving does not mark returned; it just confirms received
        if (l != null) l.incBooksReceived();
        return true;
    }

    public boolean recordReturn(BorrowRequest r, LocalDate returnDate) {
        if (r == null || !r.isApproved() || r.isReturned()) return false;
        r.setReturned(true);
        Book b = getBookById(r.getBookId());
        if (b != null) b.setAvailable(true);
        return true;
    }

    // Reports
    public Map<String, Object> studentHistoryReport(String studentUsername) {
        Map<String,Object> out = new HashMap<>();
        List<BorrowRequest> his = requests.stream().filter(r -> r.getStudentUsername().equals(studentUsername)).collect(Collectors.toList());
        int total = his.size();
        int notReturned = (int) his.stream().filter(r -> r.isApproved() && !r.isReturned()).count();
        int lateCount = (int) his.stream().filter(r -> r.isReturned() && r.getEndDate()!=null && r.getEndDate().isBefore(LocalDate.now())).count();
        out.put("history", his);
        out.put("total", total);
        out.put("notReturned", notReturned);
        out.put("lateCount", lateCount);
        return out;
    }

    public int totalStudents() { return students.size(); }
    public int totalBooks() { return books.size(); }
    public int totalRequests() { return requests.size(); }
    public int totalCurrentlyBorrowed() {
        return (int) requests.stream().filter(r -> r.isApproved() && !r.isReturned()).count();
    }

    public Map<String, Object> borrowStats() {
        Map<String,Object> out = new HashMap<>();
        int reqs = requests.size();
        int lent = (int) requests.stream().filter(BorrowRequest::isApproved).count();
        List<Long> durations = requests.stream()
                .filter(r -> r.isApproved() && r.isReturned())
                // durations are not well-defined in in-memory version; use 0 if missing
                .map(r -> ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()))
                .collect(Collectors.toList());
        double avg = durations.isEmpty() ? 0.0 : durations.stream().mapToLong(Long::longValue).average().orElse(0.0);
        out.put("requests", reqs);
        out.put("lent", lent);
        out.put("avgDays", avg);
        return out;
    }

    public Map<String,Integer> librarianPerformance(String username) {
        Map<String,Integer> out = new HashMap<>();
        Librarian l = librarians.stream().filter(x->x.getUsername().equals(username)).findFirst().orElse(null);
        if (l == null) return out;
        out.put("booksAdded", l.getBooksAdded());
        out.put("booksLent", l.getBooksLent());
        out.put("booksReceived", l.getBooksReceived());
        return out;
    }

    public Map<String,Object> studentsStatsAndTopLate10() {
        Map<String,Object> out = new HashMap<>();
        List<Map<String,Object>> each = new ArrayList<>();
        for (Student s : students) {
            Map<String,Object> m = new HashMap<>();
            List<BorrowRequest> his = requests.stream().filter(r -> r.getStudentUsername().equals(s.getUsername())).collect(Collectors.toList());
            int total = his.size();
            int notReturned = (int) his.stream().filter(r -> r.isApproved() && !r.isReturned()).count();
            int late = (int) his.stream().filter(r -> r.isReturned() && r.getEndDate()!=null && r.getEndDate().isBefore(LocalDate.now())).count();
            m.put("username", s.getUsername());
            m.put("total", total);
            m.put("notReturned", notReturned);
            m.put("lateCount", late);
            each.add(m);
        }
        List<Map<String,Object>> top10 = each.stream()
                .sorted((a,b) -> Integer.compare((int)b.get("lateCount"), (int)a.get("lateCount")))
                .limit(10)
                .collect(Collectors.toList());
        out.put("perStudent", each);
        out.put("top10Late", top10);
        return out;
    }

    // For tests and interaction
    public List<Book> getAllBooks() { return Collections.unmodifiableList(books); }
    public List<Student> getAllStudents() { return Collections.unmodifiableList(students); }
    public List<Librarian> getAllLibrarians() { return Collections.unmodifiableList(librarians); }
    public List<BorrowRequest> getAllRequests() { return Collections.unmodifiableList(requests); }
}
