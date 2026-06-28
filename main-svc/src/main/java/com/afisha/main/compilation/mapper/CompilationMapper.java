package com.afisha.main.compilation.mapper;

import com.afisha.main.compilation.dto.CompilationDto;
import com.afisha.main.compilation.dto.NewCompilationDto;
import com.afisha.main.compilation.model.Compilation;
import com.afisha.main.event.dto.EventShortDto;
import com.afisha.main.event.model.Event;

import java.util.List;
import java.util.Set;

public class CompilationMapper {

    public static Compilation toCompilationFromNew(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}