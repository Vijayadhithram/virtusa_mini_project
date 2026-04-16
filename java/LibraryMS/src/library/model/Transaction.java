package library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    public enum Status { ISSUED, RETURNED }

    private String transactionId;
    private String userId;
    private String bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;

    // Fine per day in rupees
    public static final double FINE_PER_DAY = 2.0;
    public static final int LOAN_PERIOD_DAYS = 14;

    public Transaction(String transactionId, String userId, String bookId) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS);
        this.status = Status.ISSUED;
    }

    public String getTransactionId() { return transactionId; }
    public String getUserId()        { return userId; }
    public String getBookId()        { return bookId; }
    public LocalDate getIssueDate()  { return issueDate; }
    public LocalDate getDueDate()    { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Status getStatus()        { return status; }

    public void markReturned() {
        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    public double calculateFine() {
        if (status == Status.ISSUED) {
            // Check if currently overdue
            long overdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return overdue > 0 ? overdue * FINE_PER_DAY : 0;
        } else {
            // Fine at the time of return
            long overdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            return overdue > 0 ? overdue * FINE_PER_DAY : 0;
        }
    }

    public boolean isOverdue() {
        if (status == Status.RETURNED) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        String returned = (returnDate != null) ? returnDate.toString() : "Not yet";
        double fine = calculateFine();
        return String.format(
            "[%s] User: %s | Book: %s | Issued: %s | Due: %s | Returned: %s | Status: %s | Fine: Rs.%.1f",
            transactionId, userId, bookId, issueDate, dueDate, returned, status, fine
        );
    }
}
