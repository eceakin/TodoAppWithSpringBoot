package com.eceakin.todoAppSpring.infrastructure.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eceakin.todoAppSpring.entities.TodoItem;
import com.eceakin.todoAppSpring.entities.TodoItem.Priority;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    
    List<TodoItem> findByTodoListId(Long todoListId);
    
    List<TodoItem> findByTodoListIdOrderByCreatedAtDesc(Long todoListId);
    
    List<TodoItem> findByTodoListIdAndCompleted(Long todoListId, Boolean completed);
    
    List<TodoItem> findByTodoListIdAndPriority(Long todoListId, Priority priority);
    
    @Query("SELECT ti FROM TodoItem ti WHERE ti.todoList.user.id = :userId")
    List<TodoItem> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ti FROM TodoItem ti WHERE ti.todoList.user.id = :userId AND ti.completed = :completed")
    List<TodoItem> findByUserIdAndCompleted(@Param("userId") Long userId, @Param("completed") Boolean completed);
    
    @Query("SELECT ti FROM TodoItem ti WHERE ti.dueDate <= :date AND ti.completed = false")
    List<TodoItem> findOverdueTodoItems(@Param("date") LocalDateTime date);
    
    @Query("SELECT ti FROM TodoItem ti WHERE ti.todoList.user.id = :userId AND ti.dueDate BETWEEN :startDate AND :endDate")
    List<TodoItem> findByUserIdAndDueDateBetween(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    long countByTodoListId(Long todoListId);
    
    long countByTodoListIdAndCompleted(Long todoListId, Boolean completed);
    
    @Query("SELECT COUNT(ti) FROM TodoItem ti WHERE ti.todoList.user.id = :userId AND ti.completed = :completed")
    long countByUserIdAndCompleted(@Param("userId") Long userId, @Param("completed") Boolean completed);
}