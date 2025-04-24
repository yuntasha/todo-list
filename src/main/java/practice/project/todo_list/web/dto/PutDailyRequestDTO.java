package practice.project.todo_list.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PutDailyRequestDTO {
    @NotBlank(message = "제목은 반드시 존재해야합니다.")
    private String title;
    @Nullable
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate deadline;
}
