package practice.project.todo_list.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.project.todo_list.global.response.SuccessResponseDto;
import practice.project.todo_list.service.DailyService;
import practice.project.todo_list.web.dto.GetDailyListResponseDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/daily")
@Validated
public class DailyController {
    private final DailyService dailyService;

    @GetMapping()
    public SuccessResponseDto<GetDailyListResponseDTO> getDailyList() {
        return SuccessResponseDto.success(new GetDailyListResponseDTO(dailyService.getDailyList()));
    }
}
