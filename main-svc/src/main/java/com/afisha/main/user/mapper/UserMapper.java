package com.afisha.main.user.mapper;

import com.afisha.main.user.dto.NewUserRequest;
import com.afisha.main.user.dto.UserDto;
import com.afisha.main.user.dto.UserShortDto;
import com.afisha.main.user.model.User;

public class UserMapper {

    public static User toNewUserFromRequest(NewUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .allowSubscriptions(true)
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .allowSubscriptions(user.isAllowSubscriptions())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}