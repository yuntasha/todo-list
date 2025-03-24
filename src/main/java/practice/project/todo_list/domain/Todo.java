package practice.project.todo_list.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Todo {
    private int id;
    private String title;
    private String content;
    private int state;
    private int deleteState;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
