package com.eceakin.todoAppSpring.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemDto {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private Priority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private Long todoListId;
    private String todoListTitle;
}
