package practice.project.todo_list;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dao.TodoDaoImpl;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JDBCConfig {
    private final DataSource dataSource;

    @Bean
    public TodoDao todoDao() {
        return new TodoDaoImpl(dataSource);
    }
}
