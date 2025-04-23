package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.web.dto.PostDailyReqestDTO;

import java.time.LocalDate;

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
}
