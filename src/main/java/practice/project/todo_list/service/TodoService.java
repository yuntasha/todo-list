package practice.project.todo_list.service;

import practice.project.todo_list.dto.PatchStateDTO;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {
    TodoDetailDto getTodoDetail(int id);
    List<TodoTitleDto> getTodo();
    int postTodo(PostRequestDto postRequestDto);
    int deleteTodo(int id);
    List<TodoTitleDto> getTodoByPeriod(PeriodDto periodDto);
    void patchState(PatchStateDTO patchStateDTO);
}
