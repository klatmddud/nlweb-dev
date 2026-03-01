package com.nlweb.ensemble.repository;

import com.nlweb.ensemble.entity.Ensemble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
public interface EnsembleRepository extends JpaRepository<Ensemble, UUID> {

    Optional<Ensemble> findById(UUID id);

    Optional<Ensemble> findByArtistAndTitle(String artist, String title);

    List<Ensemble> findByProgramId(UUID programId);

    boolean existsByArtistAndTitle(String artist, String title);

    boolean existsByDayOfWeekAndStartTimeAndEndTime(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

}
