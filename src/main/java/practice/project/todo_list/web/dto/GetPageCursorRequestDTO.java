package practice.project.todo_list.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetPageCursorRequestDTO {
    @Min(value = 0, message = "범위 내 숫자여야 합니다. 0 - 2")
    @Max(value = 2, message = "범위 내 숫자여야 합니다. 0 - 2")
    private Integer state;
    @Positive
    private int size;
    private Integer cursor;
}
