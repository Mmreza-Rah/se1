package ir.university.library.repository;

import ir.university.library.model.BorrowRecord;
import ir.university.library.model.BorrowRequest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing borrow requests and records in memory
 */
public class BorrowRepository {
    private final Map<String, BorrowRequest> requests;
    private final Map<String, BorrowRecord> records;
    private static BorrowRepository instance;

    private BorrowRepository() {
        this.requests = new HashMap<>();
        this.records = new HashMap<>();
    }

    public static synchronized BorrowRepository getInstance() {
        if (instance == null) {
            instance = new BorrowRepository();
        }
        return instance;
    }

    // Request operations
    public void saveRequest(BorrowRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        requests.put(request.getRequestId(), request);
    }

    public Optional<BorrowRequest> findRequestById(String requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }

    public List<BorrowRequest> findAllRequests() {
        return new ArrayList<>(requests.values());
    }

    public List<BorrowRequest> findPendingRequests() {
        return requests.values().stream()
                .filter(req -> req.getStatus() == BorrowRequest.Status.PENDING)
                .collect(Collectors.toList());
    }

    public List<BorrowRequest> findRequestsForTodayOrYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        return requests.values().stream()
                .filter(req -> req.getStatus() == BorrowRequest.Status.PENDING)
                .filter(req -> {
                    LocalDate start = req.getStartDate();
                    return start.equals(today) || start.equals(yesterday);
                })
                .collect(Collectors.toList());
    }

    public long countAllRequests() {
        return requests.size();
    }

    // Record operations
    public void saveRecord(BorrowRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null");
        }
        records.put(record.getRecordId(), record);
    }

    public Optional<BorrowRecord> findRecordById(String recordId) {
        return Optional.ofNullable(records.get(recordId));
    }

    public List<BorrowRecord> findAllRecords() {
        return new ArrayList<>(records.values());
    }

    public List<BorrowRecord> findRecordsByStudent(String studentUsername) {
        return records.values().stream()
                .filter(record -> record.getStudentUsername().equals(studentUsername))
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> findRecordsByBook(String bookId) {
        return records.values().stream()
                .filter(record -> record.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> findActiveRecords() {
        return records.values().stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }

    public long countActiveRecords() {
        return records.values().stream()
                .filter(record -> !record.isReturned())
                .count();
    }

    public long countRecordsByLentBy(String staffUsername) {
        return records.values().stream()
                .filter(record -> record.getLentBy().equals(staffUsername))
                .count();
    }

    public long countRecordsByReceivedBy(String staffUsername) {
        return records.values().stream()
                .filter(record -> record.getReceivedBy() != null &&
                        record.getReceivedBy().equals(staffUsername))
                .count();
    }

    public void clear() {
        requests.clear();
        records.clear();
    }
}