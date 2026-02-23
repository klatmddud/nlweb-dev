package com.nlweb.program.repository;

import com.nlweb.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {

    boolean existsByTitle(String title);

}
