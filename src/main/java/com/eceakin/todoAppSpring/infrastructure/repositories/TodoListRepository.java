package com.eceakin.todoAppSpring.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eceakin.todoAppSpring.entities.TodoList;


@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    
    List<TodoList> findByUserId(Long userId);
    
    List<TodoList> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT tl FROM TodoList tl WHERE tl.user.id = :userId AND tl.title LIKE %:title%")
    List<TodoList> findByUserIdAndTitleContaining(@Param("userId") Long userId, @Param("title") String title);
    
    @Query("SELECT tl FROM TodoList tl JOIN FETCH tl.todoItems WHERE tl.id = :id")
    Optional<TodoList> findByIdWithTodoItems(@Param("id") Long id);
    
    @Query("SELECT tl FROM TodoList tl JOIN FETCH tl.todoItems ti WHERE tl.user.id = :userId")
    List<TodoList> findByUserIdWithTodoItems(@Param("userId") Long userId);
    
    long countByUserId(Long userId);
}