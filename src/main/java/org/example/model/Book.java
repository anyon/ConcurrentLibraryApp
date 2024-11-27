package org.example.model;

public record Book(int id, String title, String author, String serialNumber, boolean isBorrowed) {

        public Book borrow() {
                return new Book(id, title, author, serialNumber, true);
        }

        public Book returnBook() {
                return new Book(id, title, author, serialNumber, false);
        }
}


