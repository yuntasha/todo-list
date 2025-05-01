package practice.project.todo_list.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.project.todo_list.domain.Todo;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    String title;
    String content;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .build();
    }
}
