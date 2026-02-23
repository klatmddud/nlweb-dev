package com.nlweb.admin.service;

import com.nlweb.admin.repository.AdminRepository;
import com.nlweb.admin.entity.Admin;
import com.nlweb.admin.dto.object.*;
import com.nlweb.admin.dto.command.*;
import com.nlweb.admin.exception.*;
import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.entity.User;
import com.nlweb.user.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    /** 집부 임명 */
    public AdminObject create(CreateAdminCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new UserNotFoundException(command.getUsername()));

        if (adminRepository.findByUser(user).isPresent()) {
            throw new DuplicateAdminException(command.getUsername());
        }

        if (adminRepository.findByRole(command.getRole()).isPresent()) {
            throw new DuplicateAdminException(command.getRole());
        }

        Admin admin = Admin.builder()
                .user(user)
                .role(command.getRole())
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        log.info("집부 생성 완료: {}", savedAdmin.getUser().getUsername());

        return AdminObject.fromEntity(savedAdmin);
    }

    public AdminObject updateRole(String username, String role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Admin admin = adminRepository.findByUser(user)
                .orElseThrow(() -> new AdminNotFoundException("username: " + username));

        String normalizedRole = role.trim();

        Optional<Admin> existingRoleOwner = adminRepository.findByRole(normalizedRole);
        if (existingRoleOwner.isPresent() && !existingRoleOwner.get().getId().equals(admin.getId())) {
            throw new DuplicateAdminException(normalizedRole);
        }

        admin.setRole(normalizedRole);
        Admin savedAdmin = adminRepository.save(admin);
        log.info("집부 역할 수정 완료: {} -> {}", savedAdmin.getUser().getUsername(), savedAdmin.getRole());

        return AdminObject.fromEntity(savedAdmin);
    }

}
