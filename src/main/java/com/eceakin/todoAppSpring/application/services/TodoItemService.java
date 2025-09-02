package com.eceakin.todoAppSpring.application.services;

import java.util.List;
import java.util.Optional;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.TodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.UpdateTodoItemDto;
import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

public interface TodoItemService {
    
    TodoItemDto createTodoItem(CreateTodoItemDto createTodoItemDto);
    
    Optional<TodoItemDto> getTodoItemById(Long id);
    
    List<TodoItemDto> getTodoItemsByListId(Long todoListId);
    
    List<TodoItemDto> getTodoItemsByUserId(Long userId);
    
    List<TodoItemDto> getCompletedTodoItems(Long todoListId);
    
    List<TodoItemDto> getPendingTodoItems(Long todoListId);
    
    List<TodoItemDto> getOverdueTodoItems();
    
    TodoItemDto updateTodoItem(Long id, UpdateTodoItemDto updateTodoItemDto);
    
    TodoItemDto toggleTodoItemCompletion(Long id);
    
    void deleteTodoItem(Long id);
    
    List<TodoItemDto> getTodoItemsByPriority(Long todoListId, Priority priority);
}