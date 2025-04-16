package practice.project.todo_list.web.dto;

import practice.project.todo_list.dto.TodoDeleteTitleDto;

import java.util.List;

public record GetDeleteTodoListResponse(List<TodoDeleteTitleDto> result) {
}
