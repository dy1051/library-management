package com.sdy.librarymanagement.member.presentation;

import com.sdy.librarymanagement.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> signup(@Valid @RequestBody SignupRequest request) {
        Long memberId = memberService.signup(request.loginId(), request.password(), request.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }
}
