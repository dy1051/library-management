package com.sdy.librarymanagement.member.application;

import com.sdy.librarymanagement.member.domain.Member;
import com.sdy.librarymanagement.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(String loginId, String password, String name) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new IllegalStateException("중복 ID입니다!");
        }

        String encodePassword = passwordEncoder.encode(password);
        Member member = new Member(loginId, encodePassword, name);

        return memberRepository.save(member).getId();
    }
}
