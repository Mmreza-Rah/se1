package ir.university.library.service;

import ir.university.library.model.Book;
import ir.university.library.model.Staff;
import ir.university.library.repository.BookRepository;
import ir.university.library.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for book management operations
 */
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService() {
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Register a new book
     */
    public Book registerBook(String title, String author, int publicationYear, 
                            String isbn, String staffUsername) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }

        Book book = new Book(title, author, publicationYear, isbn, staffUsername);
        bookRepository.save(book);

        // Update staff statistics
        userRepository.findByUsername(staffUsername).ifPresent(user -> {
            if (user instanceof Staff) {
                ((Staff) user).incrementBooksRegistered();
                userRepository.save(user);
            }
        });

        return book;
    }

    /**
     * Update book information
     */
    public boolean updateBook(String bookId, String title, String author, 
                             int publicationYear, String isbn) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (title != null && !title.trim().isEmpty()) {
                book.setTitle(title);
            }
            if (author != null && !author.trim().isEmpty()) {
                book.setAuthor(author);
            }
            if (publicationYear > 0) {
                book.setPublicationYear(publicationYear);
            }
            if (isbn != null && !isbn.trim().isEmpty()) {
                book.setIsbn(isbn);
            }
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    /**
     * Search books with filters
     */
    public List<Book> searchBooks(String title, Integer year, String author) {
        return bookRepository.search(title, year, author);
    }

    /**
     * Search books by title only (for guests)
     */
    public List<Book> searchByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    /**
     * Get book by ID
     */
    public Optional<Book> getBookById(String bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get total book count
     */
    public long getTotalBookCount() {
        return bookRepository.countAll();
    }

    /**
     * Set book availability
     */
    public void setBookAvailability(String bookId, boolean available) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setAvailable(available);
            bookRepository.save(book);
        });
    }
}