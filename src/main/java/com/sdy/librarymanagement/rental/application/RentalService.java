package com.sdy.librarymanagement.rental.application;

import com.sdy.librarymanagement.book.domain.Book;
import com.sdy.librarymanagement.book.domain.BookNotFoundException;
import com.sdy.librarymanagement.book.domain.BookRepository;
import com.sdy.librarymanagement.member.domain.Member;
import com.sdy.librarymanagement.member.domain.MemberNotFoundException;
import com.sdy.librarymanagement.member.domain.MemberRepository;
import com.sdy.librarymanagement.rental.domain.Rental;
import com.sdy.librarymanagement.rental.domain.RentalNotFoundException;
import com.sdy.librarymanagement.rental.domain.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public Long rent(String loginId, Long bookId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new);
        member.checkRentalAllowed();

        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        book.rent();

        Rental rental = new Rental(member.getId(), book.getId(), LocalDate.now());
        Rental savedRental = rentalRepository.save(rental);
        member.increaseRentalCount();

        return savedRental.getId();
    }

    @Transactional(readOnly = true)
    public List<Rental> findMyRentals(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFoundException::new);

        return rentalRepository.findByMemberId(member.getId());
    }

    @Transactional
    public void returnBook(String loginId, Long rentalId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new);
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);

        if (!rental.getMemberId().equals(member.getId())) {
            throw new AccessDeniedException("본인의 대여만 반납할 수 있습니다.");
        }

        Book book = bookRepository.findById(rental.getBookId()).orElseThrow(BookNotFoundException::new);
        book.returnBook();
        member.decreaseRentalCount();
        rentalRepository.delete(rental);
    }

    @Transactional
    public void extend(String loginId, Long rentalId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new);
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);

        if (!rental.getMemberId().equals(member.getId())) {
            throw new AccessDeniedException("본인의 대여만 연장할 수 있습니다.");
        }

        rental.extendDueDate();
    }
}
