package com.afisha.main.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.enums.RequestStatus;
import com.afisha.main.event.model.Event;
import com.afisha.main.user.model.User;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Enumerated(EnumType.STRING)
    RequestStatus status;
    LocalDateTime created;
}