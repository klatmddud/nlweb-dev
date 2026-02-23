package com.nlweb.program.repository;

import com.nlweb.program.entity.ProgramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramUserRepository extends JpaRepository<ProgramUser, UUID> {

}
