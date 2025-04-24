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
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.DailyModifyDTO;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.PostDailyDTO;
import practice.project.todo_list.global.error.code.DailyErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.global.error.handler.GlobalExceptionHandler;
import practice.project.todo_list.service.DailyService;
import practice.project.todo_list.web.dto.PostDailyReqestDTO;
import practice.project.todo_list.web.dto.PutDailyRequestDTO;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @Test
    @DisplayName("데일리 상세정보 받기 성공 - content 있음")
    void getDailyByIdSuccessWithContent() throws Exception {
        // given
        final String url = "/api/v1/daily/1/detail";
        DailyDetailDTO dto = DailyDetailDTO.builder()
                .id(1)
                .title("title1")
                .content("content1")
                .deadline(LocalDate.of(2020,2, 2))
                .createAt(LocalDateTime.of(2020,2,2,2,2,2))
                .updateAt(LocalDateTime.of(2020,2,2,2,2,2))
                .build();

        doReturn(dto)
                .when(dailyService)
                .getDailyDetail(1);

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.content").value(containsString("content1")));
    }

    @Test
    @DisplayName("데일리 상세정보 받기 성공 - content 없음")
    void getDailyByIdSuccessBlankContent() throws Exception {
        // given
        final String url = "/api/v1/daily/1/detail";
        DailyDetailDTO dto = DailyDetailDTO.builder()
                .id(1)
                .title("title1")
                .content("")
                .deadline(LocalDate.of(2020,2, 2))
                .createAt(LocalDateTime.of(2020,2,2,2,2,2))
                .updateAt(LocalDateTime.of(2020,2,2,2,2,2))
                .build();

        doReturn(dto)
                .when(dailyService)
                .getDailyDetail(1);

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.content").value(containsString("")));
    }

    @Test
    @DisplayName("데일리 상세정보 받기 실패 - 존재하지 않는 ID")
    void getDailyByIdFailureNotFound() throws Exception {
        // given
        final String url = "/api/v1/daily/1/detail";

        doThrow(new BusinessException(DailyErrorCode.DAILY_NOT_FOUND))
                .when(dailyService)
                .getDailyDetail(1);

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("해당 id를 가진 daily가 존재하지 않습니다.")));
    }

    @Test
    @DisplayName("데일리 상세정보 받기 실패 - 아이디가 양수가 아님")
    void getDailyByIdFailureNotPositive() throws Exception {
        // given
        final String url = "/api/v1/daily/abcd/detail";

        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(containsString("COMMON-002")));
    }

    @Test
    @DisplayName("데일리 삭제 성공")
    void deleteDailyByIdSuccess() throws Exception {
        // given
        final String url = "/api/v1/daily/1";
        doNothing().when(dailyService).deleteDaily(1);

        // when
        final ResultActions result = mockMvc.perform((
                MockMvcRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                ));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("데일리 삭제 실패 - 아이디 존재하지 않음")
    void deleteDailyByIdFailureNotFound() throws Exception {
        // given
        final String url = "/api/v1/daily/100";
        doThrow(new BusinessException(DailyErrorCode.DAILY_NOT_FOUND))
                .when(dailyService).deleteDaily(100);

        // when
        final ResultActions result = mockMvc.perform((
                MockMvcRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
        ));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(containsString("DAILY-002")));
    }

    @Test
    @DisplayName("데일리 수정 실패 - 존재하지 않은 id")
    void putDailyById() throws Exception {
        // given
        final String url = "/api/v1/daily/1";
        final PutDailyRequestDTO body = new PutDailyRequestDTO("title", "content", LocalDate.of(2020, 12, 12));

        doNothing()
                .when(dailyService).update(any(DailyModifyDTO.class));

        // when
        final ResultActions result = mockMvc.perform((
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(body))
        ));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("데일리 수정 실패 - 존재하지 않은 id")
    void putDailyByIdFailureNotFound() throws Exception {
        // given
        final String url = "/api/v1/daily/100";
        final PutDailyRequestDTO body = new PutDailyRequestDTO("title", "content", LocalDate.of(2020, 12, 12));

        doThrow(new BusinessException(DailyErrorCode.DAILY_NOT_FOUND))
                        .when(dailyService).update(any(DailyModifyDTO.class));

        // when
        final ResultActions result = mockMvc.perform((
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(body))
        ));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(containsString("DAILY-002")));
    }
}