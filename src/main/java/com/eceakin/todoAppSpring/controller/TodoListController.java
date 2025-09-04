package com.eceakin.todoAppSpring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eceakin.todoAppSpring.application.dtos.CreateTodoListDto;
import com.eceakin.todoAppSpring.application.dtos.TodoListDto;
import com.eceakin.todoAppSpring.application.services.TodoListOwnershipService;
import com.eceakin.todoAppSpring.application.services.TodoListService;
import com.eceakin.todoAppSpring.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/todolists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoListController {
    
    private final TodoListService todoListService;
    private final TodoListOwnershipService todoListOwnershipService;
    
    @PostMapping
    public ResponseEntity<TodoListDto> createTodoList(@Valid @RequestBody CreateTodoListDto createTodoListDto) {
        // Automatically set the current user as the owner
        createTodoListDto.setUserId(SecurityUtils.getCurrentUserId());
        TodoListDto todoListDto = todoListService.createTodoList(createTodoListDto);
        return new ResponseEntity<>(todoListDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoListDto> getTodoListById(@PathVariable Long id) {
        // Check if user owns this todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(id)) {
            return ResponseEntity.notFound().build();
        }
        
        return todoListService.getTodoListById(id)
                .map(todoList -> ResponseEntity.ok(todoList))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<TodoListDto>> getMyTodoLists() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<TodoListDto> todoLists = todoListService.getTodoListsByUserId(currentUserId);
        return ResponseEntity.ok(todoLists);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoListDto> updateTodoList(@PathVariable Long id, 
                                                    @Valid @RequestBody CreateTodoListDto updateTodoListDto) {
        // Check if user owns this todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(id)) {
            return ResponseEntity.notFound().build();
        }
        
        TodoListDto todoListDto = todoListService.updateTodoList(id, updateTodoListDto);
        return ResponseEntity.ok(todoListDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long id) {
        // Check if user owns this todo list
        if (!todoListOwnershipService.isOwnerOfTodoList(id)) {
            return ResponseEntity.notFound().build();
        }
        
        todoListService.deleteTodoList(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TodoListDto>> searchMyTodoListsByTitle(@RequestParam String title) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<TodoListDto> todoLists = todoListService.searchTodoListsByTitle(currentUserId, title);
        return ResponseEntity.ok(todoLists);
    }
}