package practice.project.todo_list.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("parameterPeriod")
    @DisplayName("기간 검색 API 성공")
    void 기간_검색하기(LocalDate start, LocalDate end) {
        // given
        doReturn(List.of(TodoTitleDto.builder().build(),
                TodoTitleDto.builder().build(),
                TodoTitleDto.builder().build()))
                .when(todoDao).findByPeriod(any(PeriodDto.class));
        PeriodDto input = new PeriodDto(start, end);

        // when
        List<TodoTitleDto> todoByPeriod = todoService.getTodoByPeriod(input);

        // then
        assertEquals(3, todoByPeriod.size());
    }

    private static Stream<Arguments> parameterPeriod() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 1, 31), LocalDate.of(2024, 2, 10)),
                Arguments.of(LocalDate.of(2024, 3, 31), LocalDate.of(2024, 3, 31))
        );
    }

    @Test
    @DisplayName("기간 검색 API에서 start > end인 경우")
    void findPeriodError() {
        // given
        PeriodDto input = new PeriodDto(LocalDate.of(2024, 3, 31), LocalDate.of(2024, 2, 10));

        // when, then
        BusinessException businessException = assertThrows(BusinessException.class, () -> todoService.getTodoByPeriod(input));

        assertEquals(TodoErrorCode.WRONG_PERIOD, businessException.getErrorCode());
    }
}