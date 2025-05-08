package practice.project.todo_list.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TodoPageCursorOutDTO {
    List<TodoTitleDTO> todoList;
    private int nextCursor;
    private int size;
    private boolean hasNext;

    public static TodoPageCursorOutDTO of(List<TodoTitleDTO> todoList, TodoPageCursorInDTO todoPageCursorInDTO) {
        List<TodoTitleDTO> list = todoList.stream()
                .limit(todoPageCursorInDTO.getSize())
                .toList();

        return new TodoPageCursorOutDTO(list, getNextCursor(todoList, todoPageCursorInDTO), list.size(), list.size() < todoList.size());
    }

    private static int getNextCursor(List<TodoTitleDTO> todoList, TodoPageCursorInDTO todoPageCursorInDTO) {
        if (todoList.isEmpty()) {
            return -1;
        }
        return todoList.get(Math.min(todoList.size() - 1, todoPageCursorInDTO.getSize())).getId();
    }
}
