package com.sdy.librarymanagement.book.domain;

public class BookAlreadyRentedException extends RuntimeException {

    public BookAlreadyRentedException() {
        super("이미 대여 중인 도서입니다.");
    }
}