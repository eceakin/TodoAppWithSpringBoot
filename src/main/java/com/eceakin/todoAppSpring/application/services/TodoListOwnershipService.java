package com.eceakin.todoAppSpring.application.services;

import org.springframework.stereotype.Service;

import com.eceakin.todoAppSpring.infrastructure.repositories.TodoListRepository;
import com.eceakin.todoAppSpring.security.SecurityUtils;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class TodoListOwnershipService {
	 private final TodoListRepository todoListRepository;
	    
	    public boolean isOwnerOfTodoList(Long todoListId) {
	        try {
	            Long currentUserId = SecurityUtils.getCurrentUserId();
	            return todoListRepository.findById(todoListId)
	                    .map(todoList -> todoList.getUser().getId().equals(currentUserId))
	                    .orElse(false);
	        } catch (Exception e) {
	            return false;
	        }
	    }
}
