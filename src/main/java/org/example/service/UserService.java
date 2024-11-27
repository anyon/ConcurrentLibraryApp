package org.example.service;

import org.example.model.User;

public class UserService {
    public void printBorrowedBooks(User user) {
        System.out.println(user.getName() + "'s borrowed books:");
        if (user.getBorrowedBooks().isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            user.getBorrowedBooks().forEach(book ->
                    System.out.println("- " + book.title() + " by " + book.author())
            );
        }
    }
}
