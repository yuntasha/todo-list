package practice.project.todo_list.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoDeleteTitleDto {
    private int id;
    private String title;
    private int state;
    private LocalDateTime deleteAt;
}
