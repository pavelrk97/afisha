package com.afisha.main.subscription.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.enums.FriendshipsStatus;
import com.afisha.main.user.model.User;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    User follower;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @Column(name = "subscribe_time")
    LocalDateTime subscribeTime;

    @Column(name = "unsubscribe_time")
    LocalDateTime unsubscribeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendships_status", nullable = false)
    FriendshipsStatus friendshipsStatus;
}

