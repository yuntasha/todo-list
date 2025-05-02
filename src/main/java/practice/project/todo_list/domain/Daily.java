package practice.project.todo_list.domain;

import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.dto.DailyModifyDTO;
import practice.project.todo_list.dto.PostDailyDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Daily {
    private int id;
    private String title;
    private String content;
    private LocalDate deadline;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public boolean isBefore() {
        return deadline.isBefore(LocalDate.now());
    }
}
