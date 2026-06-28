package com.afisha.main.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.afisha.main.enums.FriendshipsStatus;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberData {
    Long userId;
    String ownerName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime subscribeTime;
    FriendshipsStatus friendshipsStatus;
}
