package ir.university.library.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Book entity in the library system
 */
public class Book {
    private String bookId;
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
    private boolean available;
    private LocalDateTime registeredAt;
    private String registeredBy; // Staff username

    public Book(String title, String author, int publicationYear, String isbn, String registeredBy) {
        this.bookId = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.available = true;
        this.registeredAt = LocalDateTime.now();
        this.registeredBy = registeredBy;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + publicationYear +
                ", isbn='" + isbn + '\'' +
                ", available=" + available +
                '}';
    }
}