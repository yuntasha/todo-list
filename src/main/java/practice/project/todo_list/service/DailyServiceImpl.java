package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.project.todo_list.dao.DailyDao;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.PostDailyDTO;
import practice.project.todo_list.global.error.code.DailyErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyServiceImpl implements DailyService {

    private final DailyDao dailyDao;

    @Override
    public List<DailyTitleDTO> getDailyList() {
        return dailyDao.findAll();
    }

    @Override
    public void postDaily(PostDailyDTO postDailyDTO) {
        Daily daily = Daily.from(postDailyDTO);

        if (daily.isBefore()) {
            throw new BusinessException(DailyErrorCode.DAILY_DEADLINE_PAST);
        }

        dailyDao.create(daily);
    }

    @Override
    public DailyDetailDTO getDailyDetail(int id) {
        return DailyDetailDTO.from(dailyDao.findById(id).orElseThrow(() -> new BusinessException(DailyErrorCode.DAILY_NOT_FOUND)));
    }
}
