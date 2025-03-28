package practice.project.todo_list.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TodoDaoImpl.class)
class TodoDaoImplTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private TodoDaoImpl todoDao;

    @BeforeEach
    void init() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("CREATE TABLE todo (\n" +
                    "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "  title VARCHAR(20) NOT NULL,\n" +
                    "  content VARCHAR(200),\n" +
                    "  state INT NOT NULL DEFAULT 0,\n" +
                    "  create_at TIMESTAMP NOT NULL,\n" +
                    "  update_at TIMESTAMP NOT NULL,\n" +
                    "  delete_state INT NOT NULL DEFAULT 0\n" +
                    ");");
        }


    }

    @Test
    void 전체_조회() {
        //given
        todoDao.postTodo(new PostRequestDto("테스트 1", "테스트 내용 1"));
        todoDao.postTodo(new PostRequestDto("테스트 2", "테스트 내용 2"));
        todoDao.postTodo(new PostRequestDto("테스트 3", "테스트 내용 3"));
        todoDao.postTodo(new PostRequestDto("테스트 4", "테스트 내용 4"));
        todoDao.postTodo(new PostRequestDto("테스트 5", "테스트 내용 5"));

        // when
        List<TodoTitleDto> todos = todoDao.findAll();

        // then
        assertEquals(5, todos.size());
    }
}