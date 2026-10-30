package com.sdy.librarymanagement.member.presentation;

import com.sdy.librarymanagement.member.application.MemberService;
import com.sdy.librarymanagement.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/me")
    public MemberResponse me(Authentication authentication) {
        Member member = memberService.findMe(authentication.getName());
        return MemberResponse.from(member);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        memberService.changePassword(
                authentication.getName(),
                request.currentPassword(),
                request.newPassword()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(Authentication authentication) {
        memberService.withdraw(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}")
    public MemberResponse findById(@PathVariable("memberId") Long memberId) {
        return MemberResponse.from(memberService.findById(memberId));
    }
}
