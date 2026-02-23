package com.nlweb.program.repository;

import com.nlweb.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {

    /** id로 찾기 */
    Optional<Program> findById(UUID id);

    /** 제목으로 찾기 */
    Optional<Program> findByTitle(String title);

}
