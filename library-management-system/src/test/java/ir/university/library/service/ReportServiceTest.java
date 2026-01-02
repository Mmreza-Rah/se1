package ir.university.library.service;

import ir.university.library.model.*;
import ir.university.library.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for report service
 * Covers all report scenarios from requirements
 */
class ReportServiceTest {
    private ReportService reportService;
    private BorrowService borrowService;
    private BookService bookService;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;
    
    private Student testStudent;
    private Staff testStaff;

    @BeforeEach
    void setUp() {
        reportService = new ReportService();
        borrowService = new BorrowService();
        bookService = new BookService();
        userRepository = UserRepository.getInstance();
        bookRepository = BookRepository.getInstance();
        borrowRepository = BorrowRepository.getInstance();
        
        userRepository.clear();
        bookRepository.clear();
        borrowRepository.clear();
        
        testStudent = new Student("student1", "pass123", "S001", "Test Student", "test@test.com");
        testStaff = new Staff("staff1", "pass456", "ST001", "Test Staff");
        
        userRepository.save(testStudent);
        userRepository.save(testStaff);
    }

    @AfterEach
    void tearDown() {
        userRepository.clear();
        bookRepository.clear();
        borrowRepository.clear();
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û´-Û±: ØªÙˆÙ„ÛŒØ¯ Ú¯Ø²Ø§Ø±Ø´ Ø¨Ø±Ø§ÛŒ ÛŒÚ© Ø¯Ø§Ù†Ø´Ø¬Ùˆ
    @Test
    @DisplayName("4-1: Generate student report with correct statistics")
    void testGenerateStudentReport() {
        // Create and approve 3 borrow requests
        Book book1 = bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        Book book2 = bookService.registerBook("Book 2", "Author 2", 2024, "ISBN2", testStaff.getUsername());
        Book book3 = bookService.registerBook("Book 3", "Author 3", 2024, "ISBN3", testStaff.getUsername());
        
        // FIX: Use future dates to avoid validation error
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        // Borrow 1: Returned on time
        BorrowRequest req1 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book1.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req1.getRequestId(), testStaff.getUsername());
        BorrowRecord record1 = borrowRepository.findRecordsByStudent(testStudent.getUsername()).get(0);
        borrowService.returnBook(record1.getRecordId(), testStaff.getUsername());
        
        // Borrow 2: Returned late
        BorrowRequest req2 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book2.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req2.getRequestId(), testStaff.getUsername());
        // FIX: Refresh the list to get the actual record reference
        List<BorrowRecord> records = borrowRepository.findRecordsByStudent(testStudent.getUsername());
        BorrowRecord record2 = records.stream()
            .filter(r -> r.getBookId().equals(book2.getBookId()))
            .findFirst()
            .orElseThrow();
        // Manually set late return
        record2.setActualReturnDate(endDate.plusDays(3).atStartOfDay());
        record2.setReturned(true);
        record2.setReceivedBy(testStaff.getUsername());
        borrowRepository.saveRecord(record2);
        bookService.setBookAvailability(book2.getBookId(), true);
        
