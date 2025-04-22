package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class DailyTitleDTO {
    private int id;
    private String title;
    private LocalDate deadline;
}
