package ru.practicum.ewm.compilation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.compilation.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompilationServiceImplTest {

    @Mock
    private CompilationRepository compilationRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private CompilationServiceImpl compilationService;

    @Test
    void create_whenValidInput_thenReturnsSavedCompilation() {
        NewCompilationDto input = NewCompilationDto.builder()
                .title("Топ концерты")
                .pinned(true)
                .build();
        Compilation saved = Compilation.builder()
                .id(1L)
                .title("Топ концерты")
                .pinned(true)
                .events(new HashSet<>())
                .build();
        when(eventRepository.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());
        when(compilationRepository.save(any(Compilation.class))).thenReturn(saved);

        CompilationDto result = compilationService.create(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Топ концерты");
        assertThat(result.getPinned()).isTrue();
        verify(compilationRepository).save(any(Compilation.class));
    }

    @Test
    void findCompilationById_whenNotFound_thenThrowsNotFound() {
        when(compilationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compilationService.findCompilationById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void delete_whenCalled_thenDelegatesToRepository() {
        compilationService.delete(5L);

        verify(compilationRepository).deleteById(5L);
    }
}