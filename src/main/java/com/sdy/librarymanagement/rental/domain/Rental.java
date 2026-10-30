package com.sdy.librarymanagement.rental.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rental {

    public Rental(Long memberId, Long bookId, LocalDate rentedOn) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.rentedOn = rentedOn;
        this.dueDate = rentedOn.plusDays(7);
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "rented_on", nullable = false)
    private LocalDate rentedOn;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    public void extendDueDate() {
        this.dueDate = this.dueDate.plusDays(7);
    }
}
