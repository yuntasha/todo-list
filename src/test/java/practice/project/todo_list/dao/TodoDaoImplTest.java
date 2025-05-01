package practice.project.todo_list.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.*;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        List<Todo> todos = todoDao.findAll();

        // then
        assertEquals(5, todos.size());
    }

    @Test
    void 할일_추가() {
        //given
        todoDao.postTodo(Todo.builder()
                .title("테스트 2")
                .content("테스트 내용 2")
                .state(0)
                .deleteState(0)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build());

        // when
        List<Todo> todos = todoDao.findAll();

        // then
        assertEquals(6, todos.size());
    }

    @Test
    void 상태_조회() {
        // when
        List<Todo> ready = todoDao.findByState(0);
        List<Todo> progress = todoDao.findByState(1);
        List<Todo> done = todoDao.findByState(2);

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

        // when
        List<Todo> todos = todoDao.findByPeriod(start, end);

        // then
        assertEquals(3, todos.size());
    }

    @Test
    void 기간을_정해서_조회_결과가_없는_경우() {
        // given
        LocalDate start = LocalDate.of(2025, 1, 28);
        LocalDate end = LocalDate.of(2025, 1, 30);

        // when
        List<Todo> todos = todoDao.findByPeriod(start, end);

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
        todoDao.setDeleteStateById(id, 1, 0);

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
        Todo todoDto = assertDoesNotThrow(() -> todoDao.findById(id).orElseThrow());

        // then
        assertEquals(1, count);
        assertEquals(state, todoDto.getState());
    }

    private static Stream<Arguments> parameterPatchState() {
        return Stream.of(
                Arguments.of(1, 2), // 성공적
                Arguments.of(1, 0)  // 기존 state와 같은 경우
        );
    }

    @Test
    @DisplayName("삭제된 Todo 조회 성공 - 비어있는 경우")
    void getDeleteTodoListEmpty() {
        // given
        // when
        List<Todo> delete = todoDao.findDelete();

        // then
        assertTrue(delete.isEmpty());
    }

    @Test
    @DisplayName("삭제된 Todo 조회 성공 - 존재하는 경우")
    void getDeleteTodoExist() {
        // given
        int[] id = {1, 3, 4};
        for (int i : id) {
            todoDao.setDeleteStateById(i, 1, 0);
        }

        // when
        List<Todo> delete = todoDao.findDelete();

        // then
        assertEquals(3, delete.size());
        assertEquals(1, delete.get(0).getId());
        assertEquals(3, delete.get(1).getId());
        assertEquals(4, delete.get(2).getId());
    }

    @Test
    @DisplayName("Todo 수정 성공 - 존재하는 경우")
    void updateTodoSuccess() {
        // given
        int id = 1;
        String title = "수정 제목 1";
        String content = "수정 내용 1";
        Todo todoInput = Todo.builder()
                .id(id)
                .title(title)
                .content(content)
                .updateAt(LocalDateTime.now())
                .build();

        // when
        int count = todoDao.updateTodo(todoInput);
        Optional<Todo> opTodo = todoDao.findById(id);

        // then
        assertEquals(1, count);
        Todo todo = assertDoesNotThrow(() -> opTodo.orElseThrow(Exception::new));
        assertEquals(title, todo.getTitle());
        assertEquals(content, todo.getContent());
        assertEquals(LocalDate.now(), todo.getUpdateAt().toLocalDate());
    }

    @Test
    @DisplayName("Todo 수정 실패 - 존재하지 않는 경우")
    void updateTodoFailure() {
        // given
        int id = 20;
        String title = "수정 제목 1";
        String content = "수정 내용 1";
        Todo todoInput = Todo.builder()
                .id(id)
                .title(title)
                .content(content)
                .updateAt(LocalDateTime.now())
                .build();

        // when
        int count = todoDao.updateTodo(todoInput);

        // then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("특정 시점 이후 지워진 데이터 삭제")
    void deleteTodo() {
        // given
        int id = 1;
        int normalState = 0;
        int deleteState = 1;
        LocalDate now = LocalDate.now();

        // when
        todoDao.setDeleteStateById(id, deleteState, normalState);
        int deleteCount = todoDao.deleteBefore(now.plusMonths(1L));
        List<Todo> list = todoDao.findAll();
        int isRestore = todoDao.setDeleteStateById(id, normalState, deleteState);

        // then
        assertEquals(0, isRestore);
        assertEquals(1, deleteCount);
        assertEquals(4, list.size());
    }

    @AfterAll
    void end() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DROP TABLE todo");
        }
    }

}