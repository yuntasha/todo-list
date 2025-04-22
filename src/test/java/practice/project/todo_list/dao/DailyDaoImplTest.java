package practice.project.todo_list.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.DailyCreateDTO;
import practice.project.todo_list.dto.DailyTitleDTO;

import javax.sql.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DailyDaoImpl.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DailyDaoImplTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DailyDao dailyDao;

    List<Daily> testData = List.of(
            Daily.builder()
                    .title("제목 1")
                    .content("내용 1")
                    .deadLine(LocalDate.of(2020, 3, 1))
                    .createAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .updateAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build(),
            Daily.builder()
                    .title("제목 2")
                    .content("내용 2")
                    .deadLine(LocalDate.of(2020, 3, 1))
                    .createAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .updateAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build(),
            Daily.builder()
                    .title("제목 3")
                    .content("내용 3")
                    .deadLine(LocalDate.of(2020, 3, 30))
                    .createAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .updateAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build(),
            Daily.builder()
                    .title("제목 4")
                    .content("내용 4")
                    .deadLine(LocalDate.MAX)
                    .createAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .updateAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build(),
            Daily.builder()
                    .title("제목 5")
                    .content("내용 5")
                    .deadLine(LocalDate.MAX)
                    .createAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .updateAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build()
    );

    @BeforeAll
    void init() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("CREATE TABLE daily (\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  title VARCHAR(20) NOT NULL,\n" +
                    "  content VARCHAR(200),\n" +
                    "  deadline DATE NOT NULL,\n" +
                    "  create_at TIMESTAMP NOT NULL,\n" +
                    "  update_at TIMESTAMP NOT NULL\n" +
                    ");");

            String sql = "INSERT INTO daily (title, content, deadline, create_at, update_at) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Daily daily : testData) {
                    pstmt.setString(1, daily.getTitle());
                    pstmt.setString(2, daily.getContent());
                    pstmt.setDate(3, Date.valueOf(daily.getDeadLine()));
                    pstmt.setTimestamp(4, Timestamp.valueOf(daily.getCreateAt()));
                    pstmt.setTimestamp(5, Timestamp.valueOf(daily.getUpdateAt()));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        }
    }
    
    @Test
    @DisplayName("데일리 전체 조회 성공")
    void findDailySuccess() {
        // given
        // when
        List<DailyTitleDTO> dailys = dailyDao.findAll();

        // then
        assertEquals(5, dailys.size());
        assertEquals("제목 1", dailys.get(0).getTitle());
    }

    @Test
    @DisplayName("데일리 생성 성공 - ")
    void dailyCreateSuccess() {
        // given
        DailyCreateDTO dto = new DailyCreateDTO("테스트 1", "내용 1", LocalDate.MAX);

        // when
        int id = dailyDao.create(dto);

        // then
    }

    @AfterAll
    void end() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DROP TABLE daily");
        }
    }
}