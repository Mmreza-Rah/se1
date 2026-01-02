package ir.university.library.repository;

import ir.university.library.model.Book;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing books in memory
 */
public class BookRepository {
    private final Map<String, Book> books;
    private static BookRepository instance;

    private BookRepository() {
        this.books = new HashMap<>();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public void save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        books.put(book.getBookId(), book);
    }

    public Optional<Book> findById(String bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> search(String title, Integer year, String author) {
        return books.values().stream()
                .filter(book -> {
                    boolean matches = true;
                    if (title != null && !title.isEmpty()) {
                        matches = book.getTitle().toLowerCase()
                                .contains(title.toLowerCase());
                    }
                    if (matches && year != null) {
                        matches = book.getPublicationYear() == year;
                    }
                    if (matches && author != null && !author.isEmpty()) {
                        matches = book.getAuthor().toLowerCase()
                                .contains(author.toLowerCase());
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    public List<Book> searchByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return new ArrayList<>();
        }
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public long countAll() {
        return books.size();
    }

    public long countByRegisteredBy(String staffUsername) {
        return books.values().stream()
                .filter(book -> book.getRegisteredBy().equals(staffUsername))
                .count();
    }

    public void delete(String bookId) {
        books.remove(bookId);
    }

    public void clear() {
        books.clear();
    }
}