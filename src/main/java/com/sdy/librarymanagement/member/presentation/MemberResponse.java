package com.sdy.librarymanagement.member.presentation;

import com.sdy.librarymanagement.member.domain.Member;
import com.sdy.librarymanagement.member.domain.Role;

public record MemberResponse(
        Long id,
        String loginId,
        String name,
        Role role,
        boolean rentalAllowed,
        int rentalCount
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getRole(),
                member.isRentalAllowed(),
                member.getRentalCount()
        );
    }
}
