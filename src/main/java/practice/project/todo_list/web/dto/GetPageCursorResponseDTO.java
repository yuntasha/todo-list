package practice.project.todo_list.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.project.todo_list.dto.TodoPageCursorOutDTO;
import practice.project.todo_list.dto.TodoTitleDTO;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetPageCursorResponseDTO {
    private List<TodoTitleDTO> todoList;
    private int size;
    private int nextCursor;
    private boolean hasNext;

    public static GetPageCursorResponseDTO of(TodoPageCursorOutDTO dto) {
        return new GetPageCursorResponseDTO(dto.getTodoList(), dto.getSize(), dto.getNextCursor(), dto.isHasNext());
    }
}
