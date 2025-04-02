package practice.project.todo_list.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO-001", "해당 TODO는 존재하지 않습니다."),
    WRONG_PERIOD(HttpStatus.BAD_REQUEST, "TODO-002", "기간 검색은 start가 end보다 빠르거나 같아야합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
