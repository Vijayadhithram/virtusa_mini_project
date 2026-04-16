package library.model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private int booksIssued;

    public static final int MAX_BOOKS_ALLOWED = 3;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.booksIssued = 0;
    }

    public String getUserId()  { return userId; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }
    public int getBooksIssued(){ return booksIssued; }

    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean canIssueMore() {
        return booksIssued < MAX_BOOKS_ALLOWED;
    }

    public void incrementIssued() { booksIssued++; }
    public void decrementIssued() { if (booksIssued > 0) booksIssued--; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Email: %s | Phone: %s | Books Held: %d/%d",
                userId, name, email, phone, booksIssued, MAX_BOOKS_ALLOWED);
    }
}
