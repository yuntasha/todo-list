package practice.project.todo_list.web.controller;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.PostDailyDTO;
import practice.project.todo_list.global.error.handler.GlobalExceptionHandler;
import practice.project.todo_list.service.DailyService;
import practice.project.todo_list.web.dto.PostDailyReqestDTO;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DailyControllerTest {

    @InjectMocks
    private DailyController controller;

    @Mock
    private DailyService dailyService;

    private Gson gson;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    }
                })
                .create();

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

    @Test
    @DisplayName("데일리 리스트 받기 성공")
    void getDailyListSuccess() throws Exception {
        // given
        final String url = "/api/v1/daily";
        doReturn(List.of(new DailyTitleDTO(1, "abc", LocalDate.MAX), new DailyTitleDTO(1, "abc", LocalDate.MAX), new DailyTitleDTO(1, "abc", LocalDate.MAX)))
                .when(dailyService)
                .getDailyList();

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @ParameterizedTest
    @DisplayName("데일리 생성 성공")
    @MethodSource("parameterPostSuccess")
    void postDailySuccess(String title, String content, LocalDate deadline) throws Exception {
        // given
        final String url = "/api/v1/daily";
        final PostDailyReqestDTO dto = new PostDailyReqestDTO(title, content, deadline);
        doNothing()
                .when(dailyService)
                .postDaily(any(PostDailyDTO.class));

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        );


        // then
        result.andExpect(status().isOk());
    }

    private static Stream<Arguments> parameterPostSuccess() {
        return Stream.of(
                Arguments.of("title", "content", LocalDate.now()),
                Arguments.of("title", null, LocalDate.now()), // 내용 없음
                Arguments.of("title", "", LocalDate.now()), // 내용 빈칸
                Arguments.of("title", "content", null), // 데드라인 없음
                Arguments.of("title", null, null) // 내용 데드라인 없음
        );
    }

    @ParameterizedTest
    @DisplayName("데일리 생성 실패 - 제목이 없음")
    @MethodSource("parameterPostFailure")
    void postDailyFailure(String title, String content, LocalDate deadline) throws Exception {
        // given
        final String url = "/api/v1/daily";
        final PostDailyReqestDTO dto = new PostDailyReqestDTO(title, content, deadline);

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        );


        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Valid error")));
    }

    private static Stream<Arguments> parameterPostFailure() {
        return Stream.of(
                Arguments.of(null, "content", LocalDate.now()), // 제목 null
                Arguments.of("", "content", LocalDate.now()) // 제목 없음
        );
    }
}