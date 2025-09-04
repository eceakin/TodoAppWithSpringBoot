package com.eceakin.todoAppSpring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eceakin.todoAppSpring.application.dtos.CreateUserDto;
import com.eceakin.todoAppSpring.application.dtos.UserDto;
import com.eceakin.todoAppSpring.application.services.UserService;
import com.eceakin.todoAppSpring.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
private final UserService userService;
    
    /* @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userService.getUserById(currentUserId)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    } */  @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody CreateUserDto updateUserDto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserDto userDto = userService.updateUser(currentUserId, updateUserDto);
        return ResponseEntity.ok(userDto);
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userService.deleteUser(currentUserId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/me/with-todolists")
    public ResponseEntity<UserDto> getCurrentUserWithTodoLists() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userService.getUserWithTodoLists(currentUserId)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
}