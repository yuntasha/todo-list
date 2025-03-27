package practice.project.todo_list.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "code", "message", "result"})
public class SuccessResponseDto<T> extends BaseResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    private SuccessResponseDto(T result) {
        super(true, "200", "Success!!");
        this.result = result;
    }

    private SuccessResponseDto() {
        super(true, "200", "Success!!");
        this.result = null;
    }

    public static <T> SuccessResponseDto<T> success(T result) {
        return new SuccessResponseDto<>(result);
    }

    public static <T> SuccessResponseDto<T> success() {
        return new SuccessResponseDto<>();
    }
}
