package library.model;

/**
 * Librarian model.
 */
public class Librarian extends User {
    private int booksAdded = 0;
    private int booksLent = 0;
    private int booksReceived = 0;

    public Librarian(String username, String password) {
        super(username, password, "librarian");
    }

    public int getBooksAdded() { return booksAdded; }
    public void incBooksAdded() { booksAdded++; }

    public int getBooksLent() { return booksLent; }
    public void incBooksLent() { booksLent++; }

    public int getBooksReceived() { return booksReceived; }
    public void incBooksReceived() { booksReceived++; }

    @Override
    public String toString() {
        return "Librarian{" + "username='" + username + '\'' + '}';
    }
}
