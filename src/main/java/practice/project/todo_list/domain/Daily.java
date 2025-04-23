package practice.project.todo_list.domain;

import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.dto.PostDailyDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Daily {
    private String title;
    private String content;
    private LocalDate deadline;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static Daily from(PostDailyDTO postDailyDTO) {
        return Daily.builder()
                .title(postDailyDTO.getTitle())
                .content(postDailyDTO.getContent())
                .deadline(convertLocalDate(postDailyDTO.getDeadline()))
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    private static LocalDate convertLocalDate(LocalDate localDate) {
        return Objects.isNull(localDate) ? LocalDate.MAX : localDate;
    }

    public boolean isBefore() {
        return deadline.isBefore(LocalDate.now());
    }
}
