package library.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BorrowRequestTest {

    @Test
    void testCreateBorrowRequest() {
        LocalDate s = LocalDate.of(2025, 1, 10);
        LocalDate e = LocalDate.of(2025, 1, 20);

        BorrowRequest r = new BorrowRequest("student1", "B101", s, e);

        assertEquals("student1", r.getStudentUsername());
        assertEquals("B101", r.getBookId());
        assertEquals(s, r.getStartDate());
        assertEquals(e, r.getEndDate());
        assertFalse(r.isApproved());
        assertFalse(r.isReturned());
    }

    @Test
    void testApproveAndReturn() {
        BorrowRequest r = new BorrowRequest("s", "b", LocalDate.now(), LocalDate.now().plusDays(7));

        r.setApproved(true);
        assertTrue(r.isApproved());

        r.setReturned(true);
        assertTrue(r.isReturned());
    }
}
