package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Getter;

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
}