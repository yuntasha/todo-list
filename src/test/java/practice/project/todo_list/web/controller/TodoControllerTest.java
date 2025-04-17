package practice.project.todo_list.web.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import practice.project.todo_list.dto.PatchStateDTO;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoUpdateDto;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.global.error.handler.GlobalExceptionHandler;
import practice.project.todo_list.service.TodoService;
import practice.project.todo_list.web.dto.PatchRequestDTO;
import practice.project.todo_list.web.dto.PutRequestDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @InjectMocks
    private TodoController controller;

    @Mock
    private TodoService todoService;
    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void setUp() {
        this.gson = new Gson();

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 환경_세팅_검사() {
        assertNotNull(controller);
        assertNotNull(mockMvc);
        assertNotNull(gson);
    }

    @ParameterizedTest
    @DisplayName("기간 조회 API 성공")
    @MethodSource("parameterPeriodGood")
    void 기간_조회_성공(String start, String end) throws Exception {
        // given
        final String url = "/api/v1/todo/period";
        doReturn(new ArrayList<>())
                .when(todoService).getTodoByPeriod(any(PeriodDto.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("start", start)
                        .param("end", end)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    private static Stream<Arguments> parameterPeriodGood() {
        return Stream.of(
                Arguments.of("2019-02-01", "2020-03-31"), // yyyy-MM-dd 가능
                Arguments.of("20190201", "20190202"), // yyyyMMdd 가능
                Arguments.of("20190203", "20190202") // 시작 기간이 더 빨라도 가능
        );
    }

    @ParameterizedTest
    @DisplayName("기간 조회 API 입력 형식 오류")
    @MethodSource("parameterPeriodFormmatError")
    void 기간_조회_요청_실패(String start, String end) throws Exception {
        // given
        final String url = "/api/v1/todo/period";

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("start", start)
                        .param("end", end)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> parameterPeriodFormmatError() {
        return Stream.of(
                Arguments.of("2019-02-01", "2020-03-32"), // 존재하지 않는 날짜
                Arguments.of("20190203", "20190202a"), // 암튼 안됨
                Arguments.of(null, "20190203"), // null 안됨
                Arguments.of("20190203", null), // null 안됨
                Arguments.of("", "") // 값이 비어있으면 에러
        );
    }

    @ParameterizedTest
    @DisplayName("상태 변환 API 성공")
    @MethodSource("parameterStateSuccess")
    void patchStateFormatSuccess(String id, String state) throws Exception {
        // given
        final String url = "/api/v1/todo/" + id + "/state";
        doNothing().when(todoService).patchState(any(PatchStateDTO.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .content(gson.toJson(new PatchRequestDTO(Integer.parseInt(state))))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    private static Stream<Arguments> parameterStateSuccess() {
        return Stream.of(
                Arguments.of("1", "2"), // state 범위 초과
                Arguments.of("1", "1"), // state 범위 초과
                Arguments.of("1", "0"), // state 범위 초과
                Arguments.of("21", "2"), // 아이디 음수
                Arguments.of("22", "1"), // 아이디 0
                Arguments.of("22", "0") // 둘 다 문제
        );
    }

    @ParameterizedTest
    @DisplayName("상태 변환 API 실패 - 형식 오류")
    @MethodSource("parameterStateFormmatError")
    void patchStateFormatError(String id, String state) throws Exception {
        // given
        final String url = "/api/v1/todo/" + id + "/state";
        String json = "{\"state\": \"" + state + "\"}";

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> parameterStateFormmatError() {
        return Stream.of(
                Arguments.of("1", "-1"), // state 범위 초과
                Arguments.of("1", "3"), // state 범위 초과
                Arguments.of("1", "5"), // state 범위 초과
                Arguments.of("0", "4bd") // state가 숫자가 아님
        );
    }

    @Test
    @DisplayName("상태 변환 API 실패 - 존재하지 않는 id")
    void patchStateNotExistError() throws Exception {
        // given
        String id = "3";
        String state = "2";
        final String url = "/api/v1/todo/" + id + "/state";
        String json = "{\"state\": \"" + state + "\"}";

        doThrow(new BusinessException(TodoErrorCode.TODO_NOT_FOUND))
                .when(todoService)
                .patchState(any(PatchStateDTO.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("해당 TODO는 존재하지 않습니다.")));
    }

    @Test
    @DisplayName("Todo 제목, 내용 수정 성공")
    void updateTodoSuccess() throws Exception {
        // given
        String id = "3";
        final String url = "/api/v1/todo/" + id;

        doNothing()
                .when(todoService)
                .updateTodo(any(TodoUpdateDto.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(new PutRequestDTO("제목 1", "내용 1")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 제목, 내용 수정 실패 - 존재하지 않는 아이디")
    void updateTodoFailure() throws Exception {
        // given
        String id = "3";
        final String url = "/api/v1/todo/" + id;

        doThrow(new BusinessException(TodoErrorCode.TODO_NOT_FOUND))
                .when(todoService)
                .updateTodo(any(TodoUpdateDto.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(new PutRequestDTO("제목 1", "내용 1")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("해당 TODO는 존재하지 않습니다.")));
    }

    @ParameterizedTest
    @DisplayName("Todo 제목, 내용 수정 실패 - 유효성 검사 실패")
    @MethodSource("parameterUpdateFormmatError")
    void updateTodoValidFailure(String title, String content) throws Exception {
        // given
        String id = "3";
        final String url = "/api/v1/todo/" + id;

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(new PutRequestDTO(title, content)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Valid error")));
    }

    private static Stream<Arguments> parameterUpdateFormmatError() {
        return Stream.of(
                Arguments.of("", "-1"), // 빈칸
                Arguments.of("1", ""), // 빈칸
                Arguments.of(null, "5"), // null
                Arguments.of("0", null) // null
        );
    }
}