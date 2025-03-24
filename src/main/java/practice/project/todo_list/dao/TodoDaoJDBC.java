package practice.project.todo_list.dao;

import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoDaoJDBC implements TodoDao {

    @Override
    public List<TodoTitleDto> findAll() {
        String sql = "SELECT " +
                "id, title, state, create_at " +
                "FROM " +
                "todo " +
                "WHERE " +
                "delete_state = 0";
        Connection conn = null;
        Statement stmt = null;;
        ResultSet rs = null;

        List<TodoTitleDto> result = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/todo");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(TodoTitleDto.builder()
                        .id(rs.getInt("id"))
                        .title(rs.getString("title"))
                        .state(rs.getInt("state"))
                        .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                        .build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                stmt.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @Override
    public List<TodoTitleDto> findByState(int state) {
        return null;
    }

    @Override
    public Optional<TodoDetailDto> findById(int id) {
        return Optional.empty();
    }

    @Override
    public int postTodo(PostRequestDto postRequestDto) {
        return 0;
    }

    @Override
    public int setDeleteStateByid(int id) {
        return 0;
    }

    @Override
    public void deleteByLocalDate(LocalDateTime now) {
        return;
    }
}
