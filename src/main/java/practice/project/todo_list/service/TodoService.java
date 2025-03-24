package practice.project.todo_list.service;

import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.util.List;

public interface TodoService {
    TodoDetailDto getTodoDetail(int id);
    List<TodoTitleDto> getTodo();
    int postTodo(PostRequestDto postRequestDto);
    int deleteTodo(int id);
}
