package com.sdy.librarymanagement.member.domain;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }
}
