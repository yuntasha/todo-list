package practice.project.todo_list.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoDeleteTitleDto;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TodoDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<TodoTitleDto> findAll() {

        String sql = "SELECT " +
                "id, title, state, update_at " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 0";

        return jdbcTemplate.query(sql, todoTitleMapper);
    }

    @Override
    public List<TodoTitleDto> findByState(int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);

        String sql = "SELECT " +
                "id, title, state, update_at " +
                "FROM " +
                "todo " +
                "WHERE " +
                "state = :state";


        return jdbcTemplate.query(sql, map, todoTitleMapper);
    }

    private RowMapper<TodoTitleDto> todoTitleMapper = (rs, idx) -> {
        return TodoTitleDto.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .state(rs.getInt("state"))
                .updateAt(rs.getTimestamp("update_at").toLocalDateTime())
                .build();
    };

    @Override
    public Optional<TodoDetailDto> findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        String sql = "SELECT " +
                "id, title, content, state, create_at, update_at " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 0 " +
                "AND " +
                "id = :id";


        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, map, todoDetailMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int postTodo(PostRequestDto postRequestDto) {
        String sql = "INSERT INTO todo(title, content, create_at, update_at)" +
                "VALUES(:title, :content, :createAt, :updateAt)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(Todo.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, param, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public int setDeleteStateByid(int id) {
        String sql = "UPDATE todo SET update_at = :updateAt, delete_state = 1 " +
                "WHERE id = :id";
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("updateAt", LocalDateTime.now());
        
        return jdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteByLocalDate(LocalDateTime now) {
        String sql = "DELETE todo " +
                "WHERE DATE(update_at) < :deadLine";


        Map<String, Object> map = new HashMap<>();
        map.put("deadLine", now.minusMonths(1L));

        jdbcTemplate.update(sql, map);
    }

    private RowMapper<TodoDetailDto> todoDetailMapper = (rs, idx) -> {
        return TodoDetailDto.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .state(rs.getInt("state"))
                .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                .updateAt(rs.getTimestamp("update_at").toLocalDateTime())
                .build();
    };

    @Override
    public List<TodoTitleDto> findByPeriod(PeriodDto periodDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("start", LocalDateTime.of(periodDto.getStart(), LocalTime.MIN));
        map.put("end", LocalDateTime.of(periodDto.getEnd(), LocalTime.MAX));

        String sql = "SELECT " +
                "id, title, state, update_at " +
                "FROM " +
                "todo " +
                "WHERE update_at " +
                "BETWEEN :start AND :end";


        return jdbcTemplate.query(sql, map, todoTitleMapper);
    }

    @Override
    public int patchState(int id, int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("state", state);
        map.put("update_at", LocalDateTime.now());

        String sql = "UPDATE " +
                "todo " +
                "SET state = :state, update_at = :update_at " +
                "WHERE id = :id AND delete_state = 0";

        return jdbcTemplate.update(sql, map);
    }

    @Override
    public List<TodoDeleteTitleDto> findDelete() {
        String sql = "SELECT " +
                "id, title, state, update_at " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 1";

        return jdbcTemplate.query(sql, todoDeleteTitleMapper);
    }

    private RowMapper<TodoDeleteTitleDto> todoDeleteTitleMapper = (rs, idx) -> {
        return TodoDeleteTitleDto.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .state(rs.getInt("state"))
                .deleteAt(rs.getTimestamp("update_at").toLocalDateTime())
                .build();
    };
}
