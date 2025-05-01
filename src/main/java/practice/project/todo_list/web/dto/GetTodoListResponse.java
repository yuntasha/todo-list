package practice.project.todo_list.web.dto;

import practice.project.todo_list.dto.TodoTitleDTO;

import java.util.List;

public record GetTodoListResponse(List<TodoTitleDTO> result) {
}
