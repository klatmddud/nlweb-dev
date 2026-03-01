package com.nlweb.ensemble.dto.response;

import com.nlweb.ensemble.entity.Ensemble;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnsembleResponse(
        UUID id,
        UUID program_id,

        String artist,
        String title,

        String vocal,
        String leadGuitar,
        String rhythmGuitar,
        String bass,
        String drum,
        String piano,
        String synth,
        String etc,

        String DayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {

    public static EnsembleResponse forPublic(Ensemble ensemble) {
        return new EnsembleResponse(
                ensemble.getId(),
                ensemble.getProgram().getId(),
                ensemble.getArtist(),
                ensemble.getTitle(),
                ensemble.getVocal(),
                ensemble.getLeadGuitar(),
                ensemble.getRhythmGuitar(),
                ensemble.getBass(),
                ensemble.getDrum(),
                ensemble.getPiano(),
                ensemble.getSynth(),
                ensemble.getEtc(),
                ensemble.getDayOfWeek() != null? ensemble.getDayOfWeek().toString() : null,
                ensemble.getStartTime(),
                ensemble.getEndTime()
        );
    }

}
