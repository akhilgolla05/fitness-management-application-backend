package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.UserDto;
import com.learnboot.fitnessmanagementsystem.request.UserCreationRequest;
import com.learnboot.fitnessmanagementsystem.request.UserUpdateRequest;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreationRequest request) {
        UserDto user =  userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse("User Created SuccessFully!",user));
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User Deleted Successfully!",null));
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable long userId) {
        UserDto user =  userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse("User Retrieved Successfully!",user));
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<ApiResponse> CreateUser(@RequestBody UserUpdateRequest request, @PathVariable long userId) {
        UserDto user =  userService.updateUser(request, userId);
        return ResponseEntity.ok(new ApiResponse("User Updated Successfully",user));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDto> users =  userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("User Retrieved Successfully!",users));
    }




}
