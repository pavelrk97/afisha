package com.afisha.main.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afisha.main.event.dto.EventFullDto;
import com.afisha.main.event.dto.EventSearchParams;
import com.afisha.main.event.dto.EventShortDto;
import com.afisha.main.event.service.EventService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> findAllByPublic(@Valid EventSearchParams searchEventParams,
                                                     HttpServletRequest request) {
        log.info("GET запрос на получения событий с фильтром");
        Collection<EventShortDto> events = eventService.findAllByPublic(searchEventParams, request);
        log.info("Отправлен ответ GET /events с телом: {}", events);
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("GET запрос /events/{}", eventId);
        EventFullDto event = eventService.findEventById(eventId, request);
        log.info("Отправлен ответ GET /events/{} с телом: {}", eventId, event);
        return event;
    }
}