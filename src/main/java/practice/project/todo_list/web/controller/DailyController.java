package practice.project.todo_list.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.project.todo_list.dto.DailyDetailDTO;
import practice.project.todo_list.dto.PostDailyDTO;
import practice.project.todo_list.global.response.SuccessResponseDto;
import practice.project.todo_list.service.DailyService;
import practice.project.todo_list.web.dto.GetDailyListResponseDTO;
import practice.project.todo_list.web.dto.PostDailyReqestDTO;

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

    @PostMapping()
    public SuccessResponseDto<Object> postDaily(@Valid @RequestBody PostDailyReqestDTO postDailyReqestDTO) {
        dailyService.postDaily(PostDailyDTO.from(postDailyReqestDTO));
        return SuccessResponseDto.success();
    }

    @GetMapping("{id}/detail")
    public SuccessResponseDto<DailyDetailDTO> getDailyById(@Positive @PathVariable int id) {
        return SuccessResponseDto.success(dailyService.getDailyDetail(id));
    }
}
