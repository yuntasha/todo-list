package practice.project.todo_list.dao;

import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.*;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoDao {
    List<Todo> findAll();
    List<Todo> findByState(int state);
    Optional<Todo> findById(int id);
    int postTodo(Todo Todo);
    int setDeleteStateById(int id, int state, int nowState);
    List<Todo> findByPeriod(LocalDate start, LocalDate end);
    int patchState(int id, int state);
    List<Todo> findDelete();
    int updateTodo(Todo todo);
    int deleteBefore(LocalDate cutLine);
    List<Todo> offsetPaging(int size, int offset);
    int countTodo();
    List<Todo> offsetPagingByState(int state, int size, int offset);
    int countTodoByState(int state);
}