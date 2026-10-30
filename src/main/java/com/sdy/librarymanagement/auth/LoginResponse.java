package com.sdy.librarymanagement.auth;

public record LoginResponse(
        String accessToken,
        String tokenType
) {}
