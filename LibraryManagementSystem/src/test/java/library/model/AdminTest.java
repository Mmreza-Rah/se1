package library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testAdminCreation() {
        Admin admin = new Admin("admin1", "pass123");

        assertEquals("admin1", admin.getUsername());
        assertEquals("pass123", admin.getPassword());
        assertEquals("admin", admin.getRole());
    }

    @Test
    void testSetPassword() {
        Admin admin = new Admin("admin1", "1111");
        admin.setPassword("2222");
        assertEquals("2222", admin.getPassword());
    }
}
