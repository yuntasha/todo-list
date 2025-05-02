package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.web.dto.PostDailyReqestDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class PostDailyDTO {
    private String title;
    private String content;
    private LocalDate deadline;

    public static PostDailyDTO from(PostDailyReqestDTO postDailyReqestDTO) {
        return PostDailyDTO.builder()
                .title(postDailyReqestDTO.getTitle())
                .content(postDailyReqestDTO.getContent())
                .deadline(postDailyReqestDTO.getDeadline())
                .build();
    }

    public Daily toEntity() {
        return Daily.builder()
                .title(title)
                .content(content)
                .deadline(convertLocalDate(deadline))
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    private static LocalDate convertLocalDate(LocalDate localDate) {
        return Objects.isNull(localDate) ? LocalDate.MAX : localDate;
    }
}
