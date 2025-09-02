package com.eceakin.todoAppSpring.application.services;

import java.util.List;
import java.util.Optional;

import com.eceakin.todoAppSpring.application.dtos.CreateUserDto;
import com.eceakin.todoAppSpring.application.dtos.UserDto;


public interface UserService {
    
    UserDto createUser(CreateUserDto createUserDto);
    
    Optional<UserDto> getUserById(Long id);
    
    Optional<UserDto> getUserByUsername(String username);
    
    List<UserDto> getAllUsers();
    
    UserDto updateUser(Long id, CreateUserDto updateUserDto);
    
    void deleteUser(Long id);
    
    List<UserDto> searchUsers(String keyword);
    Optional<UserDto> getUserWithTodoLists(Long id);
}