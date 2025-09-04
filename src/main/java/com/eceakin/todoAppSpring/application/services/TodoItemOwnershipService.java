package com.eceakin.todoAppSpring.application.services;

import org.springframework.stereotype.Service;

import com.eceakin.todoAppSpring.infrastructure.repositories.TodoItemRepository;
import com.eceakin.todoAppSpring.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoItemOwnershipService {
    
    private final TodoItemRepository todoItemRepository;
    
    public boolean isOwnerOfTodoItem(Long todoItemId) {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            return todoItemRepository.findById(todoItemId)
                    .map(todoItem -> todoItem.getTodoList().getUser().getId().equals(currentUserId))
                    .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }
}