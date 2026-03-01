package com.nlweb.ensemble.dto.request;

import java.util.UUID;

public record CreateEnsembleRequest(
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
        String etc
) { }
