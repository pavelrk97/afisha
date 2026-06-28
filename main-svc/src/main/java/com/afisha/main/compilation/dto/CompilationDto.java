package com.afisha.main.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.event.dto.EventShortDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    Long id;
    List<EventShortDto> events;
    Boolean pinned;
    String title;
}