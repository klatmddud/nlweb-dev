package com.nlweb.amho.repository;

import com.nlweb.amho.entity.Amho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface AmhoRepository extends JpaRepository<Amho, UUID> {

    /** 현재 코드 찾기 */
    Optional<Amho> findByIsActiveTrue();

    /** 코드로 찾기 */
    Optional<Amho> findByUserCode(String userCode);
    Optional<Amho> findByAdminCode(String adminCode);

}
