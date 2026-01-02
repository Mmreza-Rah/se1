package ir.university.library.repository;

import ir.university.library.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookRepository
 */
class BookRepositoryTest {
    private BookRepository repository;
    private Book testBook;

    @BeforeEach
    void setUp() {
        repository = BookRepository.getInstance();
        repository.clear();
        testBook = new Book("Test Book", "Test Author", 2024, "ISBN123", "staff1");
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    @DisplayName("Save and find book")
    void testSaveAndFindBook() {
        repository.save(testBook);
        
        Optional<Book> found = repository.findById(testBook.getBookId());
        assertTrue(found.isPresent());
        assertEquals(testBook.getBookId(), found.get().getBookId());
        assertEquals("Test Book", found.get().getTitle());
    }

    @Test
    @DisplayName("Save null book should throw exception")
    void testSaveNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    @DisplayName("Find all books")
    void testFindAllBooks() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "ISBN2", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        
        List<Book> books = repository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("Search books by title")
    void testSearchBooksByTitle() {
        Book book1 = new Book("Java Programming", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Advanced Java", "Author 2", 2023, "ISBN2", "staff1");
        Book book3 = new Book("Python Basics", "Author 3", 2022, "ISBN3", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        
        List<Book> results = repository.search("Java", null, null);
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Search books by year")
    void testSearchBooksByYear() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2024, "ISBN2", "staff1");
        Book book3 = new Book("Book 3", "Author 3", 2023, "ISBN3", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        
        List<Book> results = repository.search(null, 2024, null);
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Search books by author")
    void testSearchBooksByAuthor() {
        Book book1 = new Book("Book 1", "John Doe", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "John Smith", 2023, "ISBN2", "staff1");
        Book book3 = new Book("Book 3", "Jane Doe", 2022, "ISBN3", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        
        List<Book> results = repository.search(null, null, "John");
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Search with multiple filters")
    void testSearchWithMultipleFilters() {
        Book book1 = new Book("Java Programming", "John Doe", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Java Advanced", "Jane Smith", 2024, "ISBN2", "staff1");
        Book book3 = new Book("Python Basics", "John Doe", 2024, "ISBN3", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        
        List<Book> results = repository.search("Java", 2024, "John");
        assertEquals(1, results.size());
        assertEquals("Java Programming", results.get(0).getTitle());
    }

    @Test
    @DisplayName("Search by title only")
    void testSearchByTitleOnly() {
        Book book1 = new Book("Python Programming", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Java Programming", "Author 2", 2023, "ISBN2", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        
        List<Book> results = repository.searchByTitle("Python");
        assertEquals(1, results.size());
        assertEquals("Python Programming", results.get(0).getTitle());
    }

    @Test
    @DisplayName("Find available books")
    void testFindAvailableBooks() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "ISBN2", "staff1");
        book2.setAvailable(false);
        
        repository.save(book1);
        repository.save(book2);
        
        List<Book> availableBooks = repository.findAvailableBooks();
        assertEquals(1, availableBooks.size());
        assertTrue(availableBooks.get(0).isAvailable());
    }

    @Test
    @DisplayName("Count all books")
    void testCountAll() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "ISBN2", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        
        assertEquals(2, repository.countAll());
    }

    @Test
    @DisplayName("Count by registered by")
    void testCountByRegisteredBy() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "ISBN2", "staff1");
        Book book3 = new Book("Book 3", "Author 3", 2022, "ISBN3", "staff2");
        
        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        
        assertEquals(2, repository.countByRegisteredBy("staff1"));
        assertEquals(1, repository.countByRegisteredBy("staff2"));
    }

    @Test
    @DisplayName("Delete book")
    void testDeleteBook() {
        repository.save(testBook);
        assertTrue(repository.findById(testBook.getBookId()).isPresent());
        
        repository.delete(testBook.getBookId());
        assertFalse(repository.findById(testBook.getBookId()).isPresent());
    }

    @Test
    @DisplayName("Clear repository")
    void testClearRepository() {
        Book book1 = new Book("Book 1", "Author 1", 2024, "ISBN1", "staff1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "ISBN2", "staff1");
        
        repository.save(book1);
        repository.save(book2);
        assertEquals(2, repository.countAll());
        
        repository.clear();
        assertEquals(0, repository.countAll());
    }
}