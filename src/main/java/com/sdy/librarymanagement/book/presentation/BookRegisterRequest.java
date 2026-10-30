package com.sdy.librarymanagement.book.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRegisterRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 100) String author,
        @NotBlank @Size(max = 10) String category
) {}
