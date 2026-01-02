package ir.university.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an actual borrow record (approved and book handed out)
 */
public class BorrowRecord {
    private String recordId;
    private String requestId;
    private String studentUsername;
    private String bookId;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private String lentBy; // Staff who gave the book
    private String receivedBy; // Staff who received the book back
    private boolean returned;

    public BorrowRecord(String requestId, String studentUsername, String bookId,
                        LocalDate startDate, LocalDate expectedReturnDate, String lentBy) {
        this.recordId = UUID.randomUUID().toString().substring(0, 8);
        this.requestId = requestId;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.startDate = startDate;
        this.expectedReturnDate = expectedReturnDate;
        this.lentBy = lentBy;
        this.returned = false;
    }

    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getLentBy() {
        return lentBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Check if the book was returned late
     */
    public boolean isReturnedLate() {
        if (!returned || actualReturnDate == null) {
            return false;
        }
        LocalDate actualReturnLocalDate = actualReturnDate.toLocalDate();
        return actualReturnLocalDate.isAfter(expectedReturnDate);
    }

    /**
     * Get delay in days (0 if not late)
     */
    public long getDelayDays() {
        if (!isReturnedLate()) {
            return 0;
        }
        LocalDate actualReturnLocalDate = actualReturnDate.toLocalDate();
        return ChronoUnit.DAYS.between(expectedReturnDate, actualReturnLocalDate);
    }

    /**
     * Get total borrow duration in days
     */
    public long getBorrowDuration() {
        if (!returned || actualReturnDate == null) {
            return 0;
        }
        LocalDate actualReturnLocalDate = actualReturnDate.toLocalDate();
        return ChronoUnit.DAYS.between(startDate, actualReturnLocalDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecord that = (BorrowRecord) o;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "recordId='" + recordId + '\'' +
                ", student='" + studentUsername + '\'' +
                ", bookId='" + bookId + '\'' +
                ", startDate=" + startDate +
                ", expectedReturn=" + expectedReturnDate +
                ", returned=" + returned +
                '}';
    }
}