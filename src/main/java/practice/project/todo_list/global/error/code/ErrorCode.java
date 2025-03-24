package practice.project.todo_list.global.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
