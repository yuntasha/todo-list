package practice.project.todo_list.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyTitleDTO;

import javax.sql.DataSource;
import java.util.List;

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
    public List<DailyTitleDTO> findAll() {
        String sql = "SELECT " +
                "id, title, deadline " +
                "FROM daily";

        return jdbcTemplate.query(sql, dailyTitleDTORowMapper);
    }

    private RowMapper<DailyTitleDTO> dailyTitleDTORowMapper = (rs, idx) -> {
        return DailyTitleDTO.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .deadline(rs.getDate("deadline").toLocalDate())
                .build();
    };
}