        // Borrow 3: Not returned yet (still active)
        BorrowRequest req3 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book3.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req3.getRequestId(), testStaff.getUsername());
        
        // Generate report
        ReportService.StudentStats stats = reportService.getStudentStats(testStudent.getUsername());
        
        assertNotNull(stats, "Report should be generated");
        assertEquals(testStudent.getUsername(), stats.username);
        assertEquals(3, stats.totalBorrows, "Total borrows should be 3");
        // FIX: Should be 1 not returned (record3)
        assertEquals(1, stats.notReturned, "Not returned should be 1");
        assertEquals(1, stats.lateReturns, "Late returns should be 1");
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û´-Û²: Ù…Ø­Ø§Ø³Ø¨Ù‡ Ø¢Ù…Ø§Ø± Ú©Ù„ÛŒ Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡
    @Test
    @DisplayName("4-2: Library stats should calculate average borrow duration correctly")
    void testLibraryStatsAverageDuration() {
        // Create books
        Book book1 = bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        Book book2 = bookService.registerBook("Book 2", "Author 2", 2024, "ISBN2", testStaff.getUsername());
        
        // FIX: Use current/future dates
        LocalDate startDate1 = LocalDate.now();
        LocalDate endDate1 = startDate1.plusDays(7);
        
        // Borrow 1: 5 days (from startDate1 to startDate1+5)
        BorrowRequest req1 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book1.getBookId(), startDate1, endDate1
        );
        borrowService.approveRequest(req1.getRequestId(), testStaff.getUsername());
        // Get the actual record from repository
        List<BorrowRecord> records1 = borrowRepository.findRecordsByStudent(testStudent.getUsername());
        BorrowRecord record1 = records1.stream()
            .filter(r -> r.getBookId().equals(book1.getBookId()))
            .findFirst()
            .orElseThrow();
        // Set return after 5 days
        record1.setActualReturnDate(startDate1.plusDays(5).atStartOfDay());
        record1.setReturned(true);
        record1.setReceivedBy(testStaff.getUsername());
        borrowRepository.saveRecord(record1);
        bookService.setBookAvailability(book1.getBookId(), true);
        
        // Borrow 2: 10 days (from startDate2 to startDate2+10)
        LocalDate startDate2 = LocalDate.now();
        BorrowRequest req2 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book2.getBookId(), startDate2, startDate2.plusDays(7)
        );
        borrowService.approveRequest(req2.getRequestId(), testStaff.getUsername());
        // Get the actual record from repository
        List<BorrowRecord> records2 = borrowRepository.findRecordsByStudent(testStudent.getUsername());
        BorrowRecord record2 = records2.stream()
            .filter(r -> r.getBookId().equals(book2.getBookId()))
            .findFirst()
            .orElseThrow();
        // Set return after 10 days
        record2.setActualReturnDate(startDate2.plusDays(10).atStartOfDay());
        record2.setReturned(true);
        record2.setReceivedBy(testStaff.getUsername());
        borrowRepository.saveRecord(record2);
        bookService.setBookAvailability(book2.getBookId(), true);
        
        // Get stats
        ReportService.BorrowStats stats = reportService.getBorrowStats();
        
        assertNotNull(stats);
        assertEquals(2, stats.totalRequests, "Total requests should be 2");
        assertEquals(2, stats.approvedBorrows, "Approved borrows should be 2");
        
        // Average: (5 + 10) / 2 = 7.5
        assertEquals(7.5, stats.avgBorrowDuration, 0.1, 
                    "Average borrow duration should be 7.5 days");
    }

    // Additional report tests
    @Test
    @DisplayName("Get staff performance report")
    void testGetStaffPerformance() {
        // Staff registers 2 books
        Book book1 = bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        Book book2 = bookService.registerBook("Book 2", "Author 2", 2024, "ISBN2", testStaff.getUsername());
        
        // Staff approves 2 borrows
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req1 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book1.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req1.getRequestId(), testStaff.getUsername());
        
        BorrowRequest req2 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book2.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req2.getRequestId(), testStaff.getUsername());
        
        // Staff receives 1 return
        BorrowRecord record1 = borrowRepository.findRecordsByStudent(testStudent.getUsername()).get(0);
        borrowService.returnBook(record1.getRecordId(), testStaff.getUsername());
        
        ReportService.StaffPerformance performance = reportService.getStaffPerformance(
            testStaff.getUsername()
        );
        
        assertNotNull(performance);
        assertEquals(testStaff.getUsername(), performance.username);
        assertEquals(2, performance.booksRegistered, "Should have registered 2 books");
        assertEquals(2, performance.booksLent, "Should have lent 2 books");
        assertEquals(1, performance.booksReceived, "Should have received 1 book");
    }

    @Test
    @DisplayName("Get top 10 students with most delays")
    void testGetTop10StudentsWithMostDelays() {
        // Create multiple students with different delay patterns
        for (int i = 1; i <= 12; i++) {
            Student student = new Student("student" + i, "pass", "S" + i, "Student " + i, "s" + i + "@test.com");
            userRepository.save(student);
            
            Book book = bookService.registerBook("Book " + i, "Author " + i, 2024, 
                                                "ISBN" + i, testStaff.getUsername());
            
            // FIX: Use current date for startDate
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(7);
            
            BorrowRequest req = borrowService.createBorrowRequest(
                student.getUsername(), book.getBookId(), startDate, endDate
            );
            borrowService.approveRequest(req.getRequestId(), testStaff.getUsername());
            
            // Create late returns with different delays
            BorrowRecord record = borrowRepository.findRecordsByStudent(student.getUsername()).get(0);
            record.setActualReturnDate(endDate.plusDays(i).atStartOfDay()); // i days late
            record.setReturned(true);
            borrowRepository.saveRecord(record);
            bookService.setBookAvailability(book.getBookId(), true);
        }
        
        List<ReportService.StudentDelayInfo> topDelayers = reportService.getTop10StudentsWithMostDelays();
        
        assertNotNull(topDelayers);
        assertEquals(10, topDelayers.size(), "Should return top 10 only");
        
        // Check that results are sorted in descending order
        for (int i = 0; i < topDelayers.size() - 1; i++) {
            assertTrue(topDelayers.get(i).totalDelayDays >= topDelayers.get(i + 1).totalDelayDays,
                      "Results should be sorted by delay in descending order");
        }
        
        // The most delayed student should be student12 with 12 days delay
        assertEquals("student12", topDelayers.get(0).username);
        assertEquals(12, topDelayers.get(0).totalDelayDays);
    }

    @Test
    @DisplayName("Get general statistics")
    void testGetGeneralStatistics() {
        // Create some data
        Student student2 = new Student("student2", "pass", "S002", "Student 2", "s2@test.com");
        userRepository.save(student2);
        
        Book book1 = bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        Book book2 = bookService.registerBook("Book 2", "Author 2", 2024, "ISBN2", testStaff.getUsername());
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req1 = borrowService.createBorrowRequest(
            testStudent.getUsername(), book1.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req1.getRequestId(), testStaff.getUsername());
        
        BorrowRequest req2 = borrowService.createBorrowRequest(
            student2.getUsername(), book2.getBookId(), startDate, endDate
        );
        borrowService.approveRequest(req2.getRequestId(), testStaff.getUsername());
        
        ReportService.GeneralStats stats = reportService.getGeneralStats();
        
        assertNotNull(stats);
        assertEquals(2, stats.totalStudents, "Should have 2 students");
        assertEquals(2, stats.totalBooks, "Should have 2 books");
        assertEquals(2, stats.totalBorrows, "Should have 2 borrows");
        assertEquals(2, stats.activeBorrows, "Should have 2 active borrows");
    }

    @Test
    @DisplayName("Student report with no borrows")
    void testStudentReportWithNoBorrows() {
        ReportService.StudentStats stats = reportService.getStudentStats(testStudent.getUsername());
        
        assertNotNull(stats);
        assertEquals(0, stats.totalBorrows);
        assertEquals(0, stats.notReturned);
        assertEquals(0, stats.lateReturns);
    }

    @Test
    @DisplayName("Library stats with no borrows")
    void testLibraryStatsWithNoBorrows() {
        ReportService.BorrowStats stats = reportService.getBorrowStats();
        
        assertNotNull(stats);
        assertEquals(0, stats.totalRequests);
        assertEquals(0, stats.approvedBorrows);
        assertEquals(0.0, stats.avgBorrowDuration);
    }
}