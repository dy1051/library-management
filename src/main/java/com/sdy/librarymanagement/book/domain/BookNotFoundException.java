package com.sdy.librarymanagement.book.domain;

public class BookNotFoundException extends RuntimeException{

    public  BookNotFoundException() {
        super("존재하지 않는 도서입니다.");
    }
}
