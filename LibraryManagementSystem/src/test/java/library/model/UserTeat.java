package library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("ali", "12345", "student");

        assertEquals("ali", user.getUsername());
        assertEquals("12345", user.getPassword());
        assertEquals("student", user.getRole());
    }

    @Test
    void testSetPassword() {
        User user = new User("ali", "123", "student");
        user.setPassword("newpass");
        assertEquals("newpass", user.getPassword());
    }

    @Test
    void testSetRole() {
        User user = new User("ali", "123", "student");
        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }
}
