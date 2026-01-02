package ir.university.library.service;

import ir.university.library.model.Book;
import ir.university.library.model.Staff;
import ir.university.library.repository.BookRepository;
import ir.university.library.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for book search functionality
 * Covers all search scenarios from requirements
 */
class BookSearchTest {
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
        
        testStaff = new Staff("staff1", "pass123", "S001", "Test Staff");
        userRepository.save(testStaff);
        
        // Setup test data
        setupTestBooks();
    }

    @AfterEach
    void tearDown() {
        bookRepository.clear();
        userRepository.clear();
    }

    private void setupTestBooks() {
        bookService.registerBook("Java Programming", "John Doe", 2020, "ISBN001", testStaff.getUsername());
        bookService.registerBook("Advanced Java", "Jane Smith", 2021, "ISBN002", testStaff.getUsername());
        // FIX: Changed "Bob Johnson" to "Bob Williams" to avoid matching "John"
        bookService.registerBook("Python Basics", "Bob Williams", 2020, "ISBN003", testStaff.getUsername());
        bookService.registerBook("Data Structures", "Alice Brown", 2022, "ISBN004", testStaff.getUsername());
        bookService.registerBook("Clean Code", "Robert Martin", 2008, "ISBN005", testStaff.getUsername());
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û²-Û±: Ø¬Ø³ØªØ¬Ùˆ ÙÙ‚Ø· Ø¨Ø§ Ø¹Ù†ÙˆØ§Ù†
    @Test
    @DisplayName("2-1: Search by title only should return matching books")
    void testSearchByTitleOnly() {
        List<Book> results = bookService.searchBooks("Java", null, null);
        
        assertEquals(2, results.size(), "Should find 2 books with 'Java' in title");
        assertTrue(results.stream().allMatch(b -> b.getTitle().contains("Java")));
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û²-Û²: Ø¬Ø³ØªØ¬Ùˆ Ø¨Ø§ ØªØ±Ú©ÛŒØ¨ Ù†ÙˆÛŒØ³Ù†Ø¯Ù‡ Ùˆ Ø³Ø§Ù„ Ø§Ù†ØªØ´Ø§Ø±
    @Test
    @DisplayName("2-2: Search by author and year combination should return specific books")
    void testSearchByAuthorAndYear() {
        List<Book> results = bookService.searchBooks(null, 2020, "John");
        
        assertEquals(1, results.size(), "Should find 1 book by John in 2020");
        Book book = results.get(0);
        assertEquals("Java Programming", book.getTitle());
        assertEquals(2020, book.getPublicationYear());
        assertTrue(book.getAuthor().contains("John"));
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û²-Û³: Ø¬Ø³ØªØ¬Ùˆ Ø¨Ø¯ÙˆÙ† Ù‡ÛŒÚ† Ù…Ø¹ÛŒØ§Ø±ÛŒ (Ù‡Ù…Ù‡ Ù¾Ø§Ø±Ø§Ù…ØªØ±Ù‡Ø§ null)
    @Test
    @DisplayName("2-3: Search without any criteria should return all books")
    void testSearchWithoutCriteria() {
        List<Book> results = bookService.searchBooks(null, null, null);
        
        assertEquals(5, results.size(), "Should return all 5 books when no criteria specified");
    }

    // Ø³Ù†Ø§Ø±ÛŒÙˆ Û²-Û´: Ø¬Ø³ØªØ¬ÙˆÛŒÛŒ Ú©Ù‡ Ù‡ÛŒÚ† Ú©ØªØ§Ø¨ÛŒ Ù…Ø·Ø§Ø¨Ù‚Øª Ù†Ø¯Ø§Ø±Ø¯
    @Test
    @DisplayName("2-4: Search with no matches should return empty list")
    void testSearchWithNoMatches() {
        List<Book> results = bookService.searchBooks("Nonexistent Book", null, null);
        
        assertTrue(results.isEmpty(), "Should return empty list when no books match");
    }

    // Additional search tests
    @Test
    @DisplayName("Search by author only")
    void testSearchByAuthorOnly() {
        List<Book> results = bookService.searchBooks(null, null, "Smith");
        
        assertEquals(1, results.size());
        assertEquals("Jane Smith", results.get(0).getAuthor());
    }

    @Test
    @DisplayName("Search by year only")
    void testSearchByYearOnly() {
        List<Book> results = bookService.searchBooks(null, 2020, null);
        
        assertEquals(2, results.size(), "Should find 2 books from 2020");
        assertTrue(results.stream().allMatch(b -> b.getPublicationYear() == 2020));
    }

    @Test
    @DisplayName("Search with all criteria")
    void testSearchWithAllCriteria() {
        List<Book> results = bookService.searchBooks("Java", 2020, "John");
        
        assertEquals(1, results.size());
        Book book = results.get(0);
        assertEquals("Java Programming", book.getTitle());
        assertEquals(2020, book.getPublicationYear());
        assertEquals("John Doe", book.getAuthor());
    }

    @Test
    @DisplayName("Search by title is case insensitive")
    void testSearchIsCaseInsensitive() {
        List<Book> results1 = bookService.searchBooks("java", null, null);
        List<Book> results2 = bookService.searchBooks("JAVA", null, null);
        List<Book> results3 = bookService.searchBooks("Java", null, null);
        
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
        assertEquals(2, results1.size());
    }

    @Test
    @DisplayName("Search by partial title match")
    void testSearchByPartialTitle() {
        List<Book> results = bookService.searchBooks("Code", null, null);
        
        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).getTitle());
    }

    @Test
    @DisplayName("Search by author partial match")
    void testSearchByAuthorPartialMatch() {
        List<Book> results = bookService.searchBooks(null, null, "Martin");
        
        assertEquals(1, results.size());
        assertEquals("Robert Martin", results.get(0).getAuthor());
    }

    @Test
    @DisplayName("Search with empty string should return all books")
    void testSearchWithEmptyString() {
        List<Book> results = bookService.searchBooks("", null, null);
        
        assertEquals(5, results.size());
    }

    @Test
    @DisplayName("Search by title for guest users")
    void testSearchByTitleForGuests() {
        List<Book> results = bookService.searchByTitle("Python");
        
        assertEquals(1, results.size());
        assertEquals("Python Basics", results.get(0).getTitle());
    }

    @Test
    @DisplayName("Search with year in future returns empty")
    void testSearchWithFutureYear() {
        List<Book> results = bookService.searchBooks(null, 2099, null);
        
        assertTrue(results.isEmpty());
    }
}