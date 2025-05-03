package practice.project.todo_list.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TodoPageOffsetOutDTO {
    private List<TodoTitleDTO> todoList;
    private int size;
    private int page;
    private int lastPage;

    private TodoPageOffsetOutDTO(List<TodoTitleDTO> todoList, int size, int page, int lastPage) {
        this.todoList = todoList;
        this.size = size;
        this.page = page;
        this.lastPage = lastPage;
    }

    public static TodoPageOffsetOutDTO of(List<TodoTitleDTO> todoList, TodoPageOffsetInDTO dto, int todoCount) {
        return new TodoPageOffsetOutDTO(todoList, dto.getSize(), dto.getOffset() + 1,  getLastPage(todoCount, dto.getSize()));
    }

    private static int getLastPage(int totalCount, int size) {
        return totalCount / size + (totalCount % size == 0 ? 0 : 1);
    }
}
