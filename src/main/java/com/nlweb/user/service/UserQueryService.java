package com.nlweb.user.service;

import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.entity.User;
import com.nlweb.user.enums.UserSessionType;
import com.nlweb.user.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    /** Id로 사용자 조회 */
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id: " + id));
    }

    /** username으로 사용자 조회 */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username: " + username));
    }

    /** 이메일로 사용자 조회 */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email: " + email));
    }

    /** identifier로 찾기 - 로그인 */
    public User getByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserNotFoundException("identifier: " + identifier));
    }

    /** 세션과 기수로 사용자 목록 조회 */
    public List<User> getBySessionAndBatch(UserSessionType session, Integer batch) {
        if (session != null && batch != null) {
            return userRepository.findBySessionAndBatch(session, batch);
        } else if (session != null) {
            return userRepository.findBySession(session);
        } else if (batch != null) {
            return userRepository.findByBatch(batch);
        } else {
            return userRepository.findAll();
        }
    }

}
