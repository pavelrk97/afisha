package com.afisha.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    @Builder.Default
    long views = 0L;

    @Builder.Default
    long confirmedRequests = 0L;
}