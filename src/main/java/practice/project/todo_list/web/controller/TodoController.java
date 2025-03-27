package practice.project.todo_list.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.global.response.SuccessResponseDto;
import practice.project.todo_list.service.TodoService;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public SuccessResponseDto<List<TodoTitleDto>> TodoTitleDto() {
        return SuccessResponseDto.success(todoService.getTodo());
    }

    @GetMapping("/detail/{id}")
    public SuccessResponseDto<TodoDetailDto> getTodoDetail(@PathVariable("id") int id) {
        return SuccessResponseDto.success(todoService.getTodoDetail(id));
    }

    @PostMapping
    public void postTodo(@RequestBody PostRequestDto postRequestDto) {
        todoService.postTodo(postRequestDto);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto<Object> deleteTodo(@PathVariable("id") int id) {
        todoService.deleteTodo(id);
        return SuccessResponseDto.success();
    }
}
