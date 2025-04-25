package practice.project.todo_list.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.service.TodoService;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    private final TodoService todoService;

    @Scheduled(cron = "0 0 6 * * *")
    public void deleteAll() {
        todoService.deleteTodoInTrash();
    }
}
