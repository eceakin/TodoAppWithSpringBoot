package com.eceakin.todoAppSpring.application.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoItemDto {
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private Boolean completed;
    private Priority priority;
    private LocalDateTime dueDate;
}