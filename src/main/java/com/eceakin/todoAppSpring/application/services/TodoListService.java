package com.eceakin.todoAppSpring.application.services;

import java.util.List;
import java.util.Optional;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoListDto;
import com.eceakin.todoAppSpring.application.dtos.TodoListDto;

public interface TodoListService {
    
    TodoListDto createTodoList(CreateTodoListDto createTodoListDto);
    
    Optional<TodoListDto> getTodoListById(Long id);
    
    List<TodoListDto> getTodoListsByUserId(Long userId);
    
    List<TodoListDto> getAllTodoLists();
    
    TodoListDto updateTodoList(Long id, CreateTodoListDto updateTodoListDto);
    
    void deleteTodoList(Long id);
    
    List<TodoListDto> searchTodoListsByTitle(Long userId, String title);
}