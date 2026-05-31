package ru.practicum.ewm.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.DuplicatedDataException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.EventRequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequest;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRequestServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private EventRepository eventRepository;
    @Mock private EventRequestRepository eventRequestRepository;

    @InjectMocks
    private EventRequestServiceImpl service;

    @Test
    void create_whenUserNotFound_thenThrowsNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(1L, 10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void create_whenInitiatorTriesToRequestOwnEvent_thenThrows() {
        User user = User.builder().id(1L).name("Иван").email("i@m.ru").build();
        Event event = Event.builder()
                .id(10L)
                .initiator(user)
                .state(EventState.PUBLISHED)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(eventRequestRepository.existsByRequesterIdAndEventId(1L, 10L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(1L, 10L))
                .isInstanceOf(DuplicatedDataException.class)
                .hasMessageContaining("Инициатор");
    }

    @Test
    void create_whenEventNotPublished_thenThrows() {
        User requester = User.builder().id(1L).build();
        User initiator = User.builder().id(2L).build();
        Event event = Event.builder()
                .id(10L)
                .initiator(initiator)
                .state(EventState.PENDING)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(requester));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(eventRequestRepository.existsByRequesterIdAndEventId(1L, 10L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(1L, 10L))
                .isInstanceOf(DuplicatedDataException.class)
                .hasMessageContaining("неопубликованном");
    }

    @Test
    void cancel_whenForeignRequest_thenThrowsForbidden() {
        User caller = User.builder().id(1L).build();
        User owner = User.builder().id(2L).build();
        EventRequest request = EventRequest.builder()
                .id(100L)
                .requester(owner)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(caller));
        when(eventRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        assertThatThrownBy(() -> service.cancelRequest(1L, 100L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("собственный");
    }

    @Test
    void cancel_whenOwnRequest_thenSetsCanceledStatus() {
        User caller = User.builder().id(1L).build();
        EventRequest request = EventRequest.builder()
                .id(100L)
                .requester(caller)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(caller));
        when(eventRequestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(eventRequestRepository.save(any(EventRequest.class))).thenReturn(request);

        ParticipationRequestDto result = service.cancelRequest(1L, 100L);

        assertThat(result).isNotNull();
        verify(eventRequestRepository).save(any(EventRequest.class));
    }
}