package practice.project.todo_list.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.global.log.LogExecutionTime;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DailyDaoImpl implements DailyDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DailyDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int create(Daily daily) {
        String sql = "INSERT INTO daily(title, content, deadline, create_at, update_at) " +
                "VALUES(:title, :content, :deadline, :createAt, :updateAt)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new BeanPropertySqlParameterSource(daily);


        jdbcTemplate.update(sql, parameter, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    @LogExecutionTime
    public List<Daily> findAll() {
        String sql = "SELECT * " +
                "FROM daily";

        return jdbcTemplate.query(sql, dailyMapper);
    }

    public Optional<Daily> findById(int id) {
        String sql = "SELECT * " +
                "FROM daily " +
                "WHERE id = :id";

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("id", id);

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, parameter, dailyMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Daily> dailyMapper = (rs, idx) -> {
        return Daily.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .deadline(rs.getDate("deadline").toLocalDate())
                .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                .updateAt(rs.getTimestamp("update_at").toLocalDateTime())
                .build();
    };

    @Override
    public int deleteById(int id) {
        String sql = "DELETE " +
                "FROM daily " +
                "WHERE id = :id";

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("id", id);

        return jdbcTemplate.update(sql, parameter);
    }

    @Override
    public int modify(Daily daily) {
        String sql = "UPDATE daily " +
                "SET title = :title, content = :content, deadline = :deadline, update_at = :updateAt " +
                "WHERE id = :id";

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("id", daily.getId());
        parameter.put("title", daily.getTitle());
        parameter.put("content", daily.getContent());
        parameter.put("deadline", daily.getDeadline());
        parameter.put("updateAt", LocalDateTime.now());

        return jdbcTemplate.update(sql, parameter);
    }
}
