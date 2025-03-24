package practice.project.todo_list.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseResponseDto {
    final boolean success;
    final String code;
    final String message;
}
