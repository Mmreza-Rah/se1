package ir.university.library.model;

/**
 * Library staff member
 */
public class Staff extends User {
    private String staffId;
    private String fullName;
    private int booksRegistered;
    private int booksLent;
    private int booksReceived;

    public Staff(String username, String password) {
        super(username, password);
        this.booksRegistered = 0;
        this.booksLent = 0;
        this.booksReceived = 0;
    }

    public Staff(String username, String password, String staffId, String fullName) {
        super(username, password);
        this.staffId = staffId;
        this.fullName = fullName;
        this.booksRegistered = 0;
        this.booksLent = 0;
        this.booksReceived = 0;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBooksRegistered() {
        return booksRegistered;
    }

    public void incrementBooksRegistered() {
        this.booksRegistered++;
    }

    public int getBooksLent() {
        return booksLent;
    }

    public void incrementBooksLent() {
        this.booksLent++;
    }

    public int getBooksReceived() {
        return booksReceived;
    }

    public void incrementBooksReceived() {
        this.booksReceived++;
    }

    @Override
    public String getUserType() {
        return "STAFF";
    }

    @Override
    public String toString() {
        return "Staff{" +
                "username='" + getUsername() + '\'' +
                ", staffId='" + staffId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", booksRegistered=" + booksRegistered +
                ", booksLent=" + booksLent +
                ", booksReceived=" + booksReceived +
                '}';
    }
}