package com.nlweb.program.repository;

import com.nlweb.program.entity.ProgramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramUserRepository extends JpaRepository<ProgramUser, UUID> {

    /** 프로그램에 참여한 모든 사용자 검색 */
    List<ProgramUser> findByProgramId(UUID programId);

    /** 특정 사용자가 참여한 모든 프로그램 검색 */
    List<ProgramUser> findByUserId(UUID userId);

    /** 특정 프로그램에 참여한 특정 사용자 검색 */
    Optional<ProgramUser> findByUserIdAndProgramId(UUID userId, UUID programId);

}
