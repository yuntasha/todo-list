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
import practice.project.todo_list.domain.Todo;
import practice.project.todo_list.dto.*;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;

import java.time.LocalDate;
import java.util.Collections;
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

    @ParameterizedTest
    @MethodSource("parameterPeriod")
    @DisplayName("기간 검색 API 성공")
    void 기간_검색하기(LocalDate start, LocalDate end) {
        // given
        doReturn(List.of(Todo.builder().build(),
                Todo.builder().build(),
                Todo.builder().build()))
                .when(todoDao).findByPeriod(any(LocalDate.class), any(LocalDate.class));
        PeriodDto input = new PeriodDto(start, end);

        // when
        List<TodoTitleDTO> todoByPeriod = todoService.getTodoByPeriod(input);

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

    @Test
    @DisplayName("상태 변환 - 존재하지 않는 id")
    void patchStateNotFoundId() {
        // given
        int id = 2;
        int state = 1;
        PatchStateDTO dto = new PatchStateDTO(id, state);
        doReturn(0).when(todoDao)
                .patchState(anyInt(), anyInt());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> todoService.patchState(dto));

        // then
        assertEquals(TodoErrorCode.TODO_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("상태 변환 - 성공")
    void patchStateSuccess() {
        // given
        int id = 2;
        int state = 1;
        PatchStateDTO dto = new PatchStateDTO(id, state);
        doReturn(1).when(todoDao)
                .patchState(anyInt(), anyInt());

        // when
        // then
        assertDoesNotThrow(() -> todoService.patchState(dto));
    }

    @Test
    @DisplayName("Todo 복원 - 성공")
    void restoreTodoSuccess() {
        // given
        int id = 2;
        doReturn(1).when(todoDao)
                .setDeleteStateById(anyInt(), anyInt(), anyInt());

        // when

        // then
        int n = assertDoesNotThrow(() -> todoService.restoreTodo(id));
        assertEquals(id, n);
    }

    @Test
    @DisplayName("Todo 복원 - 실패")
    void restoreTodoFailure() {
        // given
        int id = 2;
        doReturn(0).when(todoDao)
                .setDeleteStateById(anyInt(), anyInt(), anyInt());

        // when

        // then
        BusinessException e = assertThrows(BusinessException.class, () -> todoService.restoreTodo(id));
        assertEquals(TodoErrorCode.TODO_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("Todo 수정 성공")
    void updateTodoSuccess() {
        // given
        int id = 1;
        String title = "수정 제목 1";
        String content = "수정 내용 1";
        TodoUpdateDto dto = new TodoUpdateDto(id, title, content);
        doReturn(1)
                .when(todoDao)
                        .updateTodo(any());

        // when
        // then
        assertDoesNotThrow(() -> todoService.updateTodo(dto));
    }

    @Test
    @DisplayName("Todo 수정 실패 - 존재하지 않는 경우")
    void updateTodoFailure() {
        // given
        int id = 20;
        String title = "수정 제목 1";
        String content = "수정 내용 1";
        TodoUpdateDto dto = new TodoUpdateDto(id, title, content);
        doReturn(0)
                .when(todoDao)
                .updateTodo(any());

        // when
        BusinessException ex = assertThrows(BusinessException.class, () -> todoService.updateTodo(dto));

        // then
        assertEquals(TodoErrorCode.TODO_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("오프셋 페이징 성공 - 상태 없음")
    void offsetPaging() {
        // given
        Integer state = null;
        int size = 5;
        int offset = 1;
        TodoPageOffsetInDTO dto = TodoPageOffsetInDTO.of(size, offset);
        doReturn(10)
                .when(todoDao)
                .countTodo();
        doReturn(Collections.EMPTY_LIST)
                .when(todoDao)
                .offsetPaging(size, offset * size);

        // when
        TodoPageOffsetOutDTO result = todoService.getPageOffsetTodo(dto);

        // then
        assertEquals(2, result.getLastPage());
        assertEquals(2, result.getPage());
        assertEquals(5, result.getSize());
        assertTrue(result.getTodoList().isEmpty());
    }

    @Test
    @DisplayName("오프셋 페이징 성공 - 상태 있음")
    void offsetPagingExistState() {
        // given
        Integer state = 0;
        int size = 5;
        int offset = 1;
        TodoPageOffsetInDTO dto = TodoPageOffsetInDTO.of(state, size, offset);
        doReturn(10)
                .when(todoDao)
                .countTodoByState(state);
        doReturn(Collections.EMPTY_LIST)
                .when(todoDao)
                .offsetPagingByState(state, size, offset * size);

        // when
        TodoPageOffsetOutDTO result = todoService.getPageOffsetTodo(dto);

        // then
        assertEquals(2, result.getLastPage());
        assertEquals(2, result.getPage());
        assertEquals(5, result.getSize());
        assertTrue(result.getTodoList().isEmpty());
    }

    @Test
    @DisplayName("커서 페이징 성공 - 상태 없음")
    void cursorPaging() {
        // given
        Integer state = null;
        int size = 5;
        int cursor = 1;
        int nextId = 6;
        TodoPageCursorInDTO dto = TodoPageCursorInDTO.builder()
                                        .cursor(cursor)
                                        .size(size)
                                        .state(state)
                                        .build();

        doReturn(List.of(Todo.builder().build(), Todo.builder().build(), Todo.builder().build(), Todo.builder().build(), Todo.builder().id(6).build(), Todo.builder().build()))
                .when(todoDao)
                .cursorPaging(size + 1, cursor);

        // when
        TodoPageCursorOutDTO result = todoService.getPageCursorTodo(dto);

        // then
        assertEquals(size, result.getSize());
        assertEquals(nextId, result.getNextCursor());
        assertEquals(5, result.getTodoList().size());
        assertTrue(result.isHasNext());
    }

    @Test
    @DisplayName("커서 페이징 성공 - 존재하지 않음")
    void cursorPagingSuccessEmpty() {
        // given
        Integer state = null;
        int size = 5;
        int cursor = 1;
        TodoPageCursorInDTO dto = TodoPageCursorInDTO.builder()
                .cursor(cursor)
                .size(size)
                .state(state)
                .build();

        doReturn(Collections.EMPTY_LIST)
                .when(todoDao)
                .cursorPaging(size + 1, cursor);

        // when
        TodoPageCursorOutDTO result = todoService.getPageCursorTodo(dto);

        // then
        assertEquals(0, result.getSize());
        assertEquals(-1, result.getNextCursor());
        assertEquals(0, result.getTodoList().size());
        assertFalse(result.isHasNext());
    }

    @Test
    @DisplayName("상태 조건 커서 페이징 성공 - 상태 없음")
    void cursorPagingWithStateSuccess() {
        // given
        Integer state = 0;
        int size = 5;
        int cursor = 1;
        int nextId = 6;
        TodoPageCursorInDTO dto = TodoPageCursorInDTO.builder()
                .cursor(cursor)
                .size(size)
                .state(state)
                .build();

        doReturn(List.of(Todo.builder().build(), Todo.builder().build(), Todo.builder().build(), Todo.builder().build(), Todo.builder().id(6).build(), Todo.builder().build()))
                .when(todoDao)
                .cursorPagingByState(state, size + 1, cursor);

        // when
        TodoPageCursorOutDTO result = todoService.getPageCursorTodo(dto);

        // then
        assertEquals(size, result.getSize());
        assertEquals(nextId, result.getNextCursor());
        assertEquals(5, result.getTodoList().size());
        assertTrue(result.isHasNext());
    }

    @Test
    @DisplayName("상태 조건 커서 페이징 성공 - 존재하지 않음")
    void cursorPagingByStateSuccessEmpty() {
        // given
        Integer state = 1;
        int size = 5;
        int cursor = 1;
        TodoPageCursorInDTO dto = TodoPageCursorInDTO.builder()
                .cursor(cursor)
                .size(size)
                .state(state)
                .build();

        doReturn(Collections.EMPTY_LIST)
                .when(todoDao)
                .cursorPagingByState(state, size + 1, cursor);

        // when
        TodoPageCursorOutDTO result = todoService.getPageCursorTodo(dto);

        // then
        assertEquals(0, result.getSize());
        assertEquals(-1, result.getNextCursor());
        assertEquals(0, result.getTodoList().size());
        assertFalse(result.isHasNext());
    }
}