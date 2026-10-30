package com.sdy.librarymanagement.rental.presentation;

import com.sdy.librarymanagement.rental.domain.Rental;

import java.time.LocalDate;

public record RentalResponse(
        Long id,
        Long bookId,
        LocalDate rentedOn,
        LocalDate dueDate
) {
    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getBookId(),
                rental.getRentedOn(),
                rental.getDueDate()
        );
    }
}