package practice.project.todo_list.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-001", "Internal server error"),
    NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "COMMON-002", "Valid error"),
    NOT_FOUND_URL(HttpStatus.NOT_FOUND, "COMMON-003", "Not exist URL"),
    NOT_EXIST_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-004", "Not exist method"),
    NOT_SUPPORT_MEDIATYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON-005", "In header Content-Type is wrong"),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-006", "Missing request parameter"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
