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
import practice.project.todo_list.dto.*;
import practice.project.todo_list.web.dto.PostRequestDto;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TodoDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Todo> findAll() {

        String sql = "SELECT * " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 0";

        return jdbcTemplate.query(sql, todoMapper);
    }

    @Override
    public List<Todo> findByState(int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);

        String sql = "SELECT * " +
                "FROM " +
                "todo " +
                "WHERE " +
                "state = :state " +
                "AND " +
                "delete_state = 0";


        return jdbcTemplate.query(sql, map, todoMapper);
    }

    private RowMapper<Todo> todoMapper = (rs, idx) -> {
        return Todo.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .state(rs.getInt("state"))
                .deleteState(rs.getInt("delete_state"))
                .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                .updateAt(rs.getTimestamp("update_at").toLocalDateTime())
                .build();
    };

    @Override
    public Optional<Todo> findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        String sql = "SELECT * " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 0 " +
                "AND " +
                "id = :id";


        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, map, todoMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int postTodo(Todo todo) {
        String sql = "INSERT INTO todo(title, content, create_at, update_at) " +
                "VALUES(:title, :content, :createAt, :updateAt)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(todo);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, param, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public int setDeleteStateById(int id, int state, int nowState) {
        String sql = "UPDATE todo SET update_at = :updateAt, delete_state = :state " +
                "WHERE id = :id AND delete_state = :nowState";
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("updateAt", LocalDateTime.now());
        map.put("state", state);
        map.put("nowState", nowState);

        return jdbcTemplate.update(sql, map);
    }

    @Override
    public List<Todo> findByPeriod(LocalDate start, LocalDate end) {
        Map<String, Object> map = new HashMap<>();
        map.put("start", LocalDateTime.of(start, LocalTime.MIN));
        map.put("end", LocalDateTime.of(end, LocalTime.MAX));

        String sql = "SELECT * " +
                "FROM " +
                "todo " +
                "WHERE update_at " +
                "BETWEEN :start AND :end " +
                "AND delete_state = 0";


        return jdbcTemplate.query(sql, map, todoMapper);
    }

    @Override
    public int patchState(int id, int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("state", state);
        map.put("update_at", LocalDateTime.now());

        String sql = "UPDATE todo " +
                "SET state = :state, update_at = :update_at " +
                "WHERE id = :id AND delete_state = 0";

        return jdbcTemplate.update(sql, map);
    }

    @Override
    public List<Todo> findDelete() {
        String sql = "SELECT * " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 1";

        return jdbcTemplate.query(sql, todoMapper);
    }

    @Override
    public int updateTodo(Todo todo) {
        String sql = "UPDATE todo " +
                "SET title = :title, content = :content, update_at = :update_at " +
                "WHERE id = :id AND delete_state = 0";

        Map<String, Object> map = new HashMap<>();
        map.put("id", todo.getId());
        map.put("title", todo.getTitle());
        map.put("content", todo.getContent());
        map.put("update_at", todo.getUpdateAt());

        return jdbcTemplate.update(sql, map);
    }

    @Override
    public int deleteBefore(LocalDate cutLine) {
        String sql = "DELETE FROM todo " +
                "WHERE update_at <= :cutLine AND delete_state = 1";

        Map<String, Object> map = new HashMap<>();
        map.put("cutLine", cutLine.atStartOfDay());

        return jdbcTemplate.update(sql, map);
    }

    @Override
    public List<Todo> offsetPaging(int size, int offset) {
        String sql = "SELECT * " +
                "FROM todo " +
                "WHERE delete_state = 0 " +
                "LIMIT :size OFFSET :offset";

        Map<String, Object> map = new HashMap<>();
        map.put("size", size);
        map.put("offset", offset);

        return jdbcTemplate.query(sql, map, todoMapper);
    }

    @Override
    public int countTodo() {
        String sql = "SELECT COUNT(*) " +
                "FROM todo " +
                "WHERE delete_state = 0";

        return jdbcTemplate.queryForObject(sql, Collections.emptyMap(), Integer.class);
    }

    @Override
    public List<Todo> offsetPagingByState(int state, int size, int offset) {
        String sql = "SELECT * " +
                "FROM todo " +
                "WHERE delete_state = 0 " +
                "AND state = :state " +
                "LIMIT :size OFFSET :offset";

        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("size", size);
        map.put("offset", offset);

        return jdbcTemplate.query(sql, map, todoMapper);
    }

    @Override
    public int countTodoByState(int state) {
        String sql = "SELECT COUNT(*) " +
                "FROM todo " +
                "WHERE state = :state " +
                "AND delete_state = 0";

        Map<String, Object> map = new HashMap<>();
        map.put("state", state);

        return jdbcTemplate.queryForObject(sql, map, Integer.class);
    }
}
