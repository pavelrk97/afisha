package com.afisha.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.enums.EventState;
import com.afisha.main.location.dto.LocationDto;
import com.afisha.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    Long id;
    String title;
    String annotation;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    UserShortDto initiator;
    CategoryDto category;
    @Builder.Default
    long views = 0L;
    @Builder.Default
    long confirmedRequests = 0L;
}