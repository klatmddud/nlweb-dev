package com.nlweb.admin.service;

import com.nlweb.admin.exception.AdminNotFoundException;
import com.nlweb.admin.repository.AdminRepository;
import com.nlweb.admin.dto.object.AdminObject;
import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.entity.User;
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
public class AdminQueryService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    /** Admin Id로 찾기 */
    public AdminObject getById(UUID id) {
        return AdminObject.fromEntity(
                adminRepository.findById(id)
                        .orElseThrow(() -> new AdminNotFoundException("id: " + id))
        );
    }

    /** username으로 찾기 */
    public AdminObject getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException("username: " + username));

        return AdminObject.fromEntity(
                adminRepository.findByUser(user)
                .orElseThrow(() -> new AdminNotFoundException("username: " + username))
        );
    }

    /** 전체 관리자 조회 */
    public List<AdminObject> getAll() {
        return adminRepository.findAll().stream()
                .map(AdminObject::fromEntity)
                .toList();
    }

}
