package practice.project.todo_list.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DailyErrorCode implements ErrorCode {

    DAILY_DEADLINE_PAST(HttpStatus.BAD_REQUEST, "DAILY-001", "daily의 deadline은 현재 시각보다 이전일 수 없습니다."),
    DAILY_NOT_FOUND(HttpStatus.NOT_FOUND, "DAILY-002", "해당 id를 가진 daily가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}