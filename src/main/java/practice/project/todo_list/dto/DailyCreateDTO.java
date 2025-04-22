package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyCreateDTO {
    private String title;
    private String content;
    private LocalDate startAt;
}
