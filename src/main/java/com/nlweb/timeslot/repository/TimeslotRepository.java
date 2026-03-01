package com.nlweb.timeslot.repository;

import com.nlweb.timeslot.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, UUID> {

    Optional<Timeslot> findById(UUID id);

    boolean existsByStartAtLessThanAndEndAtGreaterThan(LocalDateTime endAt, LocalDateTime startAt);

    long deleteByEnsembleId(UUID ensembleId);

}
