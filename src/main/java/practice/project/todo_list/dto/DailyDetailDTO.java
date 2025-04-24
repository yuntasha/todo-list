package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.domain.Daily;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class DailyDetailDTO {
    private int id;
    private String title;
    private String content;
    private LocalDate deadline;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static DailyDetailDTO from(Daily daily) {
        return DailyDetailDTO.builder()
                .id(daily.getId())
                .title(daily.getTitle())
                .content(Objects.isNull(daily.getContent()) ? "" : daily.getContent())
                .deadline(daily.getDeadline())
                .createAt(daily.getCreateAt())
                .updateAt(daily.getUpdateAt())
                .build();
    }
}
