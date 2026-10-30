package com.sdy.librarymanagement.book.application;

import com.sdy.librarymanagement.book.domain.Book;
import com.sdy.librarymanagement.book.domain.BookNotFoundException;
import com.sdy.librarymanagement.book.domain.BookRepository;
import com.sdy.librarymanagement.book.domain.RentalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Long register(String title, String author, String category) {
        Book book = new Book(title, author, category);

        return bookRepository.save(book).getId();
    }

    @Transactional(readOnly = true)
    public List<Book> list() {
        return bookRepository.findAll();
    }

    @Transactional
    public void update(Long bookId, String title, String author, String category) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        book.updateInfo(title, author, category);
    }

    @Transactional
    public void delete(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Book> findByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Book> findByRentalStatus(RentalStatus rentalStatus) {
        return bookRepository.findByRentalStatus(rentalStatus);
    }
}
