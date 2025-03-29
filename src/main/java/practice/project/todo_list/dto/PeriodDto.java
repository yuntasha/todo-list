package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PeriodDto {
    private LocalDate start;
    private LocalDate end;
}
