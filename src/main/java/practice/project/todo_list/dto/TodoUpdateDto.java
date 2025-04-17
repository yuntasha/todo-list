package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.web.dto.PutRequestDTO;

@Getter
@Builder
@AllArgsConstructor
public class TodoUpdateDto {
    private int id;
    private String title;
    private String content;

    public static TodoUpdateDto from(int id, PutRequestDTO putRequestDTO) {
        return TodoUpdateDto.builder()
                .id(id)
                .title(putRequestDTO.getTitle())
                .content(putRequestDTO.getContent())
                .build();
    }
}
