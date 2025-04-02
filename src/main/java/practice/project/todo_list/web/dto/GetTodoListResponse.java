package practice.project.todo_list.web.dto;

import lombok.Getter;
import practice.project.todo_list.dto.TodoTitleDto;

import java.util.List;

public record GetTodoListResponse(List<TodoTitleDto> result) {
}
