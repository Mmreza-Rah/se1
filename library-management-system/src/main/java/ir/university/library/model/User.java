package ir.university.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class for all user types in the library system
 */
public abstract class User {
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private boolean active;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract String getUserType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", type=" + getUserType() +
                ", active=" + active +
                '}';
    }
}