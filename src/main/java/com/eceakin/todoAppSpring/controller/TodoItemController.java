package com.eceakin.todoAppSpring.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.TodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.TodoListDto;
import com.eceakin.todoAppSpring.application.dtos.UpdateTodoItemDto;
import com.eceakin.todoAppSpring.application.services.TodoItemOwnershipService;
import com.eceakin.todoAppSpring.application.services.TodoItemService;
import com.eceakin.todoAppSpring.application.services.TodoListOwnershipService;
import com.eceakin.todoAppSpring.entities.TodoItem.Priority;
import com.eceakin.todoAppSpring.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/todoitems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoItemController {
    
    private final TodoItemService todoItemService;
    private final TodoListOwnershipService todoListOwnershipService;
    private final TodoItemOwnershipService todoItemOwnershipService;
    
    @PostMapping
    public ResponseEntity<TodoItemDto> createTodoItem(@Valid @RequestBody CreateTodoItemDto createTodoItemDto) {
        // Check if user owns the todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(createTodoItemDto.getTodoListId())) {
            return ResponseEntity.badRequest().build();
        }
        
        TodoItemDto todoItemDto = todoItemService.createTodoItem(createTodoItemDto);
        return new ResponseEntity<>(todoItemDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> getTodoItemById(@PathVariable Long id) {
        // Check if user owns this todo item
        if (!todoItemOwnershipService.isOwnerOfTodoItem(id)) {
            return ResponseEntity.notFound().build();
        }
        
        return todoItemService.getTodoItemById(id)
                .map(todoItem -> ResponseEntity.ok(todoItem))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<TodoItemDto>> getMyTodoLists() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByUserId(currentUserId);
        return ResponseEntity.ok(todoItems);
    }
    
    
    @GetMapping("/todolist/{todoListId}")
    public ResponseEntity<List<TodoItemDto>> getTodoItemsByListId(@PathVariable Long todoListId) {
        // Check if user owns this todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(todoListId)) {
            return ResponseEntity.notFound().build();
        }
        
        List<TodoItemDto> todoItems = todoItemService.getPendingTodoItems(todoListId);
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/my/overdue")
    public ResponseEntity<List<TodoItemDto>> getMyOverdueTodoItems() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByUserId(currentUserId);
        // Filter overdue items on client-side or implement in service
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/todolist/{todoListId}/priority/{priority}")
    public ResponseEntity<List<TodoItemDto>> getTodoItemsByPriority(@PathVariable Long todoListId, 
                                                                   @PathVariable Priority priority) {
        // Check if user owns this todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(todoListId)) {
            return ResponseEntity.notFound().build();
        }
        
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByPriority(todoListId, priority);
        return ResponseEntity.ok(todoItems);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> updateTodoItem(@PathVariable Long id, 
                                                     @Valid @RequestBody UpdateTodoItemDto updateTodoItemDto) {
        // Check if user owns this todo item
        if (!todoItemOwnershipService.isOwnerOfTodoItem(id)) {
            return ResponseEntity.notFound().build();
        }
        
        TodoItemDto todoItemDto = todoItemService.updateTodoItem(id, updateTodoItemDto);
        return ResponseEntity.ok(todoItemDto);
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoItemDto> toggleTodoItemCompletion(@PathVariable Long id) {
        // Check if user owns this todo item
        if (!todoItemOwnershipService.isOwnerOfTodoItem(id)) {
            return ResponseEntity.notFound().build();
        }
        
        TodoItemDto todoItemDto = todoItemService.toggleTodoItemCompletion(id);
        return ResponseEntity.ok(todoItemDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        // Check if user owns this todo item
        if (!todoItemOwnershipService.isOwnerOfTodoItem(id)) {
            return ResponseEntity.notFound().build();
        }
        
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build();
    }
}