package practice.project.todo_list.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.lang.Nullable;
import practice.project.todo_list.web.dto.GetPageOffsetRequestDTO;

import java.util.Objects;

@Getter
public class TodoPageOffsetInDTO {
    private Integer state;
    private int size;
    private int offset;

    private TodoPageOffsetInDTO(Integer state, int size, int offset) {
        this.state = state;
        this.size = size;
        this.offset = offset;
    }

    public TodoPageOffsetInDTO(int size, int offset) {
        this.state = null;
        this.size = size;
        this.offset = offset;
    }

    public static TodoPageOffsetInDTO of(GetPageOffsetRequestDTO dto) {
        return new TodoPageOffsetInDTO(dto.getState(), dto.getSize(), dto.getPage() - 1);
    }

    public static TodoPageOffsetInDTO of(Integer state, int size, int offset) {
        return new TodoPageOffsetInDTO(state, size, offset);
    }

    public static TodoPageOffsetInDTO of(int size, int offset) {
        return new TodoPageOffsetInDTO(size, offset);
    }

    public boolean haveState() {
        return !Objects.isNull(state);
    }

    public int getSqlOffset() {
        return size * offset;
    }
}
