package library.model;

/**
 * Student model.
 */
public class Student extends User {
    private boolean active = true;

    public Student(String username, String password) {
        super(username, password, "student");
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Student{" + "username='" + username + '\'' + ", active=" + active + '}';
    }
}
