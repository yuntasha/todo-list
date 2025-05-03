package practice.project.todo_list.service;

import practice.project.todo_list.dto.*;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.util.List;

public interface TodoService {
    TodoDetailDto getTodoDetail(int id);
    List<TodoTitleDTO> getTodo();
    int postTodo(PostRequestDto postRequestDto);
    int deleteTodo(int id);
    List<TodoTitleDTO> getTodoByPeriod(PeriodDto periodDto);
    void patchState(PatchStateDTO patchStateDTO);
    List<TodoDeleteTitleDto> getDeleteTodo();
    int restoreTodo(int id);
    void updateTodo(TodoUpdateDto todoUpdateDto);
    int deleteTodoInTrash();
    TodoPageOffsetOutDTO getPageTodo(TodoPageOffsetInDTO todoPageOffsetInDTO);
}
