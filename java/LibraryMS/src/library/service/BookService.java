package library.service;

import library.model.Book;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    // bookId -> Book
    private Map<String, Book> books = new LinkedHashMap<>();
    private int idCounter = 1;

    public BookService() {
        // Pre-load some sample books
        addBook("The Pragmatic Programmer", "David Thomas", "Technology", 3);
        addBook("Clean Code",               "Robert C. Martin", "Technology", 2);
        addBook("To Kill a Mockingbird",    "Harper Lee",    "Fiction",    4);
        addBook("1984",                     "George Orwell", "Fiction",    5);
        addBook("Sapiens",                  "Yuval Noah Harari", "History", 3);
        addBook("The Alchemist",            "Paulo Coelho",  "Fiction",    4);
    }

    public Book addBook(String title, String author, String genre, int copies) {
        String id = String.format("B%03d", idCounter++);
        Book book = new Book(id, title, author, genre, copies);
        books.put(id, book);
        return book;
    }

    public boolean removeBook(String bookId) {
        if (!books.containsKey(bookId)) return false;
        Book b = books.get(bookId);
        // Only remove if no copies are currently issued
        if (b.getAvailableCopies() < b.getTotalCopies()) {
            return false; // some copies still out
        }
        books.remove(bookId);
        return true;
    }

    public boolean updateBook(String bookId, String title, String author, String genre, int copies) {
        Book b = books.get(bookId);
        if (b == null) return false;
        if (!title.isBlank())  b.setTitle(title);
        if (!author.isBlank()) b.setAuthor(author);
        if (!genre.isBlank())  b.setGenre(genre);
        if (copies > 0)        b.setTotalCopies(copies);
        return true;
    }

    public Book findById(String bookId) {
        return books.get(bookId);
    }

    public List<Book> searchByTitle(String keyword) {
        String kw = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String keyword) {
        String kw = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
}
