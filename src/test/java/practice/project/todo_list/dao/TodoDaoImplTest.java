package practice.project.todo_list.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TodoDaoImpl.class)
@Transactional
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

    @Test
    @DisplayName("상태 변경 - 해당 todo가 존재하지 않는 경우")
    void patchStateTodoNoExistError() {
        // given
        int id = 6;
        int state = 2;

        // when
        int count = todoDao.patchState(id, state);

        // then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("상태 변경 - 해당 todo가 삭제된 경우")
    void patchStateTodoDeleted() {
        // given
        int id = 1;
        int state = 2;
        todoDao.setDeleteStateByid(id);

        // when
        int count = todoDao.patchState(id, state);

        // then
        assertEquals(0, count);
    }

    @ParameterizedTest
    @MethodSource("parameterPatchState")
    @DisplayName("상태 변경 - 성공")
    void patchStateSuccess(int id, int state) {
        // given

        // when
        int count = todoDao.patchState(id, state);
        TodoDetailDto todoDetailDto = assertDoesNotThrow(() -> todoDao.findById(id).orElseThrow());

        // then
        assertEquals(1, count);
        assertEquals(state, todoDetailDto.getState());
    }

    private static Stream<Arguments> parameterPatchState() {
        return Stream.of(
                Arguments.of(1, 2), // 성공적
                Arguments.of(1, 0)  // 기존 state와 같은 경우
        );
    }

    @AfterAll
    void end() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DROP TABLE todo");
        }
    }

}