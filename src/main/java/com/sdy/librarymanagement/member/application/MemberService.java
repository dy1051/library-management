package com.sdy.librarymanagement.member.application;

import com.sdy.librarymanagement.member.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(String loginId, String password, String name) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException();
        }

        String encodePassword = passwordEncoder.encode(password);
        Member member = new Member(loginId, encodePassword, name);

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public Member findMe(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void changePassword(String loginId, String currentPassword, String newPassword) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new);

        boolean matches = passwordEncoder.matches(currentPassword, member.getPassword());

        if(!matches)
            throw new PasswordMismatchException();

        member.changePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void withdraw(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }
}
