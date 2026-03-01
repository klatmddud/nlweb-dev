package com.nlweb.ensemble.dto.request;

import java.util.UUID;

public record UpdateEnsembleRequest (
        UUID id,
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
