package com.afisha.main.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.afisha.main.exception.DuplicatedDataException;
import com.afisha.main.user.UserRepository;
import com.afisha.main.user.dto.NewUserRequest;
import com.afisha.main.user.dto.UserDto;
import com.afisha.main.user.mapper.UserMapper;
import com.afisha.main.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new DuplicatedDataException("Email уже зарегистрирован: " + newUserRequest.getEmail());
        }
        log.info("Создание пользователя с данными: {}", newUserRequest);
        User user = UserMapper.toNewUserFromRequest(newUserRequest);
        User createdUser = userRepository.save(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        int page = from > 0 ? from / size : 0;
        Pageable pageable = PageRequest.of(page, size);
        return (ids != null) ? userRepository.findByIdIn(ids, pageable)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList()) : userRepository.findAll(pageable)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}