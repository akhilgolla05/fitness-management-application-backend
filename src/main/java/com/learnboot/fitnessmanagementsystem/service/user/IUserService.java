package com.learnboot.fitnessmanagementsystem.service.user;

import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.UserDto;
import com.learnboot.fitnessmanagementsystem.request.UserCreationRequest;
import com.learnboot.fitnessmanagementsystem.request.UserUpdateRequest;

import java.util.List;

public interface IUserService {
    UserDto createUser(UserCreationRequest request);

    UserDto getUserById(Long userId);

    UserDto updateUser(UserUpdateRequest request, long userId);

    void deleteUser(long userId);

    List<UserDto> getAllUsers();
}
