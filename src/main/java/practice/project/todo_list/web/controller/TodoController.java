package practice.project.todo_list.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.project.todo_list.dto.*;
import practice.project.todo_list.global.response.SuccessResponseDto;
import practice.project.todo_list.service.TodoService;
import practice.project.todo_list.web.dto.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todo")
@Validated
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public SuccessResponseDto<GetTodoListResponse> getTodoList() {
        return SuccessResponseDto.success(new GetTodoListResponse(todoService.getTodo()));
    }

    @GetMapping("/detail/{id}")
    public SuccessResponseDto<TodoDetailDto> getTodoDetail(@PathVariable("id") int id) {
        return SuccessResponseDto.success(todoService.getTodoDetail(id));
    }

    @PostMapping
    public SuccessResponseDto<Object> postTodo(@RequestBody PostRequestDto postRequestDto) {
        return SuccessResponseDto.success(new PostResponseDto(todoService.postTodo(postRequestDto)
        ));
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto<Object> deleteTodo(@PathVariable("id") @Min(1) int id) {
        return SuccessResponseDto.success(new DeleteResponseDto(todoService.deleteTodo(id)));
    }

    @GetMapping("/period")
    public SuccessResponseDto<GetTodoListResponse> getTodoListByPeriod(@ModelAttribute @Valid GetPeriodRequestDto requestDto) {
        return SuccessResponseDto.success(new GetTodoListResponse(todoService.getTodoByPeriod(new PeriodDto(requestDto.getStart(), requestDto.getEnd()))));
    }

    @PatchMapping("/{id}/state")
    public SuccessResponseDto<Object> patchStateById(@PathVariable("id") @Min(1) int id, @RequestBody @Valid PatchRequestDTO patchRequestDTO) {
        todoService.patchState(PatchStateDTO.of(id, patchRequestDTO));
        return SuccessResponseDto.success();
    }

    @GetMapping("/trash")
    public SuccessResponseDto<GetDeleteTodoListResponse> getTodoTrashList() {
        return SuccessResponseDto.success(new GetDeleteTodoListResponse(todoService.getDeleteTodo()));
    }

    @PatchMapping("/{id}/restore")
    public SuccessResponseDto<Object> patchRestoreById(@PathVariable("id") @Min(1) int id) {
        return SuccessResponseDto.success(new DeleteResponseDto(todoService.restoreTodo(id)));
    }

    @PutMapping("/{id}")
    public SuccessResponseDto<Object> putTodoById(@PathVariable("id") @Positive(message = "id는 양수입니다") int id, @RequestBody @Valid PutRequestDTO putRequestDTO) {
        todoService.updateTodo(TodoUpdateDto.from(id, putRequestDTO));
        return SuccessResponseDto.success();
    }

    @GetMapping("/page/offset")
    public SuccessResponseDto<GetPageOffsetResponseDTO> getPageOffset(@ModelAttribute @Valid GetPageOffsetRequestDTO getPageOffsetRequestDTO) {
        return SuccessResponseDto.success(GetPageOffsetResponseDTO.of(todoService.getPageOffsetTodo(TodoPageOffsetInDTO.of(getPageOffsetRequestDTO))));
    }

    @GetMapping("/page/cursor")
    public SuccessResponseDto<GetPageCursorResponseDTO> getPageCursor(@ModelAttribute @Valid GetPageCursorRequestDTO getPageCursorRequestDTO) {
        return SuccessResponseDto.success(GetPageCursorResponseDTO.of(todoService.getPageCursorTodo(TodoPageCursorInDTO.of(getPageCursorRequestDTO))));
    }
}
