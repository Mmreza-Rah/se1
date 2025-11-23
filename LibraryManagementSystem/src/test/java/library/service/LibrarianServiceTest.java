package library.service;

import library.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {
    @Test
    void registerLoginStudentAndSearchBook() {
        LibraryService svc = new LibraryService();
        boolean ok = svc.registerStudent("testuser","pw");
        assertTrue(ok);
        Student s = svc.loginStudent("testuser","pw");
        assertNotNull(s);
        List<Book> found = svc.searchBooks("Java", null, null);
        assertNotNull(found);
    }

    @Test
    void borrowRequestFlowApproveReceiveReturn() {
        LibraryService svc = new LibraryService();
        svc.registerStudent("stu1","p");
        svc.addLibrarian("l1","p");
        Book b = new Book("BX","Title","Auth",2021);
        svc.addBook(b, svc.loginLibrarian("l1","p"));
        svc.requestBorrow("stu1","BX", LocalDate.now(), LocalDate.now().plusDays(7));
        List<BorrowRequest> reqs = svc.getAllRequests();
        assertFalse(reqs.isEmpty());
        BorrowRequest r = reqs.get(0);
        boolean approved = svc.approveRequest(r, svc.loginLibrarian("l1","p"));
        assertTrue(approved);
        assertFalse(b.isAvailable());
        boolean received = svc.recordReceiving(r, svc.loginLibrarian("l1","p"), LocalDate.now());
        assertTrue(received);
        boolean returned = svc.recordReturn(r, LocalDate.now().plusDays(5));
        assertTrue(returned);
        assertTrue(b.isAvailable());
    }

    @Test
    void statsAndReports() {
        LibraryService svc = new LibraryService();
        Map<String,Object> stats = svc.borrowStats();
        assertNotNull(stats.get("requests"));
        Map<String,Object> students = svc.studentsStatsAndTopLate10();
        assertNotNull(students.get("top10Late"));
    }
}
