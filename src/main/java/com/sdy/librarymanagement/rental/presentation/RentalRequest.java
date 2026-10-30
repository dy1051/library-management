package com.sdy.librarymanagement.rental.presentation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RentalRequest(
        @NotNull @Positive Long bookId
) {}
