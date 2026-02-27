package com.nlweb.admin.service;

import com.nlweb.admin.exception.AdminNotFoundException;
import com.nlweb.admin.repository.AdminRepository;
import com.nlweb.admin.entity.Admin;
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
    public Admin getById(UUID id) {
        return  adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException("id: " + id));
    }

    /** username으로 찾기 */
    public Admin getByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AdminNotFoundException("username: " + username));
        return adminRepository.findByUser(user).orElseThrow(() -> new AdminNotFoundException("username: " + username));
    }

    /** 전체 관리자 조회 */
    public List<Admin> getAll() {
        return adminRepository.findAll().stream().toList();
    }

}
