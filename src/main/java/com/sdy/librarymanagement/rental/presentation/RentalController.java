package com.sdy.librarymanagement.rental.presentation;

import com.sdy.librarymanagement.rental.application.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<Long> rent(
            Authentication authentication,
            @Valid @RequestBody RentalRequest request
    ) {
        Long rentalId = rentalService.rent(authentication.getName(), request.bookId());

        return ResponseEntity.status(HttpStatus.CREATED).body(rentalId);
    }

    @GetMapping("/me")
    public List<RentalResponse> findMyRentals(Authentication authentication) {
        return rentalService.findMyRentals(authentication.getName()).stream()
                .map(RentalResponse::from)
                .toList();
    }

    @DeleteMapping("/{rentalId}")
    public ResponseEntity<Void> returnBook(Authentication authentication, @PathVariable("rentalId") Long rentalId) {
        rentalService.returnBook(authentication.getName(), rentalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{rentalId}/extend")
    public ResponseEntity<Void> extend(
            Authentication authentication,
            @PathVariable("rentalId") Long rentalId
    ) {
        rentalService.extend(authentication.getName(), rentalId);
        return ResponseEntity.noContent().build();
    }
}
