# Library Management System

A fully console-based Library Management System built in **Core Java** using Object-Oriented Programming principles. Handles books, users, issue/return transactions, fine calculation, and reporting — with no external dependencies.

---

## Screenshots

### Main Menu
```
==============================================================
                  LIBRARY MANAGEMENT SYSTEM
==============================================================
  Welcome! Sample books and users have been pre-loaded.

==============================================================
                          MAIN MENU
==============================================================
  1. Book Management
  2. User Management
  3. Issue / Return Books
  4. Reports
  5. Exit
--------------------------------------------------------------
  Your choice [1-5]:
```

### Book Listing
```
  -- BOOK LIST --
--------------------------------------------------------------
  [B001] "The Pragmatic Programmer" by David Thomas | Genre: Technology | Available: 2/3
  [B002] "Clean Code" by Robert C. Martin | Genre: Technology | Available: 2/2
  [B003] "To Kill a Mockingbird" by Harper Lee | Genre: Fiction | Available: 4/4
  [B004] "1984" by George Orwell | Genre: Fiction | Available: 5/5
  [B005] "Sapiens" by Yuval Noah Harari | Genre: History | Available: 3/3
  [B006] "The Alchemist" by Paulo Coelho | Genre: Fiction | Available: 4/4
```

### Issuing a Book
```
  -- ISSUE BOOK --
--------------------------------------------------------------
  User ID: U001
  Book ID: B001
  [OK] Issued "The Pragmatic Programmer" to Arjun Mehta
  Due date: 14 days from today.
```

### Overdue Report
```
  -- OVERDUE BOOKS --
--------------------------------------------------------------
  [!] User: Arjun Mehta          | Book: The Pragmatic Programmer   | Fine so far: Rs.6.0
```

---

## Project Structure

```
LibraryMS/
│
├── src/
│   └── library/
│       ├── LibraryApp.java          ← Entry point + all menus
│       ├── model/
│       │   ├── Book.java            ← Book entity
│       │   ├── User.java            ← User entity
│       │   └── Transaction.java     ← Issue/return record + fine logic
│       ├── service/
│       │   ├── BookService.java     ← CRUD + search for books
│       │   ├── UserService.java     ← CRUD + registration for users
│       │   └── TransactionService.java  ← Issue, return, fine, overdue
│       └── util/
│           └── ConsoleUtil.java     ← Formatted console I/O helpers
│
├── out/                             ← Compiled .class files (after build)
└── README.md
```

---

## How to Run

### Prerequisites
- Java 11 or higher installed  
- No external libraries required

### Compile
```bash
# From the LibraryMS/ directory
find src -name "*.java" | xargs javac -d out
```

### Run
```bash
java -cp out library.LibraryApp
```

### Or use a JAR
```bash
jar cfe LibraryMS.jar library.LibraryApp -C out .
java -jar LibraryMS.jar
```

---

## Features

| Feature | Description |
|---|---|
| Add / Update / Remove Books | Manage the book catalog with copy count tracking |
| Register / Update / Remove Users | User accounts with a 3-book borrowing limit |
| Issue Books | Validates availability and per-user limits before issuing |
| Return Books | Marks return date and auto-calculates fine |
| Fine Calculation | Rs.2 per day after the 14-day loan period |
| Search | Search books by title keyword or author keyword |
| Reports | All books, all users, all transactions, overdue list |
| Duplicate Check | Prevents same user from borrowing same book twice |

---

## Tools & Technologies Used

- **Language:** Java 21 (Core Java only, no frameworks)
- **Data Storage:** In-memory (`HashMap`, `LinkedHashMap`)
- **Date Logic:** `java.time.LocalDate`, `ChronoUnit.DAYS`
- **I/O:** `java.util.Scanner` with a custom `ConsoleUtil` wrapper

---

## Java OOP Concepts Used

