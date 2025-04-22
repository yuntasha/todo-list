package practice.project.todo_list.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.project.todo_list.dao.DailyDao;
import practice.project.todo_list.dto.DailyTitleDTO;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DailyServiceImplTest {

    @InjectMocks
    private DailyServiceImpl dailyService;

    @Mock
    private DailyDao dailyDao;

    @Test
    @DisplayName("데일리 전부 조회 API 성공 - 비어있는 경우")
    void getDailyListEmptySuccess() {
        // given
        doReturn(List.of())
                .when(dailyDao).findAll();
        
        // when
        List<DailyTitleDTO> dailyList = dailyService.getDailyList();

        // then
        assertTrue(dailyList.isEmpty());
    }

    @Test
    @DisplayName("데일리 전부 조회 API 성공 - 몇 개 있는 경우")
    void getDailyListSuccess() {
        // given
        doReturn(List.of(new DailyTitleDTO(1, "abc", LocalDate.MAX), new DailyTitleDTO(1, "abc", LocalDate.MAX), new DailyTitleDTO(1, "abc", LocalDate.MAX)))
                .when(dailyDao).findAll();

        // when
        List<DailyTitleDTO> dailyList = dailyService.getDailyList();

        // then
        assertEquals(3, dailyList.size());
    }
}