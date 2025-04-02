package practice.project.todo_list.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class GetPeriodRequestDto {

    @NotNull(message = "시작 시간이 존재해야 합니다.")
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate start;

    @NotNull(message = "끝 시간이 존재해야 합니다.")
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate end;
}
