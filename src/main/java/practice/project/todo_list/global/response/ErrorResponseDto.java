package practice.project.todo_list.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import practice.project.todo_list.global.error.code.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponseDto extends BaseResponseDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public ErrorResponseDto(ErrorCode errorCode) {
        super(false, errorCode.getCode(), errorCode.getMessage());
        this.errors = new ArrayList<>();
    }

    public ErrorResponseDto(ErrorCode errorCode, List<ValidationError> errors) {
        super(false, errorCode.getCode(), errorCode.getMessage());
        this.errors = errors;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
