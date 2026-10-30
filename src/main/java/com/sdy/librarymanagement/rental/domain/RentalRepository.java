package com.sdy.librarymanagement.rental.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByMemberId(Long memberId);
}
