package library.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String username;
    private String password;
    private boolean active;

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
