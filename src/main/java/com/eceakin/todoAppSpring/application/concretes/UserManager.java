package com.eceakin.todoAppSpring.application.concretes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eceakin.todoAppSpring.application.dtos.CreateUserDto;
import com.eceakin.todoAppSpring.application.dtos.TodoListDto;
import com.eceakin.todoAppSpring.application.dtos.UserDto;
import com.eceakin.todoAppSpring.application.services.UserService;
import com.eceakin.todoAppSpring.entities.TodoList;
import com.eceakin.todoAppSpring.entities.User;
import com.eceakin.todoAppSpring.infrastructure.repositories.TodoItemRepository;
import com.eceakin.todoAppSpring.infrastructure.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UserManager implements UserService {
    
    private final UserRepository userRepository;
    private final TodoItemRepository todoItemRepository;
    
    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        validateUserUniqueness(createUserDto);
        
        User user = buildUserFromDto(createUserDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findByIdWithTodoLists(id).map(this::convertToDtoWithTodoLists);
    }
    
    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDto);
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    // Load user with todo lists for complete information
                    return userRepository.findByIdWithTodoLists(user.getId())
                            .map(this::convertToDtoWithTodoLists)
                            .orElse(convertToDto(user));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDto updateUser(Long id, CreateUserDto updateUserDto) {
        User existingUser = findUserById(id);
        validateUserUniquenessForUpdate(existingUser, updateUserDto);
        
        updateUserFields(existingUser, updateUserDto);
        User savedUser = userRepository.save(existingUser);
        return convertToDto(savedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    public List<UserDto> searchUsers(String keyword) {
        return userRepository.findByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<UserDto> getUserWithTodoLists(Long id) {
        return userRepository.findByIdWithTodoLists(id).map(this::convertToDtoWithTodoLists);
    }
    
    // Private helper methods
    private void validateUserUniqueness(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + createUserDto.getUsername());
        }
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + createUserDto.getEmail());
        }
    }
    
    private void validateUserUniquenessForUpdate(User existingUser, CreateUserDto updateUserDto) {
        if (!existingUser.getUsername().equals(updateUserDto.getUsername()) && 
            userRepository.existsByUsername(updateUserDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + updateUserDto.getUsername());
        }
        
        if (!existingUser.getEmail().equals(updateUserDto.getEmail()) && 
            userRepository.existsByEmail(updateUserDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + updateUserDto.getEmail());
        }
    }
    
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
    
    private User buildUserFromDto(CreateUserDto createUserDto) {
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword()); // In real app, hash the password
        return user;
    }
    
    private void updateUserFields(User user, CreateUserDto updateUserDto) {
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isEmpty()) {
            user.setPassword(updateUserDto.getPassword()); // In real app, hash the password
        }
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setTodoLists(null); // No todo lists loaded
        return dto;
    }
    
    private UserDto convertToDtoWithTodoLists(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Convert todo lists to DTOs
        if (user.getTodoLists() != null) {
            List<TodoListDto> todoListDtos = user.getTodoLists().stream()
                    .map(this::convertTodoListToDto)
                    .collect(Collectors.toList());
            dto.setTodoLists(todoListDtos);
        } else {
            dto.setTodoLists(null);
        }
        
        return dto;
    }
    
    private TodoListDto convertTodoListToDto(TodoList todoList) {
        TodoListDto dto = new TodoListDto();
        dto.setId(todoList.getId());
        dto.setTitle(todoList.getTitle());
        dto.setDescription(todoList.getDescription());
        dto.setCreatedAt(todoList.getCreatedAt());
        dto.setUpdatedAt(todoList.getUpdatedAt());
        dto.setUserId(todoList.getUser().getId());
        dto.setUsername(todoList.getUser().getUsername());
        
        // Calculate statistics (without loading all todo items for performance)
        Long totalItems = todoItemRepository.countByTodoListId(todoList.getId());
        Long completedItems = todoItemRepository.countByTodoListIdAndCompleted(todoList.getId(), true);
        
        dto.setTotalItems(totalItems.intValue());
        dto.setCompletedItems(completedItems.intValue());
        dto.setTodoItems(null); // Don't load items in user context to avoid deep nesting
        
        return dto;
    }
}

