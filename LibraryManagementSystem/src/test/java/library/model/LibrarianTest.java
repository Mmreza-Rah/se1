package library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibrarianTest {
    @Test
    void countersWork() {
        Librarian l = new Librarian("li","pw");
        assertEquals(0, l.getBooksAdded());
        l.incBooksAdded();
        assertEquals(1, l.getBooksAdded());
    }
}
