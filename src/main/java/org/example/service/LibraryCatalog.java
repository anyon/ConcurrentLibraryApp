package org.example.service;

import org.example.model.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LibraryCatalog {
    private final List<Book> catalog;
    private int nextId = 1;

    public LibraryCatalog() {
        this.catalog = new CopyOnWriteArrayList<>();
    }

    public void addBook(String title, String author, String serialNumber) {
        catalog.add(new Book(nextId++, title, author, serialNumber, false));
    }

    public List<Book> getCatalog() {
        return catalog;
    }

    public Book findAvailableBookById(int id) {
        return catalog.stream()
                .filter(book -> book.id() == id && !book.isBorrowed())
                .findFirst()
                .orElse(null);
    }

    public Book findAvailableBookBySerialNumber(String serialNumber) {
        return catalog.stream()
                .filter(book -> book.serialNumber() == serialNumber && !book.isBorrowed())
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooksByTitleAndAuthor(String title, String author) {
        return catalog.stream()
                .filter(book -> book.title().equalsIgnoreCase(title) && book.author().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public Book getFirstAvailableBooksByTitleAndAuthor(String title, String author) {
        return catalog.stream()
                .filter(book -> book.title().equalsIgnoreCase(title) && book.author().equalsIgnoreCase(author))
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAvailableBooksByTitleAndAuthor(String title, String author) {
        return catalog.stream()
                .filter(book -> book.title().equalsIgnoreCase(title) &&
                        book.author().equalsIgnoreCase(author) &&
                        !book.isBorrowed())
                .collect(Collectors.toList());
    }

    public List<Book> getAllAvailableBookByTitle(String title) {
        return catalog.stream()
                .filter(book -> book.title().equalsIgnoreCase(title) &&
                        !book.isBorrowed())
                .collect(Collectors.toList());
    }

    public List<Book> getAvailableBooks() {
        return catalog.stream()
                .filter(book -> !book.isBorrowed())
                .collect(Collectors.toList());
    }

    public int getAvailableBooksCount() {
        return (int) catalog.stream()
                .filter(book -> !book.isBorrowed())
                .count();
    }

    public int getAvailableBooksByTitleAndAuthorCount(String title, String author) {
        return (int) catalog.stream()
                .filter(book -> book.title().equals(title) &&
                        book.author().equals(author) &&
                        !book.isBorrowed())
                .count();
    }

    public void updateBook(Book oldBook, Book newBook) {
        catalog.remove(oldBook);
        catalog.add(newBook);
    }
}
