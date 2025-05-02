package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.web.dto.PutDailyRequestDTO;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Builder
@Data
public class DailyModifyDTO {
    private int id;
    private String title;
    private String content;
    private LocalDate deadline;

    public static DailyModifyDTO of(int id, PutDailyRequestDTO putDailyRequestDTO) {
        return DailyModifyDTO.builder()
                .id(id)
                .title(putDailyRequestDTO.getTitle())
                .content(putDailyRequestDTO.getContent())
                .deadline(putDailyRequestDTO.getDeadline())
                .build();
    }

    public Daily toEntity() {
        return Daily.builder()
                .id(id)
                .title(title)
                .content(content)
                .deadline(convertLocalDate(deadline))
                .build();
    }

    private static LocalDate convertLocalDate(LocalDate localDate) {
        return Objects.isNull(localDate) ? LocalDate.MAX : localDate;
    }
}
