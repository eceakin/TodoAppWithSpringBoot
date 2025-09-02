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
import com.eceakin.todoAppSpring.application.services.TodoListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/todolists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoListController {
    
    private final TodoListService todoListService;
    
    @PostMapping
    public ResponseEntity<TodoListDto> createTodoList(@Valid @RequestBody CreateTodoListDto createTodoListDto) {
        TodoListDto todoListDto = todoListService.createTodoList(createTodoListDto);
        return new ResponseEntity<>(todoListDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoListDto> getTodoListById(@PathVariable Long id) {
        return todoListService.getTodoListById(id)
                .map(todoList -> ResponseEntity.ok(todoList))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<TodoListDto>> getAllTodoLists() {
        List<TodoListDto> todoLists = todoListService.getAllTodoLists();
        return ResponseEntity.ok(todoLists);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoListDto>> getTodoListsByUserId(@PathVariable Long userId) {
        List<TodoListDto> todoLists = todoListService.getTodoListsByUserId(userId);
        return ResponseEntity.ok(todoLists);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoListDto> updateTodoList(@PathVariable Long id, 
                                                    @Valid @RequestBody CreateTodoListDto updateTodoListDto) {
        TodoListDto todoListDto = todoListService.updateTodoList(id, updateTodoListDto);
        return ResponseEntity.ok(todoListDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long id) {
        todoListService.deleteTodoList(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<TodoListDto>> searchTodoListsByTitle(@PathVariable Long userId, 
                                                                  @RequestParam String title) {
        List<TodoListDto> todoLists = todoListService.searchTodoListsByTitle(userId, title);
        return ResponseEntity.ok(todoLists);
    }
}