package practice.project.todo_list.dao;

import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoDao {
    List<TodoTitleDto> findAll();
    List<TodoTitleDto> findByState(int state);
    Optional<TodoDetailDto> findById(int id);
    int postTodo(PostRequestDto postRequestDto);
    int setDeleteStateByid(int id);
    void deleteByLocalDate(LocalDateTime now);
}
