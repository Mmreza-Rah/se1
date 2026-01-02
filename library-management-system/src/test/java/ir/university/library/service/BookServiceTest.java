package ir.university.library.service;

import ir.university.library.model.Book;
import ir.university.library.model.Staff;
import ir.university.library.repository.BookRepository;
import ir.university.library.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookService
 */
class BookServiceTest {
    private BookService bookService;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private Staff testStaff;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        bookRepository = BookRepository.getInstance();
        userRepository = UserRepository.getInstance();
        
        bookRepository.clear();
        userRepository.clear();
        
        testStaff = new Staff("teststaff", "pass123", "S999", "Test Staff");
        userRepository.save(testStaff);
    }

    @AfterEach
    void tearDown() {
        bookRepository.clear();
        userRepository.clear();
    }

    @Test
    void testRegisterBook() {
        Book book = bookService.registerBook("Test Book", "Test Author", 2024, 
                                            "ISBN123", testStaff.getUsername());
        
        assertNotNull(book);
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2024, book.getPublicationYear());
        assertTrue(book.isAvailable());
    }

    @Test
    void testRegisterBookWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.registerBook("", "Test Author", 2024, 
                                    "ISBN123", testStaff.getUsername());
        });
    }

    @Test
    void testRegisterBookWithEmptyAuthor() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.registerBook("Test Book", "", 2024, 
                                    "ISBN123", testStaff.getUsername());
        });
    }

    @Test
    void testUpdateBook() {
        Book book = bookService.registerBook("Old Title", "Old Author", 2020, 
                                            "ISBN123", testStaff.getUsername());
        
        boolean updated = bookService.updateBook(book.getBookId(), "New Title", 
                                                "New Author", 2024, "ISBN456");
        
        assertTrue(updated);
        
        Optional<Book> updatedBook = bookService.getBookById(book.getBookId());
        assertTrue(updatedBook.isPresent());
        assertEquals("New Title", updatedBook.get().getTitle());
        assertEquals("New Author", updatedBook.get().getAuthor());
        assertEquals(2024, updatedBook.get().getPublicationYear());
    }

    @Test
    void testUpdateNonExistentBook() {
        boolean updated = bookService.updateBook("nonexistent", "Title", "Author", 2024, "ISBN");
        assertFalse(updated);
    }

    @Test
    void testSearchBooksByTitle() {
        bookService.registerBook("Java Programming", "John Doe", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Advanced Java", "Jane Smith", 2023, "ISBN2", testStaff.getUsername());
        bookService.registerBook("Python Basics", "Bob Johnson", 2022, "ISBN3", testStaff.getUsername());
        
        List<Book> results = bookService.searchBooks("Java", null, null);
        assertEquals(2, results.size());
    }

    @Test
    void testSearchBooksByYear() {
        bookService.registerBook("Book 2024", "Author A", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Book 2023", "Author B", 2023, "ISBN2", testStaff.getUsername());
        
        List<Book> results = bookService.searchBooks(null, 2024, null);
        assertEquals(1, results.size());
        assertEquals("Book 2024", results.get(0).getTitle());
    }

    @Test
    void testSearchBooksByAuthor() {
        bookService.registerBook("Book 1", "John Doe", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Book 2", "John Doe", 2023, "ISBN2", testStaff.getUsername());
        bookService.registerBook("Book 3", "Jane Smith", 2022, "ISBN3", testStaff.getUsername());
        
        List<Book> results = bookService.searchBooks(null, null, "John");
        assertEquals(2, results.size());
    }

    @Test
    void testSearchBooksWithMultipleFilters() {
        bookService.registerBook("Java Programming", "John Doe", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Java Advanced", "Jane Smith", 2024, "ISBN2", testStaff.getUsername());
        bookService.registerBook("Java Programming", "Bob Johnson", 2023, "ISBN3", testStaff.getUsername());
        
        List<Book> results = bookService.searchBooks("Java", 2024, "John");
        assertEquals(1, results.size());
        assertEquals("Java Programming", results.get(0).getTitle());
    }

    @Test
    void testGetBookById() {
        Book book = bookService.registerBook("Test Book", "Test Author", 2024, 
                                            "ISBN123", testStaff.getUsername());
        
        Optional<Book> found = bookService.getBookById(book.getBookId());
        assertTrue(found.isPresent());
        assertEquals(book.getBookId(), found.get().getBookId());
    }

    @Test
    void testGetTotalBookCount() {
        bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Book 2", "Author 2", 2023, "ISBN2", testStaff.getUsername());
        bookService.registerBook("Book 3", "Author 3", 2022, "ISBN3", testStaff.getUsername());
        
        assertEquals(3, bookService.getTotalBookCount());
    }

    @Test
    void testSetBookAvailability() {
        Book book = bookService.registerBook("Test Book", "Test Author", 2024, 
                                            "ISBN123", testStaff.getUsername());
        
        assertTrue(book.isAvailable());
        
        bookService.setBookAvailability(book.getBookId(), false);
        
        Optional<Book> updated = bookService.getBookById(book.getBookId());
        assertTrue(updated.isPresent());
        assertFalse(updated.get().isAvailable());
    }

    @Test
    void testStaffStatisticsUpdate() {
        bookService.registerBook("Book 1", "Author 1", 2024, "ISBN1", testStaff.getUsername());
        bookService.registerBook("Book 2", "Author 2", 2023, "ISBN2", testStaff.getUsername());
        
        Optional<ir.university.library.model.User> updatedStaff = userRepository.findByUsername(testStaff.getUsername());
        assertTrue(updatedStaff.isPresent());
        assertTrue(updatedStaff.get() instanceof Staff);
        assertEquals(2, ((Staff) updatedStaff.get()).getBooksRegistered());
    }
}