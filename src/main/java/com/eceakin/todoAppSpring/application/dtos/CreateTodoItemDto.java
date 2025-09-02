package com.eceakin.todoAppSpring.application.dtos;

import java.time.LocalDate;

import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoItemDto {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private Priority priority = Priority.MEDIUM;
    private LocalDate dueDate;
    private Long todoListId;
}