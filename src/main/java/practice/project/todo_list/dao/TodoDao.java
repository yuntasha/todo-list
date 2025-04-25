package practice.project.todo_list.dao;

import practice.project.todo_list.dto.*;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoDao {
    List<TodoTitleDto> findAll();
    List<TodoTitleDto> findByState(int state);
    Optional<TodoDetailDto> findById(int id);
    int postTodo(PostRequestDto postRequestDto);
    int setDeleteStateById(int id, int state, int nowState);
    List<TodoTitleDto> findByPeriod(PeriodDto periodDto);
    int patchState(int id, int state);
    List<TodoDeleteTitleDto> findDelete();
    int updateTodo(TodoUpdateDto todoUpdateDto);
    int deleteBefore(LocalDate cutLine);
}