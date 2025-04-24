package practice.project.todo_list.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.project.todo_list.dao.DailyDao;
import practice.project.todo_list.domain.Daily;
import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.DailyTitleDTO;
import practice.project.todo_list.dto.PostDailyDTO;
import practice.project.todo_list.global.error.code.DailyErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("데일리 생성 성공 - 데드라인 있음")
    void postDailySuccess() {
        // given
        doReturn(1)
                .when(dailyDao).create(any(Daily.class));
        PostDailyDTO dto = PostDailyDTO.builder()
                .title("test 1")
                .content("content 1")
                .deadline(LocalDate.now())
                .build();

        // when
        // then
        assertDoesNotThrow(() -> dailyService.postDaily(dto));
    }

    @Test
    @DisplayName("데일리 생성 성공 - 데드라인 없음")
    void postDailySuccessNoDeadline() {
        // given
        doReturn(1)
                .when(dailyDao).create(any(Daily.class));
        PostDailyDTO dto = PostDailyDTO.builder()
                .title("test 1")
                .content("content 1")
                .build();

        // when
        // then
        assertDoesNotThrow(() -> dailyService.postDaily(dto));
    }

    @Test
    @DisplayName("데일리 생성 실패 - 데드라인이 현재보다 빠름")
    void postDailyFailureDeadlineIsPast() {
        // given
        PostDailyDTO dto = PostDailyDTO.builder()
                .title("test 1")
                .content("content 1")
                .deadline(LocalDate.now().minusDays(1L))
                .build();

        // when
        BusinessException ex = assertThrows(BusinessException.class, () -> dailyService.postDaily(dto));

        // then
        assertEquals(DailyErrorCode.DAILY_DEADLINE_PAST, ex.getErrorCode());
    }

    @Test
    @DisplayName("데일리 id 기반 조회 성공")
    void findDailyById() {
        // given
        int id = 1;
        Daily daily = Daily.builder()
                .id(1)
                .title("title1")
                .content("content1")
                .deadline(LocalDate.of(2020,2, 2))
                .createAt(LocalDateTime.of(2020,2,2,2,2,2))
                .updateAt(LocalDateTime.of(2020,2,2,2,2,2))
                .build();
        doReturn(Optional.of(daily))
                .when(dailyDao).findById(id);

        // when
        DailyDetailDTO dto = dailyService.getDailyDetail(id);

        // then
        assertEquals(1, dto.getId());
        assertEquals("title1", dto.getTitle());
        assertEquals("content1", dto.getContent());
    }

    @Test
    @DisplayName("데일리 id 기반 조회 성공 - content is null")
    void findDailyByIdContentNull() {
        // given
        int id = 1;
        Daily daily = Daily.builder()
                .id(1)
                .title("title1")
                .deadline(LocalDate.of(2020,2, 2))
                .createAt(LocalDateTime.of(2020,2,2,2,2,2))
                .updateAt(LocalDateTime.of(2020,2,2,2,2,2))
                .build();
        doReturn(Optional.of(daily))
                .when(dailyDao).findById(id);

        // when
        DailyDetailDTO dto = dailyService.getDailyDetail(id);

        // then
        assertEquals(1, dto.getId());
        assertEquals("title1", dto.getTitle());
        assertTrue(dto.getContent().isBlank());
    }
    
    @Test
    @DisplayName("데일리 id 기반 조회 실패 - 존재하지 않는 id")
    void findDailyByIdNotFoundFailure() {
        // given
        int id = 100;
        doReturn(Optional.empty())
                .when(dailyDao).findById(id);
        
        // when
        BusinessException ex = assertThrows(BusinessException.class, () -> dailyService.getDailyDetail(id));

        // then
        assertEquals(DailyErrorCode.DAILY_NOT_FOUND, ex.getErrorCode());
    }
}