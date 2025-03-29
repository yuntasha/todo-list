package practice.project.todo_list.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoTitleDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoDao todoDao;

    @Test
    void getTodoDetail() {
    }

    @Test
    void getTodo() {
    }

    @Test
    void postTodo() {
    }

    @Test
    void deleteTodo() {
    }

    @Test
    void 기간_검색하기() {
        // given
        doReturn(List.of(TodoTitleDto.builder().build(),
                TodoTitleDto.builder().build(),
                TodoTitleDto.builder().build()))
                .when(todoDao).findByPeriod(any(PeriodDto.class));
        PeriodDto input = new PeriodDto(LocalDate.of(2023, 3, 31), LocalDate.of(2024, 4, 30));

        // when
        List<TodoTitleDto> todoByPeriod = todoService.getTodoByPeriod(input);

        // then
        assertEquals(3, todoByPeriod.size());
    }
}