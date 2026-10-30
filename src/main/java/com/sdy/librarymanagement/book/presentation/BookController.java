package com.sdy.librarymanagement.book.presentation;

import com.sdy.librarymanagement.book.application.BookService;
import com.sdy.librarymanagement.book.domain.RentalStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> register(@Valid @RequestBody BookRegisterRequest request) {
        Long registerId = bookService.register(request.title(), request.author(), request.category());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerId);
    }

    @GetMapping
    public List<BookResponse> list() {
        return bookService.list().stream()
                .map(BookResponse::from)
                .toList();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable("bookId") Long bookId, @Valid @RequestBody BookUpdateRequest request) {
        bookService.update(bookId, request.title(), request.author(), request.category());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable("bookId") Long bookId) {
        bookService.delete(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/title")
    public List<BookResponse> findByTitle(@RequestParam("title") String title) {
        return bookService.findByTitle(title).stream()
                .map(BookResponse::from)
                .toList();
    }

    @GetMapping("/category")
    public List<BookResponse> findByCategory(@RequestParam("category") String category) {
        return bookService.findByCategory(category).stream()
                .map(BookResponse::from)
                .toList();
    }

    @GetMapping("/status")
    public List<BookResponse> findByRentalStatus(@RequestParam("status") RentalStatus rentalStatus) {
        return bookService.findByRentalStatus(rentalStatus).stream()
                .map(BookResponse::from)
                .toList();
    }
}
