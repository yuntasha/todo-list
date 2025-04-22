package practice.project.todo_list.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import practice.project.todo_list.dao.TodoDao;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    private final TodoDao todoDao;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAll() {
        todoDao.deleteByLocalDate(LocalDateTime.now());
    }
}
