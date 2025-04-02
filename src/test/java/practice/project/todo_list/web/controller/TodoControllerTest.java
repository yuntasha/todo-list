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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.global.error.handler.GlobalExceptionHandler;
import practice.project.todo_list.service.TodoService;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
}