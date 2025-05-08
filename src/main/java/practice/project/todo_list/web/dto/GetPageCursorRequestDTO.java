package practice.project.todo_list.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetPageCursorRequestDTO {
    private Integer state;
    @Positive
    private int size;
    private Integer cursor;
}
