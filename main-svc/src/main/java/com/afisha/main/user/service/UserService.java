package com.afisha.main.user.service;

import com.afisha.main.user.dto.NewUserRequest;
import com.afisha.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUserRequest);

    void delete(Long id);

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);
}
