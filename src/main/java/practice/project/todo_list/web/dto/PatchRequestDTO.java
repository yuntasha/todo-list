package practice.project.todo_list.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatchRequestDTO {
    @Min(value = 0, message = "범위 내 숫자여야 합니다. 0 - 2")
    @Max(value = 2, message = "범위 내 숫자여야 합니다. 0 - 2")
    @NotNull(message = "state를 반드시 작성해야합니다.")
    Integer state;
}
