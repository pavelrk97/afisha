package com.afisha.main.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.afisha.main.category.CategoryRepository;
import com.afisha.main.enums.EventState;
import com.afisha.main.enums.StateAction;
import com.afisha.main.event.EventRepository;
import com.afisha.main.event.dto.NewEventDto;
import com.afisha.main.event.dto.UpdateEventAdminRequest;
import com.afisha.main.event.dto.UpdateEventUserRequest;
import com.afisha.main.event.model.Event;
import com.afisha.main.exception.ConflictException;
import com.afisha.main.exception.NotFoundException;
import com.afisha.main.exception.ValidationException;
import com.afisha.main.location.LocationRepository;
import com.afisha.main.request.EventRequestRepository;
import com.afisha.main.stats.AsyncStatsClient;
import com.afisha.main.user.UserRepository;
import com.afisha.main.user.model.User;
import com.afisha.stats.StatisticsClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock private EventRepository eventRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private UserRepository userRepository;
    @Mock private EventRequestRepository eventRequestRepository;
    @Mock private StatisticsClient statClient;
    @Mock private AsyncStatsClient asyncStatsClient;
    @Mock private ObjectMapper mapper;

    @InjectMocks
    private EventServiceImpl eventService;

    // ---------- create ----------

    @Test
    void create_whenEventDateTooSoon_thenThrowsValidation() {
        NewEventDto dto = NewEventDto.builder()
                .eventDate(LocalDateTime.now().plusMinutes(30)) // < 2h
                .build();

        assertThatThrownBy(() -> eventService.create(1L, dto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("не ранее");
    }

    @Test
    void create_whenCategoryNotFound_thenThrowsNotFound() {
        NewEventDto dto = NewEventDto.builder()
                .eventDate(LocalDateTime.now().plusHours(5))
                .category(99L)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.create(1L, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Категория");
    }

    // ---------- updateEventByAdmin ----------

    @Test
    void updateByAdmin_whenPublishingNonPending_thenThrowsConflict() {
        Event existing = Event.builder()
                .id(10L)
                .state(EventState.PUBLISHED)
                .eventDate(LocalDateTime.now().plusHours(5))
                .build();
        UpdateEventAdminRequest req = UpdateEventAdminRequest.builder()
                .stateAction(StateAction.PUBLISH_EVENT)
                .build();
        when(eventRepository.findById(10L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> eventService.updateEventByAdmin(10L, req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("опубликовать");
    }

    @Test
    void updateByAdmin_whenRejectingPublished_thenThrowsConflict() {
        Event existing = Event.builder()
                .id(10L)
                .state(EventState.PUBLISHED)
                .eventDate(LocalDateTime.now().plusHours(5))
                .build();
        UpdateEventAdminRequest req = UpdateEventAdminRequest.builder()
                .stateAction(StateAction.REJECT_EVENT)
                .build();
        when(eventRepository.findById(10L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> eventService.updateEventByAdmin(10L, req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("опубликованное");
    }

    // ---------- updateEventByPrivate ----------

    @Test
    void updateByPrivate_whenNotInitiator_thenThrowsNotFound() {
        User caller = User.builder().id(1L).build();
        User initiator = User.builder().id(2L).build();
        Event event = Event.builder()
                .id(10L)
                .initiator(initiator)
                .state(EventState.PENDING)
                .eventDate(LocalDateTime.now().plusHours(5))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(caller));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        UpdateEventUserRequest req = UpdateEventUserRequest.builder().build();

        assertThatThrownBy(() -> eventService.updateEventByPrivate(1L, 10L, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("инициатора");
    }

    @Test
    void updateByPrivate_whenEventPublished_thenThrowsConflict() {
        User user = User.builder().id(1L).build();
        Event event = Event.builder()
                .id(10L)
                .initiator(user)
                .state(EventState.PUBLISHED)
                .eventDate(LocalDateTime.now().plusHours(5))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        UpdateEventUserRequest req = UpdateEventUserRequest.builder().build();

        assertThatThrownBy(() -> eventService.updateEventByPrivate(1L, 10L, req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("опубликованное");
    }

    // ---------- findEventById (public) ----------

    @Test
    void findEventById_whenEventNotPublished_thenThrowsNotFound() {
        Event event = Event.builder()
                .id(10L)
                .state(EventState.PENDING)
                .build();
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        assertThatThrownBy(() -> eventService.findEventById(10L, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не опубликовано");
    }

    @Test
    void findEventById_whenEventNotExists_thenThrowsNotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.findEventById(999L, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("999");
    }
}