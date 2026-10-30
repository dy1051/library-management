package com.sdy.librarymanagement.auth;

import com.sdy.librarymanagement.member.domain.Member;
import com.sdy.librarymanagement.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        return User.withUsername(member.getLoginId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}
