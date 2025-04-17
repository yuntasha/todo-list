package practice.project.todo_list.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PutRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
