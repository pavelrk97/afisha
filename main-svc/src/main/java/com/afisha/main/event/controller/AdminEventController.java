package com.afisha.main.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.afisha.main.event.dto.EventFullDto;
import com.afisha.main.event.dto.EventSearchParams;
import com.afisha.main.event.dto.UpdateEventAdminRequest;
import com.afisha.main.event.service.EventService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> findAllByAdmin(@Valid EventSearchParams searchEventParams,
                                                   HttpServletRequest request) {
        log.info("GET запрос на получения событий с фильтром");
        Collection<EventFullDto> events = eventService.findAllByAdmin(searchEventParams, request);
        log.info("Отправлен ответ GET /admin/events с телом: {}", events);
        return events;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest eventDto) {
        log.info("PATCH запрос /admin/events/{} с телом {}", eventId, eventDto);
        EventFullDto event = eventService.updateEventByAdmin(eventId, eventDto);
        log.info("Отправлен ответ PATCH /admin/events/{} с телом: {}", eventId, event);
        return event;
    }
}