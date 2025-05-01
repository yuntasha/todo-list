package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.domain.Todo;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoTitleDTO {
    private int id;
    private String title;
    private int state;
    private LocalDateTime updateAt;

    public static TodoTitleDTO from(Todo todo) {
        return TodoTitleDTO.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .state(todo.getState())
                .updateAt(todo.getUpdateAt())
                .build();
    }
}
