package com.sdy.librarymanagement.book.presentation;

import com.sdy.librarymanagement.book.domain.Book;
import com.sdy.librarymanagement.book.domain.RentalStatus;

public record BookResponse(
        Long id,
        String title,
        String author,
        String category,
        RentalStatus rentalStatus
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getRentalStatus()
        );
    }
}
