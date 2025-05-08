package practice.project.todo_list.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.web.dto.GetPageCursorRequestDTO;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class TodoPageCursorInDTO {
    private int cursor;
    private int size;
    private Integer state;

    public boolean haveState() {
        return Objects.nonNull(state);
    }

    public static TodoPageCursorInDTO of(GetPageCursorRequestDTO dto) {
        return new TodoPageCursorInDTO(Objects.isNull(dto.getCursor()) ? 0 : dto.getCursor(), dto.getSize(), dto.getState());
    }
}
