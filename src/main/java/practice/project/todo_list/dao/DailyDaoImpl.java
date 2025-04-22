package practice.project.todo_list.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyCreateDTO;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.TodoTitleDto;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DailyDaoImpl implements DailyDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DailyDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int create(DailyCreateDTO dailyCreateDTO) {
        return 0;
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
