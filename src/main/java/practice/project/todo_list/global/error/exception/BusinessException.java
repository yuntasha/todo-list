package practice.project.todo_list.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import practice.project.todo_list.global.error.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
}
