package ir.university.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a borrow request made by a student
 */
public class BorrowRequest {
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private String requestId;
    private String studentUsername;
    private String bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private LocalDateTime requestedAt;
    private String approvedBy; // Staff username
    private LocalDateTime approvedAt;

    public BorrowRequest(String studentUsername, String bookId, LocalDate startDate, LocalDate endDate) {
        this.requestId = UUID.randomUUID().toString().substring(0, 8);
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Status.PENDING;
        this.requestedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public void approve(String staffUsername) {
        this.status = Status.APPROVED;
        this.approvedBy = staffUsername;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject(String staffUsername) {
        this.status = Status.REJECTED;
        this.approvedBy = staffUsername;
        this.approvedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRequest that = (BorrowRequest) o;
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public String toString() {
        return "BorrowRequest{" +
                "requestId='" + requestId + '\'' +
                ", student='" + studentUsername + '\'' +
                ", bookId='" + bookId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}