package org.example.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Library {
    private final List<Book> catalog;

    public Library() {
        this.catalog = new CopyOnWriteArrayList<Book>();
    }

    public void addBook(Book book) {
        catalog.add(book);
        System.out.println("Book added: " + book.title());
    }

    public Book findBook(String title) {
        for (Book book : catalog) {
            if (book.title().equalsIgnoreCase(title) && !book.isBorrowed()) {
                return book;
            }
        }
        return null;
    }

    public List<Book> getCatalog() {
        return catalog;
    }
}