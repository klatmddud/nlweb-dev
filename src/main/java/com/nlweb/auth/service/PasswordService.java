package com.nlweb.auth.service;

import com.nlweb.auth.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    /** 비밀번호 검증 */
    public void validateUserPassword(String userPassword, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new InvalidCredentialsException("아이디 및 비밀번호가 일치하지 않습니다.");
        }
    }

}