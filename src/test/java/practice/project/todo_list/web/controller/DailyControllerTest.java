package practice.project.todo_list.web.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import practice.project.todo_list.global.error.handler.GlobalExceptionHandler;
import practice.project.todo_list.service.DailyService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
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
        result
                .andExpect(status().isOk());
    }

}