package library.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private int year;
    private boolean available;

    public Book(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = true;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %s | Author: %s | Year: %d | Available: %s",
                id, title, author, year, available ? "Yes" : "No");
    }
}
