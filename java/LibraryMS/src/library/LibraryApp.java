package library;

import library.model.*;
import library.service.*;
import library.util.ConsoleUtil;

import java.util.List;

public class LibraryApp {

    private static BookService        bookService;
    private static UserService        userService;
    private static TransactionService txService;

    public static void main(String[] args) {
        bookService = new BookService();
        userService = new UserService();
        txService   = new TransactionService(bookService, userService);

        ConsoleUtil.header("LIBRARY MANAGEMENT SYSTEM");
        ConsoleUtil.msg("Welcome! Sample books and users have been pre-loaded.");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = ConsoleUtil.promptInt("Your choice", 1, 5);
            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> userMenu();
                case 3 -> transactionMenu();
                case 4 -> reportsMenu();
                case 5 -> { running = false; }
            }
        }

        ConsoleUtil.header("GOODBYE");
        ConsoleUtil.msg("Thank you for using the Library Management System.");
        ConsoleUtil.blank();
    }

    // -------------------------------------------------------------------------
    // MAIN MENU
    // -------------------------------------------------------------------------
    static void showMainMenu() {
        ConsoleUtil.header("MAIN MENU");
        ConsoleUtil.msg("1. Book Management");
        ConsoleUtil.msg("2. User Management");
        ConsoleUtil.msg("3. Issue / Return Books");
        ConsoleUtil.msg("4. Reports");
        ConsoleUtil.msg("5. Exit");
        ConsoleUtil.line();
    }

    // -------------------------------------------------------------------------
    // BOOK MENU
    // -------------------------------------------------------------------------
    static void bookMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.header("BOOK MANAGEMENT");
            ConsoleUtil.msg("1. View All Books");
            ConsoleUtil.msg("2. Search by Title");
            ConsoleUtil.msg("3. Search by Author");
            ConsoleUtil.msg("4. Add a Book");
            ConsoleUtil.msg("5. Update a Book");
            ConsoleUtil.msg("6. Remove a Book");
            ConsoleUtil.msg("7. Back");
            ConsoleUtil.line();
            int choice = ConsoleUtil.promptInt("Your choice", 1, 7);
            switch (choice) {
                case 1 -> listBooks(bookService.getAllBooks());
                case 2 -> {
                    String kw = ConsoleUtil.prompt("Title keyword");
                    listBooks(bookService.searchByTitle(kw));
                }
                case 3 -> {
                    String kw = ConsoleUtil.prompt("Author keyword");
                    listBooks(bookService.searchByAuthor(kw));
                }
                case 4 -> addBook();
                case 5 -> updateBook();
                case 6 -> removeBook();
                case 7 -> back = true;
            }
        }
    }

    static void listBooks(List<Book> books) {
        ConsoleUtil.section("BOOK LIST");
        if (books.isEmpty()) {
            ConsoleUtil.msg("No books found.");
        } else {
            for (Book b : books) ConsoleUtil.msg(b.toString());
        }
        ConsoleUtil.pressEnter();
    }

    static void addBook() {
        ConsoleUtil.section("ADD BOOK");
        String title  = ConsoleUtil.prompt("Title");
        String author = ConsoleUtil.prompt("Author");
        String genre  = ConsoleUtil.prompt("Genre");
        int copies    = ConsoleUtil.promptInt("Number of copies", 1, 100);

        Book added = bookService.addBook(title, author, genre, copies);
        ConsoleUtil.success("Book added: " + added.toString());
        ConsoleUtil.pressEnter();
    }

    static void updateBook() {
        ConsoleUtil.section("UPDATE BOOK");
        String id = ConsoleUtil.prompt("Book ID");
        Book b = bookService.findById(id);
        if (b == null) {
            ConsoleUtil.error("Book not found: " + id);
            ConsoleUtil.pressEnter();
            return;
        }
        ConsoleUtil.msg("Current: " + b.toString());
        String title  = ConsoleUtil.promptOptional("New title");
        String author = ConsoleUtil.promptOptional("New author");
        String genre  = ConsoleUtil.promptOptional("New genre");
        String copStr = ConsoleUtil.promptOptional("New total copies");
        int copies = copStr.isBlank() ? -1 : Integer.parseInt(copStr);

        bookService.updateBook(id, title, author, genre, copies);
        ConsoleUtil.success("Updated: " + bookService.findById(id).toString());
        ConsoleUtil.pressEnter();
    }

    static void removeBook() {
        ConsoleUtil.section("REMOVE BOOK");
        String id = ConsoleUtil.prompt("Book ID to remove");
        if (bookService.removeBook(id)) {
            ConsoleUtil.success("Book " + id + " removed.");
        } else {
            ConsoleUtil.error("Cannot remove — book not found or copies are still issued.");
        }
        ConsoleUtil.pressEnter();
    }

    // -------------------------------------------------------------------------
    // USER MENU
    // -------------------------------------------------------------------------
    static void userMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.header("USER MANAGEMENT");
            ConsoleUtil.msg("1. View All Users");
            ConsoleUtil.msg("2. Search by Name");
            ConsoleUtil.msg("3. Register New User");
            ConsoleUtil.msg("4. Update User");
            ConsoleUtil.msg("5. Remove User");
            ConsoleUtil.msg("6. Back");
            ConsoleUtil.line();
            int choice = ConsoleUtil.promptInt("Your choice", 1, 6);
            switch (choice) {
                case 1 -> listUsers(userService.getAllUsers());
                case 2 -> {
                    String kw = ConsoleUtil.prompt("Name keyword");
                    listUsers(userService.searchByName(kw));
                }
                case 3 -> registerUser();
                case 4 -> updateUser();
                case 5 -> removeUser();
                case 6 -> back = true;
            }
        }
    }

    static void listUsers(List<User> users) {
        ConsoleUtil.section("USER LIST");
        if (users.isEmpty()) {
            ConsoleUtil.msg("No users found.");
        } else {
            for (User u : users) ConsoleUtil.msg(u.toString());
        }
        ConsoleUtil.pressEnter();
    }

    static void registerUser() {
        ConsoleUtil.section("REGISTER USER");
        String name  = ConsoleUtil.prompt("Full name");
        String email = ConsoleUtil.prompt("Email");
        String phone = ConsoleUtil.prompt("Phone");

        User u = userService.registerUser(name, email, phone);
        if (u == null) {
            ConsoleUtil.error("Email already registered.");
        } else {
            ConsoleUtil.success("Registered: " + u.toString());
        }
        ConsoleUtil.pressEnter();
    }

    static void updateUser() {
        ConsoleUtil.section("UPDATE USER");
        String id = ConsoleUtil.prompt("User ID");
        User u = userService.findById(id);
        if (u == null) {
            ConsoleUtil.error("User not found: " + id);
            ConsoleUtil.pressEnter();
            return;
        }
        ConsoleUtil.msg("Current: " + u.toString());
        String name  = ConsoleUtil.promptOptional("New name");
        String email = ConsoleUtil.promptOptional("New email");
        String phone = ConsoleUtil.promptOptional("New phone");
        userService.updateUser(id, name, email, phone);
        ConsoleUtil.success("Updated: " + userService.findById(id).toString());
        ConsoleUtil.pressEnter();
    }

    static void removeUser() {
        ConsoleUtil.section("REMOVE USER");
        String id = ConsoleUtil.prompt("User ID to remove");
        if (userService.removeUser(id)) {
            ConsoleUtil.success("User " + id + " removed.");
        } else {
            ConsoleUtil.error("Cannot remove — user not found or has books checked out.");
        }
        ConsoleUtil.pressEnter();
    }

    // -------------------------------------------------------------------------
    // TRANSACTION MENU
    // -------------------------------------------------------------------------
    static void transactionMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.header("ISSUE / RETURN BOOKS");
            ConsoleUtil.msg("1. Issue a Book");
            ConsoleUtil.msg("2. Return a Book");
            ConsoleUtil.msg("3. View Transactions for a User");
            ConsoleUtil.msg("4. Back");
            ConsoleUtil.line();
            int choice = ConsoleUtil.promptInt("Your choice", 1, 4);
            switch (choice) {
                case 1 -> issueBook();
                case 2 -> returnBook();
                case 3 -> userTransactions();
                case 4 -> back = true;
            }
        }
    }

    static void issueBook() {
        ConsoleUtil.section("ISSUE BOOK");
        String userId = ConsoleUtil.prompt("User ID");
        String bookId = ConsoleUtil.prompt("Book ID");

        String error = txService.issueBook(userId, bookId);
        if (error != null) {
            ConsoleUtil.error(error);
        } else {
            User u = userService.findById(userId);
            Book b = bookService.findById(bookId);
            ConsoleUtil.success("Issued \"" + b.getTitle() + "\" to " + u.getName());
            ConsoleUtil.msg("Due date: 14 days from today.");
        }
        ConsoleUtil.pressEnter();
    }

    static void returnBook() {
        ConsoleUtil.section("RETURN BOOK");
        String txId = ConsoleUtil.prompt("Transaction ID");

        Transaction tx = txService.findById(txId);
        if (tx == null || tx.getStatus() == Transaction.Status.RETURNED) {
            ConsoleUtil.error("Transaction not found or already returned.");
            ConsoleUtil.pressEnter();
            return;
        }

        double fine = txService.returnBook(txId);
        if (fine < 0) {
            ConsoleUtil.error("Return failed.");
        } else {
            Book b = bookService.findById(tx.getBookId());
            ConsoleUtil.success("Book returned: " + b.getTitle());
            if (fine > 0) {
                ConsoleUtil.warn("Late return! Fine due: Rs." + String.format("%.1f", fine)
                        + " (Rs.2 per day)");
            } else {
                ConsoleUtil.msg("Returned on time. No fine.");
            }
        }
        ConsoleUtil.pressEnter();
    }

    static void userTransactions() {
        ConsoleUtil.section("USER TRANSACTION HISTORY");
        String userId = ConsoleUtil.prompt("User ID");
        List<Transaction> list = txService.getAllByUser(userId);
        if (list.isEmpty()) {
            ConsoleUtil.msg("No transactions found for " + userId);
        } else {
            for (Transaction t : list) ConsoleUtil.msg(t.toString());
        }
        ConsoleUtil.pressEnter();
    }

    // -------------------------------------------------------------------------
    // REPORTS MENU
    // -------------------------------------------------------------------------
    static void reportsMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.header("REPORTS");
            ConsoleUtil.msg("1. All Books (with availability)");
            ConsoleUtil.msg("2. All Users");
            ConsoleUtil.msg("3. All Transactions");
            ConsoleUtil.msg("4. Overdue Books");
            ConsoleUtil.msg("5. Back");
            ConsoleUtil.line();
            int choice = ConsoleUtil.promptInt("Your choice", 1, 5);
            switch (choice) {
                case 1 -> listBooks(bookService.getAllBooks());
                case 2 -> listUsers(userService.getAllUsers());
                case 3 -> {
                    ConsoleUtil.section("ALL TRANSACTIONS");
                    List<Transaction> all = txService.getAllTransactions();
                    if (all.isEmpty()) ConsoleUtil.msg("No transactions yet.");
                    else all.forEach(t -> ConsoleUtil.msg(t.toString()));
                    ConsoleUtil.pressEnter();
                }
                case 4 -> {
                    ConsoleUtil.section("OVERDUE BOOKS");
                    List<Transaction> overdue = txService.getOverdueTransactions();
                    if (overdue.isEmpty()) {
                        ConsoleUtil.msg("No overdue books. Great!");
                    } else {
                        for (Transaction t : overdue) {
                            User u = userService.findById(t.getUserId());
                            Book b = bookService.findById(t.getBookId());
                            ConsoleUtil.warn(
                                String.format("User: %-20s | Book: %-30s | Fine so far: Rs.%.1f",
                                    u.getName(), b.getTitle(), t.calculateFine())
                            );
                        }
                    }
                    ConsoleUtil.pressEnter();
                }
                case 5 -> back = true;
            }
        }
    }
}
