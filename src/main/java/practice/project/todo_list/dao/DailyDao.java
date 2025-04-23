package practice.project.todo_list.dao;

import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyCreateDTO;
import practice.project.todo_list.dto.DailyTitleDTO;

import java.util.List;

public interface DailyDao {
    int create(Daily daily);
    List<DailyTitleDTO> findAll();
}
