package practice.project.todo_list.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.project.todo_list.dto.TodoPageOffsetOutDTO;
import practice.project.todo_list.dto.TodoTitleDTO;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetPageOffsetResponseDTO {
    private List<TodoTitleDTO> todoList;
    private int size;
    private int page;
    private int lastPage;

    public static GetPageOffsetResponseDTO of(TodoPageOffsetOutDTO dto) {
        return new GetPageOffsetResponseDTO(dto.getTodoList(), dto.getSize(), dto.getPage(), dto.getLastPage());
    }
}
