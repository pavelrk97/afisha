package com.afisha.main.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.enums.FriendshipsStatus;
import com.afisha.main.user.dto.UserDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    Long id;
    Long followerId;
    UserDto owner;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime subscribeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime unsubscribeTime;
    FriendshipsStatus friendshipsStatus;
}

