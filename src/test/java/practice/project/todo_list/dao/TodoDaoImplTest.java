package practice.project.todo_list.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TodoDaoImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TodoDaoImplTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private TodoDaoImpl todoDao;

    private List<Todo> testData = List.of(Todo.builder()
                    .title("테스트 1")
                    .content("테스트 내용 1")
                    .state(0)
                    .createAt(LocalDateTime.of(2000, 1, 1, 1, 1))
                    .updateAt(LocalDateTime.of(2000, 1, 1, 1, 1))
                    .build(),
            Todo.builder()
                    .title("테스트 2")
                    .content("테스트 내용 2")
                    .state(1)
                    .createAt(LocalDateTime.of(2000, 1, 1, 1, 1))
                    .updateAt(LocalDateTime.of(2000, 1, 1, 1, 1))
                    .build(),
            Todo.builder()
                    .title("테스트 3")
                    .content("테스트 내용 3")
                    .state(1)
                    .createAt(LocalDateTime.of(2025, 3, 29, 1, 1))
                    .updateAt(LocalDateTime.of(2025, 3, 29, 1, 1))
                    .build(),
            Todo.builder()
                    .title("테스트 4")
                    .content("테스트 내용 4")
                    .state(2)
                    .createAt(LocalDateTime.of(2025, 3, 30, 1, 1))
                    .updateAt(LocalDateTime.of(2025, 3, 30, 1, 1))
                    .build(),
            Todo.builder()
                    .title("테스트 5")
                    .content("테스트 내용 5")
                    .state(2)
                    .createAt(LocalDateTime.of(2025, 3, 28, 1, 1))
                    .updateAt(LocalDateTime.of(2025, 3, 28, 1, 1))
                    .build());

    @BeforeAll
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

    @BeforeEach
    void insert() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO todo (title, content, state, create_at, update_at) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Todo todo : testData) {
                    pstmt.setString(1, todo.getTitle());
                    pstmt.setString(2, todo.getContent());
                    pstmt.setInt(3, todo.getState());
                    pstmt.setTimestamp(4, Timestamp.valueOf(todo.getCreateAt()));
                    pstmt.setTimestamp(5, Timestamp.valueOf(todo.getUpdateAt()));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 전체_조회() {
        // when
        List<TodoTitleDto> todos = todoDao.findAll();

        // then
        assertEquals(5, todos.size());
    }

    @RepeatedTest(3)
    void 할일_추가() {
        //given
        todoDao.postTodo(new PostRequestDto("테스트 1", "테스트 내용 1"));
        todoDao.postTodo(new PostRequestDto("테스트 2", "테스트 내용 2"));
        todoDao.postTodo(new PostRequestDto("테스트 3", "테스트 내용 3"));
        todoDao.postTodo(new PostRequestDto("테스트 4", "테스트 내용 4"));
        todoDao.postTodo(new PostRequestDto("테스트 5", "테스트 내용 5"));

        // when
        List<TodoTitleDto> todos = todoDao.findAll();

        // then
        assertEquals(10, todos.size());
    }

    @Test
    void 상태_조회() {
        // when
        List<TodoTitleDto> ready = todoDao.findByState(0);
        List<TodoTitleDto> progress = todoDao.findByState(1);
        List<TodoTitleDto> done = todoDao.findByState(2);

        // then
        assertEquals(1, ready.size());
        assertEquals(2, progress.size());
        assertEquals(2, done.size());
    }

    @Test
    void 기간을_정해서_조회() {
        // given
        LocalDate start = LocalDate.of(2025, 3, 28);
        LocalDate end = LocalDate.of(2025, 3, 30);
        PeriodDto periodDto = new PeriodDto(start, end);

        // when
        List<TodoTitleDto> todos = todoDao.findByPeriod(periodDto);

        // then
        assertEquals(3, todos.size());
    }

    @Test
    void 기간을_정해서_조회_결과가_없는_경우() {
        // given
        LocalDate start = LocalDate.of(2025, 1, 28);
        LocalDate end = LocalDate.of(2025, 1, 30);
        PeriodDto periodDto = new PeriodDto(start, end);

        // when
        List<TodoTitleDto> todos = todoDao.findByPeriod(periodDto);

        // then
        assertEquals(0, todos.size());
    }

    @AfterEach
    void delete() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DELETE FROM todo");
        }
    }

    @AfterAll
    void end() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DROP TABLE todo");
        }
    }

}