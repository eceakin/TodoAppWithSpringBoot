package com.eceakin.todoAppSpring.application.concretes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoListDto;
import com.eceakin.todoAppSpring.application.dtos.TodoListDto;
import com.eceakin.todoAppSpring.application.services.TodoListService;
import com.eceakin.todoAppSpring.entities.TodoList;
import com.eceakin.todoAppSpring.entities.User;
import com.eceakin.todoAppSpring.infrastructure.repositories.TodoItemRepository;
import com.eceakin.todoAppSpring.infrastructure.repositories.TodoListRepository;
import com.eceakin.todoAppSpring.infrastructure.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TodoListManager implements TodoListService {
    
    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;
    private final TodoItemRepository todoItemRepository;
    
    @Override
    public TodoListDto createTodoList(CreateTodoListDto createTodoListDto) {
        User user = findUserById(createTodoListDto.getUserId());
        
        TodoList todoList = buildTodoListFromDto(createTodoListDto, user);
        TodoList savedTodoList = todoListRepository.save(todoList);
        return convertToDto(savedTodoList);
    }
    
    @Override
    public Optional<TodoListDto> getTodoListById(Long id) {
        return todoListRepository.findById(id).map(this::convertToDto);
    }
    
    @Override
    public List<TodoListDto> getTodoListsByUserId(Long userId) {
        return todoListRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TodoListDto> getAllTodoLists() {
        return todoListRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public TodoListDto updateTodoList(Long id, CreateTodoListDto updateTodoListDto) {
        TodoList todoList = findTodoListById(id);
        
        updateTodoListFields(todoList, updateTodoListDto);
        TodoList savedTodoList = todoListRepository.save(todoList);
        return convertToDto(savedTodoList);
    }
    
    @Override
    public void deleteTodoList(Long id) {
        if (!todoListRepository.existsById(id)) {
            throw new IllegalArgumentException("TodoList not found with id: " + id);
        }
        todoListRepository.deleteById(id);
    }
    
    @Override
    public List<TodoListDto> searchTodoListsByTitle(Long userId, String title) {
        return todoListRepository.findByUserIdAndTitleContaining(userId, title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Private helper methods
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
    
    private TodoList findTodoListById(Long id) {
        return todoListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoList not found with id: " + id));
    }
    
    private TodoList buildTodoListFromDto(CreateTodoListDto createTodoListDto, User user) {
        TodoList todoList = new TodoList();
        todoList.setTitle(createTodoListDto.getTitle());
        todoList.setDescription(createTodoListDto.getDescription());
        todoList.setUser(user);
        return todoList;
    }
    
    private void updateTodoListFields(TodoList todoList, CreateTodoListDto updateTodoListDto) {
        todoList.setTitle(updateTodoListDto.getTitle());
        todoList.setDescription(updateTodoListDto.getDescription());
    }
    
    private TodoListDto convertToDto(TodoList todoList) {
        TodoListDto dto = new TodoListDto();
        dto.setId(todoList.getId());
        dto.setTitle(todoList.getTitle());
        dto.setDescription(todoList.getDescription());
        dto.setCreatedAt(todoList.getCreatedAt());
        dto.setUpdatedAt(todoList.getUpdatedAt());
        dto.setUserId(todoList.getUser().getId());
        dto.setUsername(todoList.getUser().getUsername());
        
        // Calculate statistics
        Long totalItems = todoItemRepository.countByTodoListId(todoList.getId());
        Long completedItems = todoItemRepository.countByTodoListIdAndCompleted(todoList.getId(), true);
        
        dto.setTotalItems(totalItems.intValue());
        dto.setCompletedItems(completedItems.intValue());
        
        return dto;
    }}