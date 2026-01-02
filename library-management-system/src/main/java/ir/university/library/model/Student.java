package ir.university.library.model;

/**
 * Student user in the library system
 */
public class Student extends User {
    private String studentId;
    private String fullName;
    private String email;

    public Student(String username, String password) {
        super(username, password);
    }

    public Student(String username, String password, String studentId, String fullName, String email) {
        super(username, password);
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUserType() {
        return "STUDENT";
    }

    @Override
    public String toString() {
        return "Student{" +
                "username='" + getUsername() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", active=" + isActive() +
                '}';
    }
}