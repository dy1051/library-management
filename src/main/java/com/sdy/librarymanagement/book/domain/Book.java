package com.sdy.librarymanagement.book.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 10)
    private String category;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "rental_status", nullable = false, length = 10)
    private RentalStatus rentalStatus = RentalStatus.AVAILABLE;

    public void rent() {
        if (rentalStatus == RentalStatus.AVAILABLE) {
            rentalStatus = RentalStatus.RENTED;
        } else {
            throw new IllegalStateException("대여중입니다!");
        }
    }

    public void returnBook() {
        if (rentalStatus == RentalStatus.RENTED) {
            rentalStatus = RentalStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("대여 중인 도서가 아닙니다.");
        }
    }

    public void updateInfo(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
    }
}
