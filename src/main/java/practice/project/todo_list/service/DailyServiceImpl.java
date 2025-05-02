package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.project.todo_list.dao.DailyDao;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.DailyModifyDTO;
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
        return dailyDao.findAll().stream()
                .map(DailyTitleDTO::from)
                .toList();
    }

    @Override
    public void postDaily(PostDailyDTO postDailyDTO) {
        Daily daily = postDailyDTO.toEntity();

        if (daily.isBefore()) {
            throw new BusinessException(DailyErrorCode.DAILY_DEADLINE_PAST);
        }

        dailyDao.create(daily);
    }

    @Override
    public DailyDetailDTO getDailyDetail(int id) {
        return DailyDetailDTO.from(dailyDao.findById(id).orElseThrow(() -> new BusinessException(DailyErrorCode.DAILY_NOT_FOUND)));
    }

    @Override
    public void deleteDaily(int id) {
        int count = dailyDao.deleteById(id);

        if (count == 0) {
            throw new BusinessException(DailyErrorCode.DAILY_NOT_FOUND);
        }
    }

    @Override
    public void update(DailyModifyDTO dailyModifyDTO) {
        int count = dailyDao.modify(dailyModifyDTO.toEntity());
        if (count == 0) {
            throw new BusinessException(DailyErrorCode.DAILY_NOT_FOUND);
        }
    }
}
