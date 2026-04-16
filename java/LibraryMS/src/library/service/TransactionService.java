package library.service;

import library.model.Book;
import library.model.Transaction;
import library.model.User;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private Map<String, Transaction> transactions = new LinkedHashMap<>();
    private BookService bookService;
    private UserService userService;
    private int idCounter = 1;

    public TransactionService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Issue a book to a user.
     * Returns an error message string on failure, or null on success.
     */
    public String issueBook(String userId, String bookId) {
        User user = userService.findById(userId);
        if (user == null)         return "User not found: " + userId;

        Book book = bookService.findById(bookId);
        if (book == null)         return "Book not found: " + bookId;

        if (!user.canIssueMore())
            return user.getName() + " already holds the maximum of "
                    + User.MAX_BOOKS_ALLOWED + " books.";

        if (!book.isAvailable())
            return "\"" + book.getTitle() + "\" has no available copies right now.";

        // Check if this user already has this book issued
        boolean alreadyHas = transactions.values().stream().anyMatch(t ->
                t.getUserId().equals(userId)
                && t.getBookId().equals(bookId)
                && t.getStatus() == Transaction.Status.ISSUED);
        if (alreadyHas)
            return user.getName() + " already has a copy of \"" + book.getTitle() + "\".";

        String txId = String.format("T%04d", idCounter++);
        Transaction tx = new Transaction(txId, userId, bookId);
        transactions.put(txId, tx);

        book.issueOne();
        user.incrementIssued();
        return null; // success
    }

    /**
     * Return a book.
     * Returns the fine amount (0 if on time).
     * Returns -1 if transaction not found or already returned.
     */
    public double returnBook(String transactionId) {
        Transaction tx = transactions.get(transactionId);
        if (tx == null || tx.getStatus() == Transaction.Status.RETURNED) return -1;

        tx.markReturned();
        double fine = tx.calculateFine();

        Book book = bookService.findById(tx.getBookId());
        if (book != null) book.returnOne();

        User user = userService.findById(tx.getUserId());
        if (user != null) user.decrementIssued();

        return fine;
    }

    public List<Transaction> getActiveByUser(String userId) {
        return transactions.values().stream()
                .filter(t -> t.getUserId().equals(userId)
                          && t.getStatus() == Transaction.Status.ISSUED)
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllByUser(String userId) {
        return transactions.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Transaction> getOverdueTransactions() {
        return transactions.values().stream()
                .filter(Transaction::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    public Transaction findById(String txId) {
        return transactions.get(txId);
    }
}
