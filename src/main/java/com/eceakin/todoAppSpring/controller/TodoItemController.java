package com.eceakin.todoAppSpring.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.TodoItemDto;
import com.eceakin.todoAppSpring.application.dtos.UpdateTodoItemDto;
import com.eceakin.todoAppSpring.application.services.TodoItemService;
import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

import java.util.List;

@RestController
@RequestMapping("/api/todoitems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoItemController {
    
    private final TodoItemService todoItemService;
    
    @PostMapping
    public ResponseEntity<TodoItemDto> createTodoItem(@Valid @RequestBody CreateTodoItemDto createTodoItemDto) {
        TodoItemDto todoItemDto = todoItemService.createTodoItem(createTodoItemDto);
        return new ResponseEntity<>(todoItemDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> getTodoItemById(@PathVariable Long id) {
        return todoItemService.getTodoItemById(id)
                .map(todoItem -> ResponseEntity.ok(todoItem))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/todolist/{todoListId}")
    public ResponseEntity<List<TodoItemDto>> getTodoItemsByListId(@PathVariable Long todoListId) {
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByListId(todoListId);
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoItemDto>> getTodoItemsByUserId(@PathVariable Long userId) {
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByUserId(userId);
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/todolist/{todoListId}/completed")
    public ResponseEntity<List<TodoItemDto>> getCompletedTodoItems(@PathVariable Long todoListId) {
        List<TodoItemDto> todoItems = todoItemService.getCompletedTodoItems(todoListId);
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/todolist/{todoListId}/pending")
    public ResponseEntity<List<TodoItemDto>> getPendingTodoItems(@PathVariable Long todoListId) {
        List<TodoItemDto> todoItems = todoItemService.getPendingTodoItems(todoListId);
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<TodoItemDto>> getOverdueTodoItems() {
        List<TodoItemDto> todoItems = todoItemService.getOverdueTodoItems();
        return ResponseEntity.ok(todoItems);
    }
    
    @GetMapping("/todolist/{todoListId}/priority/{priority}")
    public ResponseEntity<List<TodoItemDto>> getTodoItemsByPriority(@PathVariable Long todoListId, 
                                                                   @PathVariable Priority priority) {
        List<TodoItemDto> todoItems = todoItemService.getTodoItemsByPriority(todoListId, priority);
        return ResponseEntity.ok(todoItems);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> updateTodoItem(@PathVariable Long id, 
                                                     @Valid @RequestBody UpdateTodoItemDto updateTodoItemDto) {
        TodoItemDto todoItemDto = todoItemService.updateTodoItem(id, updateTodoItemDto);
        return ResponseEntity.ok(todoItemDto);
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoItemDto> toggleTodoItemCompletion(@PathVariable Long id) {
        TodoItemDto todoItemDto = todoItemService.toggleTodoItemCompletion(id);
        return ResponseEntity.ok(todoItemDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build();
    }
}