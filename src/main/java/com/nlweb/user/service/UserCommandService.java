package com.nlweb.user.service;

import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.exception.*;
import com.nlweb.user.entity.User;
import com.nlweb.user.dto.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 사용자 생성 */
    public User create(CreateUserRequest request) {

        checkUsernameExists(request.username());
        checkEmailExists(request.email());

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .email(request.email())
                .batch(request.batch())
                .session(request.session())
                .isVocalAllowed(request.session() != null && request.session().isVocal())
                .isAdmin(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("사용자 생성: {}", savedUser.getUsername());

        return savedUser;
    }

    /** 사용자 정보 수정 */
    public User update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id: " + id));

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }

        if (request.email() != null) {
            checkEmailExists(request.email());
            user.setEmail(request.email());
        }

        if (request.batch() != null) {
            user.setBatch(request.batch());
        }

        if (request.session() != null) {
            user.setSession(request.session());
            if (request.session().isVocal()) {
                user.setIsVocalAllowed(true);
            }
        }

        User savedUser = userRepository.save(user);
        log.info("사용자 정보 수정: {}", savedUser.getUsername());

        return savedUser;
    }

    /** 비밀번호 변경 */
    public User changePassword(UUID id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id: " + id));

        user.changePassword(passwordEncoder.encode(newPassword));
        User savedUser = userRepository.save(user);

        log.info("사용자 비밀번호 변경: {}", savedUser.getUsername());
        return savedUser;
    }

    /** 보컬 허용 상태 변경 */
    public User setVocalPermission(UUID id, Boolean permission) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id: " + id));

        if (user.getSession().isVocal()) {
            throw new InvalidUserOperationException("보컬 세션의 경우 보컬 세션 참여 가능 여부를 변경할 수 없습니다.");
        }

        user.setIsVocalAllowed(permission);
        User savedUser = userRepository.save(user);
        log.info("사용자 보컬 세션 허용 설정 변경: {} to {}", user.getUsername(), user.getIsVocalAllowed());

        return savedUser;
    }

    /** 사용자 소프트 삭제 */
    public User delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id: " + id));
        userRepository.delete(user);
        log.info("사용자 삭제: {}", user.getUsername());
        return user;
    }

    // ================== 중복 검사 헬퍼 ==================

    private void checkUsernameExists(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 username입니다.");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 이메일 계정입니다.");
        }
    }

}
