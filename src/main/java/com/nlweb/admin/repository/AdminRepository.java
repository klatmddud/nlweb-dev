package com.nlweb.admin.repository;

import com.nlweb.admin.entity.Admin;
import com.nlweb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    /** ID로 찾기 */
    Optional<Admin> findById(UUID id);

    /** 사용자로 찾기 */
    Optional<Admin> findByUser(User user);

    /** 집부 역할명으로 찾기 */
    Optional<Admin> findByRole(String role);

}
