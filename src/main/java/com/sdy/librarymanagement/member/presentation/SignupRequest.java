package com.sdy.librarymanagement.member.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank
    @Size(max = 10)
    String loginId,

    @NotBlank
    @Size(min = 8)
    String password,

    @NotBlank
    @Size(max = 10)
    String name
) {}
