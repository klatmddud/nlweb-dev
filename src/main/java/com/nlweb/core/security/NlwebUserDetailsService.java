package com.nlweb.core.security;

import com.nlweb.user.entity.User;
import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NlwebUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public NlwebUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("NlwebUserDetailsService - loadUserByUsername: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new NlwebUserDetails(user);
    }

    public NlwebUserDetails loadUserByIdentifier(String identifier) throws UsernameNotFoundException {
        log.debug("NlwebUserDetailsService - loadUserByIdentifier: {}", identifier);

        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserNotFoundException(identifier));

        return new NlwebUserDetails(user);
    }

    public String getUsername() {
        return userRepository.findAll().getFirst().getUsername();
    }

}
