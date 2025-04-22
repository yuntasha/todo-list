package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.project.todo_list.dao.DailyDao;
import practice.project.todo_list.dto.DailyTitleDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyServiceImpl implements DailyService {

    private final DailyDao dailyDao;

    @Override
    public List<DailyTitleDTO> getDailyList() {
        return dailyDao.findAll();
    }
}
