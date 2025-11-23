package library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void createAndToggleAvailability() {
        Book b = new Book("X1","T","A",2000);
        assertTrue(b.isAvailable());
        b.setAvailable(false);
        assertFalse(b.isAvailable());
        assertEquals("X1", b.getId());
        assertEquals("T", b.getTitle());
    }
}
