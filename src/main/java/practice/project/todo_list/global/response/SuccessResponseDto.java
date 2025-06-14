package practice.project.todo_list.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"success", "code", "message", "result"})
public class SuccessResponseDto<T> extends BaseResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    private SuccessResponseDto(String code, String message, T result) {
        super(true, code, message);
        this.result = result;
    }

    private SuccessResponseDto(String code, String message) {
        super(true, code, message);
        this.result = null;
    }

    public static <T> ResponseEntity<SuccessResponseDto<T>> success(T result) {
        return ResponseEntity.status(200).body(new SuccessResponseDto<>("200", "Success!!", result));
    }

    public static <T> ResponseEntity<SuccessResponseDto<T>> success() {
        return ResponseEntity.status(200).body(new SuccessResponseDto<>("200", "Success!!"));
    }

    public static <T> ResponseEntity<SuccessResponseDto<T>> create() {
        return ResponseEntity.status(201).body(new SuccessResponseDto<>("201", "Create!!"));
    }

    public static <T> ResponseEntity<SuccessResponseDto<T>> create(T result) {
        return ResponseEntity.status(201).body(new SuccessResponseDto<>("201", "Create!!", result));
    }
}
