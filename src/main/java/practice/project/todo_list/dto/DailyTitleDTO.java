package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.domain.Daily;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class DailyTitleDTO {
    private int id;
    private String title;
    private LocalDate deadline;

    public static DailyTitleDTO from(Daily daily) {
        return DailyTitleDTO.builder()
                .id(daily.getId())
                .title(daily.getTitle())
                .deadline(daily.getDeadline())
                .build();
    }
}
