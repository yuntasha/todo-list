package practice.project.todo_list.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter()
public class GetPageOffsetRequestDTO {

    @NotNull(message = "page는 반드시 존재해야합니다.")
    @Positive(message = "1 이상이여야 합니다.")
    private int page;

    @NotNull(message = "size는 반드시 존재해야합니다.")
    @Positive(message = "1 이상이여야 합니다.")
    private int size;

    @Nullable
    private Integer state;
}
