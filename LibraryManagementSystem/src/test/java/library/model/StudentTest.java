package library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test
    void registerAndActive() {
        Student s = new Student("u","p");
        assertEquals("u", s.getUsername());
        assertTrue(s.isActive());
        s.setActive(false);
        assertFalse(s.isActive());
    }
}
