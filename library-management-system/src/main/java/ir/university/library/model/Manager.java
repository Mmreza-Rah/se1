package ir.university.library.model;

/**
 * Library manager with administrative privileges
 */
public class Manager extends User {
    private String fullName;

    public Manager(String username, String password) {
        super(username, password);
    }

    public Manager(String username, String password, String fullName) {
        super(username, password);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getUserType() {
        return "MANAGER";
    }

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + getUsername() + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}