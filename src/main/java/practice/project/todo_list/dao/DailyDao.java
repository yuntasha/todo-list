package practice.project.todo_list.dao;

import practice.project.todo_list.domain.Daily;

import java.util.List;
import java.util.Optional;

public interface DailyDao {
    int create(Daily daily);
    List<Daily> findAll();
    Optional<Daily> findById(int id);
    int deleteById(int id);
    int modify(Daily daily);
}
