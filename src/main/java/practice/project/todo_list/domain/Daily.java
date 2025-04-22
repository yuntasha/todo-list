package practice.project.todo_list.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class Daily {
    private String title;
    private String content;
    private LocalDate deadLine;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
