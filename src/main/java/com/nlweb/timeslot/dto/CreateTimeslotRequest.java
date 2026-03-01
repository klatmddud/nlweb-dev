package com.nlweb.timeslot.dto;

import java.time.LocalDateTime;

public record CreateTimeslotRequest(
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
