package practice.project.todo_list.service;

import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.PostDailyDTO;

import java.util.List;

public interface DailyService {
    List<DailyTitleDTO> getDailyList();
    void postDaily(PostDailyDTO postDailyDTO);
    DailyDetailDTO getDailyDetail(int id);
}
