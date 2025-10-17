package library.model;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowRequest implements Serializable {
    private String studentUsername;
    private String bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private boolean returned;

    public BorrowRequest(String studentUsername, String bookId, LocalDate startDate, LocalDate endDate) {
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.approved = false;
        this.returned = false;
    }

    public String getStudentUsername() { return studentUsername; }
    public String getBookId() { return bookId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
