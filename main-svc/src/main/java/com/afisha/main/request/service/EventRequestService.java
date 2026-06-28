package com.afisha.main.request.service;

import com.afisha.main.request.dto.EventRequestStatusUpdateRequest;
import com.afisha.main.request.dto.EventRequestStatusUpdateResult;
import com.afisha.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventRequestService {

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    List<ParticipationRequestDto> getParticipationRequestsForUserEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                EventRequestStatusUpdateRequest dto);
}