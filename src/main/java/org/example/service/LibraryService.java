package org.example.service;

import org.example.model.Book;
import org.example.model.User;

public class LibraryService {
    private final LibraryCatalog catalog;

    public LibraryService(LibraryCatalog catalog) {
        this.catalog = catalog;
    }

    public synchronized String borrowBookById(User user, int id) {
        Book book = catalog.findAvailableBookById(id);
        if (book != null) {
            Book borrowedBook = book.borrow();
            catalog.updateBook(book, borrowedBook);
            user.borrowBook(borrowedBook);
            return user.getName() + " borrowed the book: " + borrowedBook.title();
        }
        return "Book not available for borrowing.";
    }

    public synchronized String borrowBookBySerialNumber(User user, String serialNumber) {
        Book book = catalog.findAvailableBookBySerialNumber(serialNumber);
        if (book != null) {
            Book borrowedBook = book.borrow();
            catalog.updateBook(book, borrowedBook);
            user.borrowBook(borrowedBook);
            return user.getName() + " borrowed the book: " + borrowedBook.title();
        }
        return "Book not available for borrowing.";
    }

    public synchronized String borrowFirstAvailableBookByTitleAndAuthor(User user, String title, String author) {
        Book book = catalog.getFirstAvailableBooksByTitleAndAuthor(title, author);

        if (book != null) {
            Book borrowedBook = book.borrow();
            catalog.updateBook(book, borrowedBook);
            user.borrowBook(borrowedBook);
            return user.getName() + " borrowed the book: " + borrowedBook.title();
        }
        return "No available books with the title: " + title;
    }

    public synchronized String returnBook(User user, String title, String author) {
        Book bookToReturn = user.getBorrowedBooks().stream()
                .filter(book -> book.title().equals(title))
                .filter(book -> book.author().equals(author))
                .findFirst()
                .orElse(null);

        return returnBook(user, bookToReturn);
    }

    public synchronized String returnBook(User user, String serialNumber) {
        Book bookToReturn = user.getBorrowedBooks().stream()
                .filter(book -> book.serialNumber().equals(serialNumber))
                .findFirst()
                .orElse(null);

        return returnBook(user, bookToReturn);
    }

    private String returnBook(User user, Book bookToReturn) {
        if (bookToReturn != null) {
            Book returnedBook = bookToReturn.returnBook();
            catalog.updateBook(bookToReturn, returnedBook);
            user.returnBook(bookToReturn);
            return user.getName() + " returned the book: " + returnedBook.title();
        }
        return "Book not found in user's borrowed list.";
    }
}
