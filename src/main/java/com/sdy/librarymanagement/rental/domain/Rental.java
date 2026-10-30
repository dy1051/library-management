package com.sdy.librarymanagement.rental.domain;

import com.sdy.librarymanagement.book.domain.Book;
import com.sdy.librarymanagement.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rental {

    public Rental(Member member, Book book, LocalDate rentedOn) {
        this.member = member;
        this.book = book;
        this.rentedOn = rentedOn;
        this.dueDate = rentedOn.plusDays(7);
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "rented_on", nullable = false)
    private LocalDate rentedOn;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    public void extendDueDate() {
        this.dueDate = this.dueDate.plusDays(7);
    }
}
