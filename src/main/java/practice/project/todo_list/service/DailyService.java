package practice.project.todo_list.service;

import practice.project.todo_list.dto.DailyTitleDTO;

import java.util.List;

public interface DailyService {
    List<DailyTitleDTO> getDailyList();
}
