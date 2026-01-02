package ir.university.library.service;

import ir.university.library.model.*;
import ir.university.library.repository.BorrowRepository;
import ir.university.library.repository.BookRepository;
import ir.university.library.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for borrow operations
 */
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService() {
        this.borrowRepository = BorrowRepository.getInstance();
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Create a borrow request
     */
    public BorrowRequest createBorrowRequest(String studentUsername, String bookId,
                                            LocalDate startDate, LocalDate endDate) {
        // Validate student is active
        Optional<User> userOpt = userRepository.findByUsername(studentUsername);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new IllegalArgumentException("Student not found");
        }
        if (!userOpt.get().isActive()) {
            throw new IllegalStateException("Student account is inactive");
        }

        // Validate book exists and is available
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        if (!bookOpt.get().isAvailable()) {
            throw new IllegalStateException("Book is not available");
        }

        // Validate dates
        // FIX: Uncommented validation for past dates
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        BorrowRequest request = new BorrowRequest(studentUsername, bookId, startDate, endDate);
        borrowRepository.saveRequest(request);
        return request;
    }

    /**
     * Get pending requests for today or yesterday
     */
    public List<BorrowRequest> getPendingRequestsForReview() {
        return borrowRepository.findRequestsForTodayOrYesterday();
    }

    /**
     * Approve a borrow request
     */
    public boolean approveRequest(String requestId, String staffUsername) {
        Optional<BorrowRequest> requestOpt = borrowRepository.findRequestById(requestId);
        if (requestOpt.isEmpty()) {
            return false;
        }

        BorrowRequest request = requestOpt.get();
        request.approve(staffUsername);
        borrowRepository.saveRequest(request);

        // Create borrow record
        BorrowRecord record = new BorrowRecord(
            request.getRequestId(),
            request.getStudentUsername(),
            request.getBookId(),
            request.getStartDate(),
            request.getEndDate(),
            staffUsername
        );
        borrowRepository.saveRecord(record);

        // Mark book as unavailable
        bookRepository.findById(request.getBookId()).ifPresent(book -> {
            book.setAvailable(false);
            bookRepository.save(book);
        });

        // Update staff statistics
        userRepository.findByUsername(staffUsername).ifPresent(user -> {
            if (user instanceof Staff) {
                ((Staff) user).incrementBooksLent();
                userRepository.save(user);
            }
        });

        return true;
    }

    /**
     * Return a borrowed book
     */
    public boolean returnBook(String recordId, String staffUsername) {
        Optional<BorrowRecord> recordOpt = borrowRepository.findRecordById(recordId);
        if (recordOpt.isEmpty()) {
            return false;
        }

        BorrowRecord record = recordOpt.get();
        if (record.isReturned()) {
            return false;
        }

        record.setActualReturnDate(LocalDateTime.now());
        record.setReceivedBy(staffUsername);
        record.setReturned(true);
        borrowRepository.saveRecord(record);

        // Mark book as available
        bookRepository.findById(record.getBookId()).ifPresent(book -> {
            book.setAvailable(true);
            bookRepository.save(book);
        });

        // Update staff statistics
        userRepository.findByUsername(staffUsername).ifPresent(user -> {
            if (user instanceof Staff) {
                ((Staff) user).incrementBooksReceived();
                userRepository.save(user);
            }
        });

        return true;
    }

    /**
     * Get borrow history for a student
     */
    public List<BorrowRecord> getStudentBorrowHistory(String studentUsername) {
        return borrowRepository.findRecordsByStudent(studentUsername);
    }

    /**
     * Get active borrows (not returned)
     */
    public List<BorrowRecord> getActiveBorrows() {
        return borrowRepository.findActiveRecords();
    }

    /**
     * Get total request count
     */
    public long getTotalRequestCount() {
        return borrowRepository.countAllRequests();
    }

    /**
     * Get active borrow count
     */
    public long getActiveBorrowCount() {
        return borrowRepository.countActiveRecords();
    }
}