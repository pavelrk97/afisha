package com.afisha.main.request.mapper;

import com.afisha.main.request.dto.ParticipationRequestDto;
import com.afisha.main.request.model.EventRequest;

public class EventRequestMapper {
    public static ParticipationRequestDto toRequestDto(EventRequest request) {
        if (request == null) {
            return null;
        }
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
