package ir.university.library.repository;

import ir.university.library.model.BorrowRecord;
import ir.university.library.model.BorrowRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BorrowRepository
 */
class BorrowRepositoryTest {
    private BorrowRepository repository;

    @BeforeEach
    void setUp() {
        repository = BorrowRepository.getInstance();
        repository.clear();
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    @DisplayName("Save and find request")
    void testSaveAndFindRequest() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        BorrowRequest request = new BorrowRequest("student1", "book1", startDate, endDate);
        
        repository.saveRequest(request);
        
        Optional<BorrowRequest> found = repository.findRequestById(request.getRequestId());
        assertTrue(found.isPresent());
        assertEquals(request.getRequestId(), found.get().getRequestId());
    }

    @Test
    @DisplayName("Save null request should throw exception")
    void testSaveNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.saveRequest(null);
        });
    }

    @Test
    @DisplayName("Find all requests")
    void testFindAllRequests() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req1 = new BorrowRequest("student1", "book1", startDate, endDate);
        BorrowRequest req2 = new BorrowRequest("student2", "book2", startDate, endDate);
        
        repository.saveRequest(req1);
        repository.saveRequest(req2);
        
        List<BorrowRequest> requests = repository.findAllRequests();
        assertEquals(2, requests.size());
    }

    @Test
    @DisplayName("Find pending requests")
    void testFindPendingRequests() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req1 = new BorrowRequest("student1", "book1", startDate, endDate);
        BorrowRequest req2 = new BorrowRequest("student2", "book2", startDate, endDate);
        req2.approve("staff1");
        
        repository.saveRequest(req1);
        repository.saveRequest(req2);
        
        List<BorrowRequest> pending = repository.findPendingRequests();
        assertEquals(1, pending.size());
        assertEquals(BorrowRequest.Status.PENDING, pending.get(0).getStatus());
    }

    @Test
    @DisplayName("Find requests for today or yesterday")
    void testFindRequestsForTodayOrYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);
        
        BorrowRequest req1 = new BorrowRequest("student1", "book1", today, today.plusDays(7));
        BorrowRequest req2 = new BorrowRequest("student2", "book2", yesterday, yesterday.plusDays(7));
        BorrowRequest req3 = new BorrowRequest("student3", "book3", twoDaysAgo, twoDaysAgo.plusDays(7));
        
        repository.saveRequest(req1);
        repository.saveRequest(req2);
        repository.saveRequest(req3);
        
        List<BorrowRequest> results = repository.findRequestsForTodayOrYesterday();
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Count all requests")
    void testCountAllRequests() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req1 = new BorrowRequest("student1", "book1", startDate, endDate);
        BorrowRequest req2 = new BorrowRequest("student2", "book2", startDate, endDate);
        
        repository.saveRequest(req1);
        repository.saveRequest(req2);
        
        assertEquals(2, repository.countAllRequests());
    }

    @Test
    @DisplayName("Save and find record")
    void testSaveAndFindRecord() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        BorrowRecord record = new BorrowRecord("req1", "student1", "book1", 
                                               startDate, endDate, "staff1");
        
        repository.saveRecord(record);
        
        Optional<BorrowRecord> found = repository.findRecordById(record.getRecordId());
        assertTrue(found.isPresent());
        assertEquals(record.getRecordId(), found.get().getRecordId());
    }

    @Test
    @DisplayName("Save null record should throw exception")
    void testSaveNullRecord() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.saveRecord(null);
        });
    }

    @Test
    @DisplayName("Find all records")
    void testFindAllRecords() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book2", 
                                             startDate, endDate, "staff1");
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        
        List<BorrowRecord> records = repository.findAllRecords();
        assertEquals(2, records.size());
    }

    @Test
    @DisplayName("Find records by student")
    void testFindRecordsByStudent() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student1", "book2", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec3 = new BorrowRecord("req3", "student2", "book3", 
                                             startDate, endDate, "staff1");
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        repository.saveRecord(rec3);
        
        List<BorrowRecord> student1Records = repository.findRecordsByStudent("student1");
        assertEquals(2, student1Records.size());
    }

    @Test
    @DisplayName("Find records by book")
    void testFindRecordsByBook() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec3 = new BorrowRecord("req3", "student3", "book2", 
                                             startDate, endDate, "staff1");
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        repository.saveRecord(rec3);
        
        List<BorrowRecord> book1Records = repository.findRecordsByBook("book1");
        assertEquals(2, book1Records.size());
    }

    @Test
    @DisplayName("Find active records")
    void testFindActiveRecords() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book2", 
                                             startDate, endDate, "staff1");
        rec2.setReturned(true);
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        
        List<BorrowRecord> activeRecords = repository.findActiveRecords();
        assertEquals(1, activeRecords.size());
        assertFalse(activeRecords.get(0).isReturned());
    }

    @Test
    @DisplayName("Count active records")
    void testCountActiveRecords() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book2", 
                                             startDate, endDate, "staff1");
        rec2.setReturned(true);
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        
        assertEquals(1, repository.countActiveRecords());
    }

    @Test
    @DisplayName("Count records by lent by")
    void testCountRecordsByLentBy() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book2", 
                                             startDate, endDate, "staff1");
        BorrowRecord rec3 = new BorrowRecord("req3", "student3", "book3", 
                                             startDate, endDate, "staff2");
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        repository.saveRecord(rec3);
        
        assertEquals(2, repository.countRecordsByLentBy("staff1"));
        assertEquals(1, repository.countRecordsByLentBy("staff2"));
    }

    @Test
    @DisplayName("Count records by received by")
    void testCountRecordsByReceivedBy() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRecord rec1 = new BorrowRecord("req1", "student1", "book1", 
                                             startDate, endDate, "staff1");
        rec1.setReceivedBy("staff1");
        rec1.setReturned(true);
        
        BorrowRecord rec2 = new BorrowRecord("req2", "student2", "book2", 
                                             startDate, endDate, "staff1");
        rec2.setReceivedBy("staff2");
        rec2.setReturned(true);
        
        BorrowRecord rec3 = new BorrowRecord("req3", "student3", "book3", 
                                             startDate, endDate, "staff1");
        // Not returned yet
        
        repository.saveRecord(rec1);
        repository.saveRecord(rec2);
        repository.saveRecord(rec3);
        
        assertEquals(1, repository.countRecordsByReceivedBy("staff1"));
        assertEquals(1, repository.countRecordsByReceivedBy("staff2"));
    }

    @Test
    @DisplayName("Clear repository")
    void testClearRepository() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        
        BorrowRequest req = new BorrowRequest("student1", "book1", startDate, endDate);
        BorrowRecord rec = new BorrowRecord("req1", "student1", "book1", 
                                           startDate, endDate, "staff1");
        
        repository.saveRequest(req);
        repository.saveRecord(rec);
        
        assertEquals(1, repository.countAllRequests());
        assertEquals(1, repository.findAllRecords().size());
        
        repository.clear();
        
        assertEquals(0, repository.countAllRequests());
        assertEquals(0, repository.findAllRecords().size());
    }
}