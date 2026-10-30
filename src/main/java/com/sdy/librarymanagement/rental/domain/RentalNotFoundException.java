package com.sdy.librarymanagement.rental.domain;

public class RentalNotFoundException extends RuntimeException{

    public RentalNotFoundException() {
        super("존재하지 않는 대출입니다.");
    }
}
