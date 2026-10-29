package com.sdy.librarymanagement.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    public Member(String loginId, String encodedPassword, String name) {
        this.loginId = loginId;
        this.password = encodedPassword;
        this.name = name;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 10)
    private String loginId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = Role.USER;

    @Column(name = "rental_allowed", nullable = false)
    private boolean rentalAllowed = true;

    @Column(name = "rental_count", nullable = false)
    private int rentalCount = 0;

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void checkRentalAllowed() {
        if (!rentalAllowed)
            throw new IllegalStateException("대여 불가 회원!!!");
    }

    public void increaseRentalCount() {
        rentalCount++;
    }

    public void decreaseRentalCount() {
        if (rentalCount <= 0) {
            throw new IllegalStateException("감소시킬 대출 권수가 없습니다.");
        }
        rentalCount--;
    }
}
