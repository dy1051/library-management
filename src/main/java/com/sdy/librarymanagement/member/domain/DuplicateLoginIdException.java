package com.sdy.librarymanagement.member.domain;

public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException() {
        super("이미 사용 중인 아이디입니다.");
    }
}