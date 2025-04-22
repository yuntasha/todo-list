package practice.project.todo_list.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.project.todo_list.dto.DailyTitleDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetDailyListResponseDTO {
    private List<DailyTitleDTO> result;
}
