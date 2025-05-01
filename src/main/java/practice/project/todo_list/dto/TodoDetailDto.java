package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.domain.Todo;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoDetailDto {
    private int id;
    private String title;
    private String content;
    private int state;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static TodoDetailDto from(Todo todo) {
        return TodoDetailDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .content(todo.getContent())
                .state(todo.getState())
                .createAt(todo.getCreateAt())
                .updateAt(todo.getUpdateAt())
                .build();
    }
}