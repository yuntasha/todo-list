package practice.project.todo_list.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice.project.todo_list.web.dto.PutDailyRequestDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyModifyDTOTest {

    @Test
    @DisplayName("Put 요청 제대로 변환되는지 확인")
    void ofPutDailyRequestDTO() {
        // given
        int id = 10;
        String title = "title";
        String content = "content";
        LocalDate deadline = LocalDate.of(2020, 2, 2);
        PutDailyRequestDTO requestDTO = new PutDailyRequestDTO(title, content, deadline);

        // when
        DailyModifyDTO dto = DailyModifyDTO.of(id, requestDTO);

        // then
        assertEquals(id, dto.getId());
        assertEquals(title, dto.getTitle());
        assertEquals(content, dto.getContent());
        assertEquals(deadline, dto.getDeadline());
    }
}