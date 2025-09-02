package com.eceakin.todoAppSpring.application.concretes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.TodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.UpdateTodoItemDto;
import com.eceakin.todoAppSpring.application.services.TodoItemService;
import com.eceakin.todoAppSpring.entities.TodoItem;
import com.eceakin.todoAppSpring.entities.TodoItem.Priority;
import com.eceakin.todoAppSpring.entities.TodoList;
import com.eceakin.todoAppSpring.infrastructure.repositories.TodoItemRepository;
import com.eceakin.todoAppSpring.infrastructure.repositories.TodoListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class TodoItemManager implements TodoItemService {
    
    private final TodoItemRepository todoItemRepository;
    private final TodoListRepository todoListRepository;
    
    @Override
    public TodoItemDto createTodoItem(CreateTodoItemDto createTodoItemDto) {
        TodoList todoList = findTodoListById(createTodoItemDto.getTodoListId());
        
        TodoItem todoItem = buildTodoItemFromDto(createTodoItemDto, todoList);
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        return convertToDto(savedTodoItem);
    }
    
    @Override
    public Optional<TodoItemDto> getTodoItemById(Long id) {
        return todoItemRepository.findById(id).map(this::convertToDto);
    }
    
    @Override
    public List<TodoItemDto> getTodoItemsByListId(Long todoListId) {
        return todoItemRepository.findByTodoListIdOrderByCreatedAtDesc(todoListId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TodoItemDto> getTodoItemsByUserId(Long userId) {
        return todoItemRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TodoItemDto> getCompletedTodoItems(Long todoListId) {
        return todoItemRepository.findByTodoListIdAndCompleted(todoListId, true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TodoItemDto> getPendingTodoItems(Long todoListId) {
        return todoItemRepository.findByTodoListIdAndCompleted(todoListId, false).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TodoItemDto> getOverdueTodoItems() {
        return todoItemRepository.findOverdueTodoItems(LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public TodoItemDto updateTodoItem(Long id, UpdateTodoItemDto updateTodoItemDto) {
        TodoItem todoItem = findTodoItemById(id);
        
        updateTodoItemFields(todoItem, updateTodoItemDto);
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        return convertToDto(savedTodoItem);
    }
    
    @Override
    public TodoItemDto toggleTodoItemCompletion(Long id) {
        TodoItem todoItem = findTodoItemById(id);
        
        todoItem.setCompleted(!todoItem.getCompleted());
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        return convertToDto(savedTodoItem);
    }
    
    @Override
    public void deleteTodoItem(Long id) {
        if (!todoItemRepository.existsById(id)) {
            throw new IllegalArgumentException("TodoItem not found with id: " + id);
        }
        todoItemRepository.deleteById(id);
    }
    
    @Override
    public List<TodoItemDto> getTodoItemsByPriority(Long todoListId, Priority priority) {
        return todoItemRepository.findByTodoListIdAndPriority(todoListId, priority).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Private helper methods
    private TodoList findTodoListById(Long todoListId) {
        return todoListRepository.findById(todoListId)
                .orElseThrow(() -> new IllegalArgumentException("TodoList not found with id: " + todoListId));
    }
    
    private TodoItem findTodoItemById(Long id) {
        return todoItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem not found with id: " + id));
    }
    
    private TodoItem buildTodoItemFromDto(CreateTodoItemDto createTodoItemDto, TodoList todoList) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(createTodoItemDto.getTitle());
        todoItem.setDescription(createTodoItemDto.getDescription());
        todoItem.setPriority(createTodoItemDto.getPriority());
      //  todoItem.setDueDate(createTodoItemDto.getDueDate());
        todoItem.setTodoList(todoList);
        return todoItem;
    }
    
    private void updateTodoItemFields(TodoItem todoItem, UpdateTodoItemDto updateTodoItemDto) {
        if (updateTodoItemDto.getTitle() != null) {
            todoItem.setTitle(updateTodoItemDto.getTitle());
        }
        if (updateTodoItemDto.getDescription() != null) {
            todoItem.setDescription(updateTodoItemDto.getDescription());
        }
        if (updateTodoItemDto.getCompleted() != null) {
            todoItem.setCompleted(updateTodoItemDto.getCompleted());
        }
        if (updateTodoItemDto.getPriority() != null) {
            todoItem.setPriority(updateTodoItemDto.getPriority());
        }
        if (updateTodoItemDto.getDueDate() != null) {
            todoItem.setDueDate(updateTodoItemDto.getDueDate());
        }
    }
    
    private TodoItemDto convertToDto(TodoItem todoItem) {
        TodoItemDto dto = new TodoItemDto();
        dto.setId(todoItem.getId());
        dto.setTitle(todoItem.getTitle());
        dto.setDescription(todoItem.getDescription());
        dto.setCompleted(todoItem.getCompleted());
        dto.setPriority(todoItem.getPriority());
        dto.setDueDate(todoItem.getDueDate());
        dto.setCreatedAt(todoItem.getCreatedAt());
        dto.setUpdatedAt(todoItem.getUpdatedAt());
        dto.setCompletedAt(todoItem.getCompletedAt());
        dto.setTodoListId(todoItem.getTodoList().getId());
        dto.setTodoListTitle(todoItem.getTodoList().getTitle());
        return dto;
    }
}