### 1. Classes and Objects
Each real-world entity has its own class:
- `Book` — stores book data and tracks copy availability
- `User` — holds member info and borrow count
- `Transaction` — represents a single borrow/return event

### 2. Encapsulation
All fields in model classes are `private`. External code always goes through getters/setters. For example, `book.issueOne()` and `book.returnOne()` control the `availableCopies` field internally — nothing outside `Book` can modify it directly.

### 3. Abstraction
The service layer (`BookService`, `UserService`, `TransactionService`) hides the internal data structures. The menu code calls `txService.issueBook(userId, bookId)` and gets back either `null` (success) or an error string — without knowing anything about how transactions are stored.

### 4. Single Responsibility Principle
Each class does one thing:
- Models hold data + basic behavior
- Services hold business logic
- `ConsoleUtil` handles all display formatting
- `LibraryApp` only handles navigation and calling services

### 5. Enums
`Transaction.Status` is an `enum` with values `ISSUED` and `RETURNED`. This is safer than using String constants like `"issued"` because the compiler catches typos.

### 6. Method Overriding
All three model classes override `toString()` to produce formatted, human-readable output that the display layer can just call directly.

---

## Business Rules

- A user can hold at most **3 books** at a time
- Loan period is **14 days**
- Fine is **Rs.2 per day** after the due date
- A user cannot borrow the same book title twice simultaneously
- A book cannot be deleted if copies are currently issued
- A user cannot be deleted if they have books checked out
- Registering with a duplicate email is rejected

---

## Sample Data (Pre-loaded)

**Books:**
| ID | Title | Author | Genre | Copies |
|---|---|---|---|---|
| B001 | The Pragmatic Programmer | David Thomas | Technology | 3 |
| B002 | Clean Code | Robert C. Martin | Technology | 2 |
| B003 | To Kill a Mockingbird | Harper Lee | Fiction | 4 |
| B004 | 1984 | George Orwell | Fiction | 5 |
| B005 | Sapiens | Yuval Noah Harari | History | 3 |
| B006 | The Alchemist | Paulo Coelho | Fiction | 4 |

**Users:**
| ID | Name | Email |
|---|---|---|
| U001 | Arjun Mehta | arjun@email.com |
| U002 | Priya Sharma | priya@email.com |
| U003 | Rahul Das | rahul@email.com |

---

## Interview Prep — Likely Questions

**Q: Why did you use `LinkedHashMap` instead of `HashMap`?**  
A: `LinkedHashMap` preserves insertion order, so when we list all books or users they appear in the order they were added — which feels natural for a catalog. Regular `HashMap` would give random ordering.

**Q: How is the fine calculated?**  
A: `Transaction.calculateFine()` uses `ChronoUnit.DAYS.between(dueDate, returnDate)`. If the result is positive, the book was returned late and we multiply by `FINE_PER_DAY`. If it's zero or negative, no fine.

**Q: What prevents issuing more copies than exist?**  
A: `BookService.issueBook()` calls `book.isAvailable()` which checks `availableCopies > 0`. The `availableCopies` field is only modified through `issueOne()` and `returnOne()` — both are guarded methods inside `Book`.

**Q: How would you add a database?**  
A: Replace the `Map<String, Book>` in each service with JDBC calls to a MySQL database. The menu code in `LibraryApp` would not change at all because it only talks to the service layer — that's the benefit of separating concerns.

**Q: Why return `null` from `issueBook()` on success instead of a boolean?**  
A: A boolean only tells you whether it succeeded. Returning `null` on success and an error `String` on failure lets the caller display the exact reason for failure (book not available vs. user limit reached vs. duplicate) without needing a separate lookup.

---

## Possible Extensions

- [ ] JDBC + MySQL for persistent storage
- [ ] JavaFX or Swing GUI
- [ ] Export transaction history to CSV
- [ ] Email notifications for due dates
- [ ] Role-based access (librarian vs. student)
- [ ] Reservation queue when a book is unavailable
