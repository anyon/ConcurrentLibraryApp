package org.example.ui;

import org.example.model.User;
import org.example.service.LibraryCatalog;
import org.example.service.LibraryService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInterface {
    private final LibraryCatalog catalog;
    private final LibraryService libraryService;
    private final Map<String, User> users;
    private User activeUser;
    public ConsoleInterface() {
        this.catalog = new LibraryCatalog();
        this.libraryService = new LibraryService(catalog);
        this.users = new HashMap<>();
        this.activeUser = createUser("Default User");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        User user = new User("Default User");

        while (true) {
            System.out.println("""
                    \nChoose an option:
                    1. Add a book
                    2. Borrow a book
                    3. Return a book
                    4. View borrowed books
                    5. View all books
                    6. View available books
                    7. View books by title and author
                    8. View available books by title and author
                    9. Change user
                    10. Exit
                    """);

            int choice = -1;
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 10.");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            switch (choice) {
                case 1 -> addBook(scanner);
                case 2 -> borrowBook(scanner, user);
                case 3 -> returnBook(scanner, user);
                case 4 -> viewBorrowedBooks(user);
                case 5 -> viewAllBooks();
                case 6 -> viewAvailableBooks();
                case 7 -> viewBooksByTitleAndAuthor(scanner);
                case 8 -> viewAvailableBooksByTitleAndAuthor(scanner);
                case 9 -> changeUser(scanner);
                case 10 -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private User createUser(String name) {
        return users.computeIfAbsent(name, User::new);
    }

    private void changeUser(Scanner scanner) {
        System.out.print("Enter the name of the user to switch to: ");
        String userName = scanner.nextLine();

        if (users.containsKey(userName)) {
            activeUser = users.get(userName);
            System.out.println("Switched to user: " + activeUser.getName());
        } else {
            activeUser = createUser(userName);
            System.out.println("New user created and switched to: " + activeUser.getName());
        }
    }

    private void addBook(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter serial number (e.g., S123 or A-456): ");
        String serialNumber = scanner.nextLine();
        catalog.addBook(title, author, serialNumber);
        System.out.println("Book added successfully!");
    }

    private void borrowBook(Scanner scanner, User user) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        String result = libraryService.borrowFirstAvailableBookByTitleAndAuthor(user, title, author);
        System.out.println(result);
    }

    private void returnBook(Scanner scanner, User user) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        String result = libraryService.returnBook(user, title, author);
        System.out.println(result);
    }

    private void returnBookBySerialNumber(Scanner scanner, User user) {
        System.out.print("Enter book Serial Number: ");
        String serialNumber = scanner.nextLine();
        String result = libraryService.returnBook(user, serialNumber);
        System.out.println(result);
    }

    private void viewBorrowedBooks(User user) {
        System.out.println("Borrowed books:");
        if (user.getBorrowedBooks().isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            user.getBorrowedBooks().forEach(book ->
                    System.out.println("- [" + book.id() + "] " + book.title() + " by " + book.author() + " (Serial: " + book.serialNumber() + ")")
            );
        }
    }

    private void viewAllBooks() {
        System.out.println("All books in the library:");
        if (catalog.getCatalog().isEmpty()) {
            System.out.println("No books in the catalog.");
        } else {
            catalog.getCatalog().forEach(book -> {
                String status = book.isBorrowed() ? "Borrowed" : "Available";
                System.out.println("- [" + book.id() + "] " + book.title() + " by " + book.author() + " (Serial: " + book.serialNumber() + ") (" + status + ")");
            });
        }
    }

    private void viewAvailableBooks() {
        System.out.println("Available books in the library:");
        catalog.getAvailableBooks().forEach(book ->
                System.out.println("- [" + book.id() + "] " + book.title() + " by " + book.author() + " (Serial: " + book.serialNumber() + ")")
        );
    }

    private void viewBooksByTitleAndAuthor(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        var books = catalog.getAllBooksByTitleAndAuthor(title, author);
        if (books.isEmpty()) {
            System.out.println("No books found for the given title and author.");
        } else {
            System.out.println("Books found:");
            books.forEach(book -> {
                String status = book.isBorrowed() ? "Borrowed" : "Available";
                System.out.println("- [" + book.id() + "] " + book.title() + " by " + book.author() + " (Serial: " + book.serialNumber() + ") (" + status + ")");
            });
        }
    }

    private void viewAvailableBooksByTitleAndAuthor(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        var books = catalog.getAvailableBooksByTitleAndAuthor(title, author);
        if (books.isEmpty()) {
            System.out.println("No available books found for the given title and author.");
        } else {
            System.out.println("Available books found:");
            books.forEach(book ->
                    System.out.println("- [" + book.id() + "] " + book.title() + " by " + book.author() + " (Serial: " + book.serialNumber() + ")")
            );
        }
    }
}
